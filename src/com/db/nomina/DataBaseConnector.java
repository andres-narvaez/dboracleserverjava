package com.db.nomina;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseConnector {
	static String dbUrl;
	static String userName;
	static String password;
	static Connection connection;
	
	public DataBaseConnector(String url, String usr, String pws) {
		dbUrl = url;
		userName = usr;
		password = pws;
		startConnection();
	}
	
	public void startConnection() {
		try {
			connection = DriverManager.getConnection(dbUrl, userName, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void stopConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ResultSet executeQuery(String sql) throws SQLException  {
		
		Statement stmnt = connection.createStatement();
		
		return stmnt.executeQuery(sql);
	}
	
	public void executeUpdate(String sql, String tableName) throws SQLException  {
		Statement stmnt = connection.createStatement();	
		int rows = stmnt.executeUpdate(sql);
		
		if (rows > 0) {
			System.out.println("Columnas en la tabla : " + tableName + " se han insertado.");
		}
	}
		
}
