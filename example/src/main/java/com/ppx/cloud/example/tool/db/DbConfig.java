/**
 * 
 */
package com.ppx.cloud.example.tool.db;

import java.util.HashSet;
import java.util.Set;

/**
 * @author mark
 * @date 2019年8月22日
 */
public class DbConfig {
	
	public static Set<String> TABLE_SET = new HashSet<String>();
	
	static {
		TABLE_SET.add("core_demo");
		TABLE_SET.add("core_more");
		TABLE_SET.add("repo_todo");
		TABLE_SET.add("core_demo_sub");
		// 'core_demo', 'core_db_test', 'core_demo_sub', 'core_demo_main', 'core_more'
	}
	
}
