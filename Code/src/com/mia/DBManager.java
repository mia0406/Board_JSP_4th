package com.mia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DBManager {
	
	public static Connection getConnection() {
		
		String driver="oracle.jdbc.driver.OracleDriver";
		String url="jdbc:oracle:thin:@localhost:1521:orcl";
		String id="mia";
		String password="mia";
		
		Connection conn = null;
		
		try {
			
			Class.forName(driver);
			conn=DriverManager.getConnection(url, id, password);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return conn;
	}
	
	
	public static void main(String[] args) {
		
		Connection conn = DBManager.getConnection();
		
		System.out.println(conn);
	}

	/*
	public static Connection getConnection() {
		
		Connection conn = null;
		
		try {
			
			Context initContext = new InitialContext();
			Context envContext = (Context)initContext.lookup("java:/comp/env");
			//DataSource ds = (DataSource)envContext.lookup("jdbc/myoracle");
			DataSource ds = (DataSource) envContext.lookup("java:comp/env/jdbc/oracle");
			
			conn = ds.getConnection();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return conn;
	} 
	*/
	
	public static void close(Connection conn, Statement stmt, ResultSet rs) {
		
		try {
			rs.close();
			stmt.close();
			conn.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
