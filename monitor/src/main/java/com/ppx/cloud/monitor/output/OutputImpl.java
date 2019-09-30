/**
 * 
 */
package com.ppx.cloud.monitor.output;

import java.text.SimpleDateFormat;
import java.util.BitSet;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ppx.cloud.base.exception.ExceptionPojo;
import com.ppx.cloud.base.exception.ExceptionUtils;
import com.ppx.cloud.base.exception.custom.ErrorException;
import com.ppx.cloud.base.exception.custom.FatalException;
import com.ppx.cloud.base.jdbc.MyDaoSupport;
import com.ppx.cloud.base.mvc.ObjectMapperCustomer;
import com.ppx.cloud.base.util.ApplicationUtils;
import com.ppx.cloud.base.util.DateUtils;
import com.ppx.cloud.base.util.MD5Utils;
import com.ppx.cloud.monitor.cache.MonitorCache;
import com.ppx.cloud.monitor.pojo.AccessLog;
import com.ppx.cloud.monitor.util.AccessLogUtils;
import com.ppx.cloud.monitor.util.MonitorUtils;

/**
 * @author mark
 * @date 2019年9月16日
 */
@Service
public class OutputImpl extends MyDaoSupport {
	
	private static Logger logger = LoggerFactory.getLogger(OutputImpl.class);

	
	public void insertStart(Map<String, Object> serviceInfo,  Map<String, Object> startInfo, Date startTime) {
		String serviceSql = "insert into monitor_service(service_id, service_info) values(?, ?) on duplicate key update service_info = ?";
		String serviceInfoJson = ObjectMapperCustomer.toJson(serviceInfo);
		getJdbcTemplate().update(serviceSql, ApplicationUtils.getServiceId(), serviceInfoJson, serviceInfoJson);
		
		
		String startInfoJson = ObjectMapperCustomer.toJson(startInfo);
		String startupSql = "insert into monitor_startup(startup_time, service_id, startup_info) values(?, ?, ?)";
		getJdbcTemplate().update(startupSql, startTime, ApplicationUtils.getServiceId(), startInfoJson);
	}
	
	public void initSqlAndUriCache() {
		String md5Sql = "select sql_md5, max_time from monitor_stat_sql order by times desc limit 1000";
		List<Map<String, Object>> sqlList = getJdbcTemplate().queryForList(md5Sql);
		for (Map<String, Object> map : sqlList) {
			MonitorCache.putSqlMaxTime((String)map.get("sql_md5"), (Integer)map.get("max_time"));
		}
	
		String uriSql = "select u.uri_seq, m.uri_text, u.max_time from monitor_stat_uri u join monitor_uri_seq_map m"
				+ " on u.uri_seq = m.uri_seq order by u.times desc limit 1000";
		List<Map<String, Object>> uriList = getJdbcTemplate().queryForList(uriSql);
		for (Map<String, Object> map : uriList) {
			MonitorCache.putUri((String)map.get("uri_text"), (Integer)map.get("uri_seq"), (Integer)map.get("max_time"));
		}
	}
	
	public void insertGather(Date gatherTime, int isOver, long maxProcessingTime, int concurrentN, Map<String, Object> gatherMap,
			Map<String, Object> lastUpdate) {
		String gatherInfoJson = ObjectMapperCustomer.toJson(gatherMap);
        String gatherSql = "insert into monitor_gather(service_id, gather_time, is_over, max_processing_time, concurrent_num, gather_info)" +
        	" values(?, ?, ?, ?, ?, ?)";
        getJdbcTemplate().update(gatherSql, ApplicationUtils.getServiceId(), gatherTime, isOver, maxProcessingTime, concurrentN, gatherInfoJson);
        
        String lastUpdateJson = ObjectMapperCustomer.toJson(lastUpdate);
        String updateServiceSql = "update monitor_service set service_last_info = ? where service_id = ?";
        getJdbcTemplate().update(updateServiceSql, lastUpdateJson, ApplicationUtils.getServiceId());
	}
	
	// 返回accessId
	public int insertAccess(AccessLog a) {
		if (a.getUriSeq() == null) {
			String insertSql = "insert ignore into monitor_uri_seq_map(uri_text) values(?)";
			getJdbcTemplate().update(insertSql, a.getUri());
			
			String seqSql = "select uri_seq from monitor_uri_seq_map where uri_text = ?";
			int uriSeq = getJdbcTemplate().queryForObject(seqSql, Integer.class, a.getUri());
			a.setUriSeq(uriSeq);
		}
		
		String[] timeStr = new SimpleDateFormat(DateUtils.TIME_PATTERN).format(a.getBeginTime()).split(" ");
		long useMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024;
        
		var map = Map.of("ip", a.getIp(), "q", a.getQueryString(), "mem", useMemory, "aid", a.getAccountId() == null ? -1 : a.getAccountId());
		String accessInfo = ObjectMapperCustomer.toJson(map);
		String insertSql = "insert into monitor_access(access_date, access_time, service_id, uri_seq, spend_time, access_info) values(?, ?, ?, ?, ?, ?)";
		getJdbcTemplate().update(insertSql, timeStr[0], timeStr[1], ApplicationUtils.getServiceId(), a.getUriSeq(), a.getSpendTime(), accessInfo);
		int accessId = super.getLastInsertId();
		
		// 日志logger
		if (a.getLog() != null) {
			var logMap = new LinkedHashMap<String, String>();
			a.getLog().forEach(s -> {
				// marker<<m>>log
				String[] ss = s.split("<<m>>");
				String marker = "0";
				String log = s;
				if (ss.length == 2) {
					marker = ss[0];
					log = ss[1];
				}
				var markerVal = logMap.get(marker) == null ? "" : logMap.get(marker);
				logMap.put(marker, markerVal + log);
			});
			
			String logSql = "insert into monitor_access_log(access_id, marker, log) values(?, ?, ?)";
			logMap.forEach((k, v) -> {
				if (v.length() > 1024) {
					v = v.substring(0, 1024 - 3) + "...";
				}
				getJdbcTemplate().update(logSql, accessId, k, v);
			});
		}
		
		return accessId;
	}
	
	public void insertStatUri(AccessLog a) {
		int s =  a.getSpendTime();
		
		String updateSql = "insert into monitor_stat_uri(uri_seq, total_time, max_time, avg_time, distribute) values(?, ?, ?, ?, ?)" +
				" on duplicate key update times = times + 1, total_time = total_time + ?, avg_time = total_time / times, distribute = "
				+ getUpdateDistribute(s) + ", lasted = now()";
		getJdbcTemplate().update(updateSql, a.getUriSeq(), s, s, s, getInsertDistribute(s), s);
		
		// maxTime, 缓存uri最大的maxTime值
		Integer maxTime = MonitorCache.getUriMaxTime(a.getUriSeq());
		if (maxTime == null || s > maxTime) {
			String maxTimeSql = "select if (max_time > ?, max_time, ?) from monitor_stat_uri where uri_seq = ?";
			maxTime = getJdbcTemplate().queryForObject(maxTimeSql, Integer.class, s, s, a.getUriSeq());

			Map<String, Object> maxMap = getUriMaxDetail(a);
			String maxMapJson = ObjectMapperCustomer.toJson(maxMap);
			String updateMaxSql = "update monitor_stat_uri set max_time = ?, max_detail = ? where uri_seq = ?";
			getJdbcTemplate().update(updateMaxSql, maxTime, maxMapJson, a.getUriSeq());
			MonitorCache.putUriMaxTime(a.getUriSeq(), maxTime);
		}
	}
	
	public void insertStatSql(AccessLog a) {
		if (a.getSqlList().size() != a.getSqlBeginTime().size()
				|| a.getSqlList().size() != a.getSqlSpendTime().size()) {
			logger.error("insertStatSql sql length error:{}", a);
			return;
		}
		
		// 一个请求对应多个sql
		for (int i = 0; i < a.getSqlList().size(); i++) {

			String sqlText = a.getSqlList().get(i);
			String sqlMd5 = sqlText;
			if (sqlText.length() != 32 || sqlText.indexOf(" ") > 0) {
				// 非缓存md5的值
				sqlMd5 = MD5Utils.getMD5(sqlText);
				String insertSql = "insert ignore into monitor_sql_md5_map(sql_md5, sql_text) values(?, ?)";
				getJdbcTemplate().update(insertSql, sqlMd5, sqlText);
			}
			
			int s =  a.getSqlSpendTime().get(i);
			
			String updateSql = "insert into monitor_stat_sql(sql_md5, total_time, max_time, avg_time, max_sql_count, distribute) values(?, ?, ?, ?, ?, ?)" +
					" on duplicate key update times = times + 1, total_time = total_time + ?, avg_time = total_time / times, max_sql_count = ?, distribute = "
					+ getUpdateDistribute(s) + ", lasted = now()";
			getJdbcTemplate().update(updateSql, sqlMd5, s, s, s, a.getSqlCount().get(i), getInsertDistribute(s), s, a.getSqlCount().get(i));
			
			
			// maxTime, 缓存sql最大的maxTime值
			Integer maxTime = MonitorCache.getSqlMaxTime(sqlMd5);
			if (maxTime == null || s > maxTime) {
				String maxTimeSql = "select if (max_time > ?, max_time, ?) from monitor_stat_sql where sql_md5 = ?";
				maxTime = getJdbcTemplate().queryForObject(maxTimeSql, Integer.class, s, s, sqlMd5);

				Map<String, Object> maxMap = getSqlMaxDetail(a, i);
				String maxMapJson = ObjectMapperCustomer.toJson(maxMap);
				String updateMaxSql = "update monitor_stat_sql set max_time = ?, max_detail = ? where sql_md5 = ?";
				getJdbcTemplate().update(updateMaxSql, maxTime, maxMapJson, sqlMd5);
				MonitorCache.putSqlMaxTime(a.getSqlList().get(i), maxTime);
			}
		}
	}
	
	public void insertDebug(Integer accessId, AccessLog a) {
		Map<String, Object> debug = AccessLogUtils.getDebugMap(a);
		String sql ="insert into monitor_debug(access_id, service_id, debug_time, debug_info) values(?, ?, ?, ?)";
		String debugInfo = ObjectMapperCustomer.toJson(debug);
		getJdbcTemplate().update(sql, accessId, ApplicationUtils.getServiceId(), a.getBeginTime(), debugInfo);
	}
	
	public void insertResponse(AccessLog a) {
		int s = a.getSpendTime();
		String hh = new SimpleDateFormat("yyyyMMddHH").format(a.getBeginTime());
		String sql = "insert into monitor_stat_response(service_id, hh, total_time, max_time, avg_time) values(?, ?, ?, ?, ?)" + 
			" on duplicate key update times = times + 1, total_time = total_time + ?, max_time = if(max_time > ?, max_time, ?), avg_time = total_time / times";
		getJdbcTemplate().update(sql, ApplicationUtils.getServiceId(), hh, s, s, s, s, s, s);
	}
	
	public void insertError(Throwable throwable, Integer accessId,  AccessLog a) {
		
		ExceptionPojo error = ExceptionUtils.getErroCode(throwable);
		String errorSql = "insert into monitor_error(access_id, service_id, error_time, error_code, error_msg) values(?, ?, ?, ?, ?)";
		
		 // FatalException和ErrorException的异常需要打印，不需要修改后端代码，不打印
        if (error.getType() == FatalException.class || error.getType() == ErrorException.class) {
			// 出错时，记录输入参数
        	getJdbcTemplate().update(errorSql, accessId, ApplicationUtils.getServiceId(), a.getBeginTime(), error.getCode(), error.getMsg());
        	
			
			var debug = AccessLogUtils.getDebugMap(a);
			String debugJson = ObjectMapperCustomer.toJson(debug);
			String detailSql = "insert into monitor_error_detail(access_id, error_detail, debug_detail) values(?, ?, ?)";
			getJdbcTemplate().update(detailSql, accessId, MonitorUtils.getExcepiton(throwable), debugJson);
        }
        else {
        	// 出错时，记录输入参数
        	String msg = error.getMsg() + ";param|injson:" + a.getParams() + "|" + a.getInJson();
        	error.setMsg(msg);
        	getJdbcTemplate().update(errorSql, accessId, ApplicationUtils.getServiceId(), a.getBeginTime(), error.getCode(), error.getMsg());
        }
	}
	
	public void insertWarning(AccessLog a, BitSet content) {
		
		
		String sql = "insert into monitor_stat_warning(uri_seq, content) values(?, ?) on duplicate key update lasted = now(), content = ?";
		getJdbcTemplate().update(sql, a.getUriSeq(), content.toLongArray()[0], content.toLongArray()[0]);
	}

	private Map<String, Object> getSqlMaxDetail(AccessLog a, int i) {
		// 最大值对应对象
		Map<String, Object> maxMap = new LinkedHashMap<String, Object>();
		maxMap.put("sid", ApplicationUtils.getServiceId());
		maxMap.put("uri", a.getUri());
		if (a.getQueryString() != null) {
			maxMap.put("str", a.getQueryString());
		}
		maxMap.put("maxed", a.getSqlBeginTime().get(i));
		maxMap.put("sqlc", a.getSqlCount().get(i));
		if (a.getSqlArgMap().get(i) != null) {
			maxMap.put("sqla", a.getSqlArgMap().get(i));
		}
		return maxMap;
	}
	
	private Map<String, Object> getUriMaxDetail(AccessLog a) {
		// uri最大请求时间对应对象
		var maxMap = new LinkedHashMap<String, Object>(8);
		maxMap.put("sid", ApplicationUtils.getServiceId());
		if (a.getQueryString() != null) {
			maxMap.put("str", a.getQueryString());
		}
		maxMap.put("maxed", a.getBeginTime());
		if (!a.getSqlList().isEmpty()) {
			maxMap.put("sql", a.getSqlList());
			maxMap.put("sqls", StringUtils.collectionToCommaDelimitedString(a.getSqlSpendTime()));
			maxMap.put("sqlc", StringUtils.collectionToCommaDelimitedString(a.getSqlCount()));

			StringBuilder sqla = new StringBuilder();
			if (!a.getSqlArgMap().isEmpty()) {
				a.getSqlArgMap().forEach((k, v) -> {
					sqla.append(v.toString());
				});
				maxMap.put("sqla", sqla.toString());
			}
		}
		return maxMap;
	}
	
	
	private static String getInsertDistribute(int t) {
		if (t < 10) {
			return "[1,0,0,0,0,0]";
		} else if (t < 100) {
			return "[0,1,0,0,0,0]";
		} else if (t < 1000) {
			return "[0,0,1,0,0,0]";
		} else if (t < 3000) {
			return "[0,0,0,1,0,0]";
		} else if (t < 10000) {
			return "[0,0,0,0,1,0]";
		} else {
			return "[0,0,0,0,0,1]";
		}
	}
	
	private static String getUpdateDistribute(int t) {
		if (t < 10) {
			return "JSON_SET(distribute, '$[0]', JSON_EXTRACT(distribute, '$[0]') + 1)";
		} else if (t < 100) {
			return "JSON_SET(distribute, '$[1]', JSON_EXTRACT(distribute, '$[1]') + 1)";
		} else if (t < 1000) {
			return "JSON_SET(distribute, '$[2]', JSON_EXTRACT(distribute, '$[2]') + 1)";
		} else if (t < 3000) {
			return "JSON_SET(distribute, '$[3]', JSON_EXTRACT(distribute, '$[3]') + 1)";
		} else if (t < 10000) {
			return "JSON_SET(distribute, '$[4]', JSON_EXTRACT(distribute, '$[4]') + 1)";
		} else {
			return "JSON_SET(distribute, '$[5]', JSON_EXTRACT(distribute, '$[5]') + 1)";
		}
	}
	
}
