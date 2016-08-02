package cn.itcast.utils;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class JdbcUtils_Tomcat {
	private static DataSource ds;
	static {
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			ds = (DataSource) envCtx.lookup("jdbc/EmployeeDB");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public static Connection getConnection() throws SQLException{
		return ds.getConnection();
	}
}
