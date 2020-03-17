/**
 * 
 */
package com.ppx.cloud.example.demo;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.util.Properties;

public class SSHConnection {
	
	public static void runSSHConnection() throws Throwable {
		System.out.println("runSSHConnection........begin.........");
		Session session;
		JSch jsch = null;
		jsch = new JSch();
		session = jsch.getSession("ehrgyslinshi", "sas.sunriver.cn", 60022);
		session.setPassword("redsea1234@sunriverlinsh");

		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);

		session.connect();
		// by security policy, you must connect through a fowarded port
		session.setPortForwardingL(3307, "192.168.88.2", 3306);
	}
	
	public static void main(String[] args) throws Throwable {
		runSSHConnection();
	}
}