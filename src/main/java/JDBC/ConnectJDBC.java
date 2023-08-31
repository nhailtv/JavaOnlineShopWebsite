package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectJDBC {
	private static Connection conn = null;
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		if(conn==null) {
			 	String dbURL = "jdbc:mysql://localhost:3306/E_shopee";
	            String dbUsername = "root";
	            String dbPassword = "Nhailtv12345";
	            Class.forName("com.mysql.cj.jdbc.Driver");
	            conn = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
	            System.out.println("Connected!");
		}		
		return conn;
	}
}
