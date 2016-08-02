package cn.carrental.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class JdbcUtils{
	private static String url = "jdbc:sqlserver://localhost:1433;DatabaseName=test";
	private static String user = "sa";
	private static String password = "456123";
	private JdbcUtils(){}
	static{
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		}catch(ClassNotFoundException e){
			throw new ExceptionInInitializerError(e);
		}
	}
	public static Connection getConnection() throws SQLException{
		return DriverManager.getConnection(url,user,password);
	}
	public static void free(ResultSet rs,Statement st,Connection conn){
		try{
			if(rs!=null) rs.close();
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{
				if(st!=null) st.close();
			}catch(SQLException e){
				e.printStackTrace();
			}finally{
				if(conn!=null)
					try{
						conn.close();
					}catch(SQLException e){
						e.printStackTrace();
					}
			}
		}
	}
}