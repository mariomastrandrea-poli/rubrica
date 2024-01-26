package it.mariomastrandrea.testrubrica.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConfig {
	public static Connection connection;
	
	private String username;
	private String password;
	private String ipServer;
	private String port;
	private String dbName;
	
	
	public DbConfig(String username, String password, String ipServer, String port, String dbName) {
		this.username = username;
		this.password = password;
		this.ipServer = ipServer;
		this.port = port;
		this.dbName = dbName;
	}
	
	public Connection getJdbcConnection() {
		String connectionString = String.format(
			"jdbc:mysql://%s:%s/%s",
			this.ipServer,
			this.port,
			this.dbName
		);
		
		Connection connection;
		
		try {
			connection = DriverManager.getConnection(connectionString, this.username, this.password);
		}
		catch (SQLException sqle) {
        	System.err.println(String.format(
        		"An error occurred creting MySQL Connection with connection string: \"%s\"", 
        		connectionString
        	));
        	sqle.printStackTrace();
            return null;
        }
		
		DbConfig.connection = connection;
		
		return connection;
	}
	
	public static boolean closeConnection() {
		try {
			DbConfig.connection.close();
			DbConfig.connection = null;
		}
		catch (SQLException sqle) {
			System.err.println("An error occurred closing connection");
			return false;
		}
		
		return true;
	}
}
