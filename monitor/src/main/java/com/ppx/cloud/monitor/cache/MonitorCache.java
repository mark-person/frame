/**
 * 
 */
package com.ppx.cloud.monitor.cache;

import java.util.HashMap;
import java.util.Map;

import com.ppx.cloud.monitor.config.MonitorSwitchConfig;


/**
 * 用作sql和uri的缓存, 在StartMonitor时加载
 * @author mark
 * 2019年9月18日
 */
public class MonitorCache {
	
	// 存放sql的md5值，用作缓存, maxTime
	private static Map<String, Integer> sqlMd5MaxTimeMap = new HashMap<String, Integer>();
	
	// 存放uri的seq值，用作缓存
	private static Map<String, Integer> uriTextSeqMap = new HashMap<String, Integer>();
	// 存放seq的maxTime值，用作缓存
	private static Map<Integer, Integer> uriSeqMaxTimeMap = new HashMap<Integer, Integer>();
	
	
	public static Integer getSqlMaxTime(String sqlMd5) {
		return sqlMd5MaxTimeMap.get(sqlMd5);
	}
	
	public static void putSqlMaxTime(String sqlMd5, Integer maxTime) {
		sqlMd5MaxTimeMap.put(sqlMd5, maxTime);
	}
	
	public static Integer getUriSeq(String uri) {
		return uriTextSeqMap.get(uri);
	}
	
	public static Integer getUriMaxTime(Integer uriSeq) {
		return uriSeqMaxTimeMap.get(uriSeq);
	}
	
	public static void putUri(String uri, Integer uriSeq, Integer maxTime) {
		uriTextSeqMap.put(uri, uriSeq);
		uriSeqMaxTimeMap.put(uriSeq, maxTime);
		
		if (MonitorSwitchConfig.IS_DEV) {
			addSeqUriDev(uriSeq, uri);
		}
	}
	
	public static void putUriMaxTime(Integer uriSeq, Integer maxTime) {
		uriSeqMaxTimeMap.put(uriSeq, maxTime);
	}
	
	
	
	// dev开发环境用来把md5的sql或uri的seq转成对应的值 >>>>>
	
	private static Map<Integer, String> seqUriMap = new HashMap<Integer, String>();
	
	private static Map<String, String> md5SqlMap = new HashMap<String, String>();
	
	public static void addSeqUriDev(Integer seq, String uri) {
		seqUriMap.put(seq, uri);
	}
	
	public static String getSeqUriDev(Integer seq) {
		return seqUriMap.get(seq);
	}
	
	public static void addMd5SqlDev(String sqlMd5, String sql) {
		md5SqlMap.put(sqlMd5, sql);
	}
	
	public static String getMd5SqlDev(String sqlMd5) {
		return md5SqlMap.get(sqlMd5);
	}
	
	
}
