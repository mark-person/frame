package com.ppx.cloud.example.tool.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.util.Strings;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ppx.cloud.base.jdbc.MyDaoSupport;
import com.ppx.cloud.base.mvc.ControllerReturn;

@Service
public class DbToolImpl extends MyDaoSupport {
	
	public List<Map<String, Object>> listTable() {
		String sql = "select TABLE_NAME value, concat(trim(substring_index(TABLE_COMMENT, '--', 1)), '(', TABLE_NAME, ')') text "
				+ " from information_schema.tables where TABLE_NAME in (:tableName)";
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tableName", DbConfig.TABLE_SET);
		
		NamedParameterJdbcTemplate nameJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
		return nameJdbcTemplate.queryForList(sql, paramMap);
	}
	
	public List<Map<String, Object>> listColumnMsg(String tableName) {
		if (!DbConfig.TABLE_SET.contains(tableName)) {
			return new ArrayList<Map<String, Object>>();
		}
		// DATA_TYPE = 'date'
		// ID -- 其它说明
		// IS_NULLABLE:YES|NO COLUMN_KEY:PRI
		String sql = "select COLUMN_NAME value, trim(substring_index(COLUMN_COMMENT, '--', 1)) comment, DATA_TYPE, IS_NULLABLE, COLUMN_KEY"
				+ " from information_schema.COLUMNS where TABLE_NAME = ? order by ORDINAL_POSITION";
		List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql, tableName);
		
		for (Map<String, Object> map : list) {
			String comment = (String)map.get("comment");
			String value = (String)map.get("value");
			String isNull = (String)map.get("IS_NULLABLE");
			String key = (String)map.get("COLUMN_KEY");
			
			isNull = "NO".equals(isNull) ? "*" : "";
			key = "PRI".equals(key) ? "PK" : "";
			
			String text = comment.split("\\|")[0] + "(" + value + ")" + isNull + key;
			map.put("text", text);
		}
		return list;
	}
	
	public List<Map<String, Object>> queryData(String tableName, String colVal[], String qKey, String qOperator, String qValue, DbPage page) {
		if (!DbConfig.TABLE_SET.contains(tableName)) {
			return new ArrayList<Map<String, Object>>();
		}
		
		NamedParameterJdbcTemplate nameJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tableName", tableName);
		paramMap.put("colVal", Arrays.asList(colVal));
		String commentSql = "select COLUMN_NAME, trim(substring_index(COLUMN_COMMENT, '--', 1)) COLUMN_COMMENT from information_schema.COLUMNS where TABLE_NAME = :tableName" +
				" and COLUMN_NAME in (:colVal)";
		List<Map<String, Object>> commentList = nameJdbcTemplate.queryForList(commentSql, paramMap);
		
		if (commentList.size() != colVal.length) {
			return new ArrayList<Map<String, Object>>();
		}
		
		Map<String, String> leftColMap = new HashMap<String, String>();
		String leftTable = "";
		
		for (Map<String, Object> map : commentList) {
			String name = (String)map.get("COLUMN_NAME");
			String comment = (String)map.get("COLUMN_COMMENT");
			if (comment.contains("|")) {
			    comment = comment.split("\\|")[1]; 
			}
			if (comment.contains(";")) {
				comment = comment.split(";")[0];
			}
			if (comment.startsWith("select")) {
				comment = comment.replaceAll("\\s{1,}", " ");
				String[] itemArray = comment.split(" ");
				if (itemArray.length < 4) continue;
				
				String leftTableName = itemArray[4];
				String leftId = itemArray[1].replace(",", "");
				String leftName = itemArray[2].replace(",", "");
				
				// 关联表字段
				leftColMap.put(name, "t." + name + " " + name + "," +  leftTableName + "." + leftName + " " + name + "__desc");
				leftTable += " left join " + leftTableName + " on t." + name + " = " + leftTableName + "." + leftId;
			}
		}
		
		String colValStr = Strings.join(Arrays.asList(colVal), ',');
		
		colValStr = "t." + colValStr.replaceAll(",", ",t.");
		
		for (String key : leftColMap.keySet()) {
			colValStr = colValStr.replace("t." + key, leftColMap.get(key));
		}
		
		String tempSql = "select " + colValStr + " from " + tableName + " t " + leftTable;		
		
		String where = "";
		List<Object> paraList = new ArrayList<Object>();
		if (Strings.isNotEmpty(qKey) && Strings.isNotEmpty(qValue)) {
			where = " where t." + qKey + " " + qOperator + " ? ";
		
			if ("like".equals(qOperator)) {
				qValue = "%" + qValue + "%";
			}
			paraList.add(qValue);
		}
		
		// 总数
		String countSql = "select count(*) from " + tableName + " t " + where;
		int count = getJdbcTemplate().queryForObject(countSql, Integer.class, paraList.toArray());
		page.setCount(count);
		if (count == 0) {
			return new ArrayList<Map<String, Object>>();
		}
		String limit = "";
		if (count > page.getLimit()) {
			limit = " limit " + page.getLimit();
		}
		
		String orderBy = "";
		if (Strings.isNotEmpty(page.getOrderName()) && Strings.isNotEmpty(page.getOrderType())) {
			orderBy = " order by t." + page.getOrderName() + " " + page.getOrderType();
		}
		
		tempSql += where + orderBy + limit;
		List<Map<String, Object>> list = getJdbcTemplate().queryForList(tempSql, paraList.toArray());
		
		for (Map<String, Object> map : list) {
			map.keySet().forEach(k -> {
				if (map.get(k) == null) {
					map.put(k, "");
				}
			});
		}
		return list;
	}
	
	
	// 
	public Map<String, Object> listSingleData(String tableName, String columnName, String queryVal) {
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String commentSql = "select COLUMN_NAME, trim(substring_index(COLUMN_COMMENT, '--', 1)) COLUMN_COMMENT"
				+ " from information_schema.COLUMNS where TABLE_NAME = ? and COLUMN_NAME = ?";
		Map<String, Object> commentMap = getJdbcTemplate().queryForMap(commentSql, tableName, columnName);
		String sql = ((String)commentMap.get("COLUMN_COMMENT")).split("\\|")[1];
		
		Map<String, String> sqlMsgMap = getSqlMsg(sql);
		String sqlTableName = sqlMsgMap.get("tableName");
		String idColumn = sqlMsgMap.get("idColumn");
		String nameColumn= sqlMsgMap.get("nameColumn");
		String sqlCommentSql = "select trim(substring_index(COLUMN_COMMENT, '--', 1)) COLUMN_COMMENT"
				+ " from information_schema.COLUMNS where TABLE_NAME = ? and (COLUMN_NAME = ? or COLUMN_NAME = ?) order by ORDINAL_POSITION";
		List<String> commList = getJdbcTemplate().queryForList(sqlCommentSql, String.class, sqlTableName, idColumn, nameColumn);
		returnMap.put("title", commList);
		
		if (!Strings.isEmpty(queryVal)) {
			if (sql.contains("where")) {
				sql += " and " + nameColumn + " like ?";
			}
			else {
				sql += " where " + nameColumn + " like ?";
			}
		}
		
		
		// 总数
		String countSql = "select count(*) from (" + sql +") t";
		int count = -1;
		if (Strings.isEmpty(queryVal)) {
			count = getJdbcTemplate().queryForObject(countSql, Integer.class);
		}
		else {
			count = getJdbcTemplate().queryForObject(countSql, Integer.class, "%" + queryVal + "%");
		}
		
		// 列表
		List<Map<String, Object>> list = null;
		sql = sql + " limit 10";
		if (Strings.isEmpty(queryVal)) {
			list = getJdbcTemplate().queryForList(sql);
		}
		else {
			list = getJdbcTemplate().queryForList(sql, "%" + queryVal + "%");
		}
		
		for (Map<String, Object> map : list) {
			map.keySet().forEach(k -> {
				if (map.get(k) == null) {
					map.put(k, "");
				}
			});
		}
		
		returnMap.put("count", count);
		returnMap.put("list", list);
		return returnMap;
	}
	
	
	private Map<String, String> getSqlMsg(String sql) {
		Map<String, String> returnMap = new HashMap<String, String>(3);
		sql = sql.replaceAll("\\s{1,}", " ").toLowerCase();
		String[] itemArray = sql.split(" ");
		if (itemArray.length < 5) {
			return returnMap;
		}
		String tableName = "";
		String idColumn = itemArray[1].replace(",", "");
		String nameColumn= itemArray[2].replace(",", "");
		for (String item : itemArray) {
			if ("from".equals(tableName)) {
				tableName = item;
			}
			if (item.equals("from")) {
				tableName = "from";
			}
		}
		returnMap.put("tableName", tableName);
		returnMap.put("idColumn", idColumn);
		returnMap.put("nameColumn", nameColumn);
		return returnMap;
	}
	
	public Map<String, Object> updateOrInsert(Map<String, String[]> map) {
		Set<String> ignoreColSet = new HashSet<String>();
		ignoreColSet.add("actionType");
		ignoreColSet.add("idCode");
		ignoreColSet.add("tableName");
		
		String actionType = map.get("actionType")[0];
		String tableName = map.get("tableName")[0];
		String[] idCode = map.get("idCode");
		
		if (!DbConfig.TABLE_SET.contains(tableName)) {
			return new HashMap<String, Object>();
		}
		
		Set<String> keySet = map.keySet();
		if ("insert".equals(actionType)) {
			// 新增 >>>>>>>>>>>>>>
			List<String> colList = new ArrayList<String>();
			List<String> questionList = new ArrayList<String>();
			List<Object> valList = new ArrayList<Object>();
			for (String key : keySet) {
				if (ignoreColSet.contains(key)) {
					continue;
				}
				colList.add(key);
				questionList.add("?");
				valList.add(map.get(key)[0]);
			}
			String insertSql = "insert into " + tableName + "(" + Strings.join(colList, ',') + ") values(" + Strings.join(questionList, ',') + ")";
			getJdbcTemplate().update(insertSql, valList.toArray());
			return ControllerReturn.of(20000, "新增成功");
		}
		else if ("update".equals(actionType)) {
			List<Object> pkParam = new ArrayList<Object>();
			List<String> pkSqlList = new ArrayList<String>();
			for (int i = 0; i < idCode.length; i++) {
				pkParam.add(map.get(idCode[i])[0]);
				pkSqlList.add(idCode[i] + " = ?");
				ignoreColSet.add(idCode[i]);
			}
			List<Object> para = new ArrayList<Object>();
			StringBuilder sqlSb = new StringBuilder();
			for (String key : keySet) {
				if (ignoreColSet.contains(key)) {
					continue;
				}
				if (map.get(key) != null) {
					sqlSb.append(key + " = " + "?,");
					para.add(map.get(key)[0]);
				}
			}
			para.addAll(pkParam);
			String updateSql = "update " + tableName + " set " + sqlSb.substring(0, sqlSb.length() - 1) 
				+ " where " + StringUtils.collectionToDelimitedString(pkSqlList, " and ");
			getJdbcTemplate().update(updateSql, para.toArray());
			return ControllerReturn.of(20001, "修改成功");
		}
		return ControllerReturn.error(30000, "actionType参数错误");
	}
	
	public int delRow(String tableName, String[] idCode, Map<String, String[]> map) {
		if (!DbConfig.TABLE_SET.contains(tableName) || idCode.length == 0) {
			return 0;
		}
		
		List<Object> pkParam = new ArrayList<Object>();
		List<String> pkSqlList = new ArrayList<String>();
		for (int i = 0; i < idCode.length; i++) {
			pkParam.add(map.get(idCode[i])[0]);
			pkSqlList.add(idCode[i] + " = ?");
		}
		String delSql = "delete from " + tableName + " where " + StringUtils.collectionToDelimitedString(pkSqlList, " and ");
		
		return getJdbcTemplate().update(delSql, pkParam.toArray());
	}
	
	public List<Map<String, Object>> listChainData(String tableName, String columnName) {
		String commentSql = "select COLUMN_NAME, trim(substring_index(COLUMN_COMMENT, '--', 1)) COLUMN_COMMENT"
				+ " from information_schema.COLUMNS where TABLE_NAME = ? and COLUMN_NAME = ?";
		Map<String, Object> commentMap = getJdbcTemplate().queryForMap(commentSql, tableName, columnName);
		
		
		String sql = ((String)commentMap.get("COLUMN_COMMENT")).split(";")[1] + " limit 100";
		
		List<Map<String, Object>> dataList = getJdbcTemplate().queryForList(sql);
		
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : dataList) {
			Map<String, Object> m = new HashMap<String, Object>();
			String vName = (String)map.keySet().toArray()[0];
			String tName = (String)map.keySet().toArray()[1];
			m.put("v", map.get(vName));
			m.put("t", map.get(tName));
			returnList.add(m);
		}
		return returnList;
	}
	
	public List<Map<String, Object>> listChainSecondData(String tableName, String columnName, Integer parentId) {
		String commentSql = "select COLUMN_NAME, trim(substring_index(COLUMN_COMMENT, '--', 1)) COLUMN_COMMENT"
				+ " from information_schema.COLUMNS where TABLE_NAME = ? and COLUMN_NAME = ?";
		Map<String, Object> commentMap = getJdbcTemplate().queryForMap(commentSql, tableName, columnName);
		
		String sqls = ((String)commentMap.get("COLUMN_COMMENT")).split("\\|")[1];
		
		
		String sql = sqls.split(";")[0] + " limit 100";
		
		List<Map<String, Object>> dataList = getJdbcTemplate().queryForList(sql, parentId);
		
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : dataList) {
			Map<String, Object> m = new HashMap<String, Object>();
			String vName = (String)map.keySet().toArray()[0];
			String tName = (String)map.keySet().toArray()[1];
			m.put("v", map.get(vName));
			m.put("t", map.get(tName));
			returnList.add(m);
		}
		return returnList;
	}
	
	
	
	
	
}
