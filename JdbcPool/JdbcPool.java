package utils;

import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

public class JdbcPool implements DataSource {

	private static LinkedList<Connection> list = new LinkedList<Connection>();
	private static Properties config = new Properties();
	static{
		try {
			config.load(JdbcUtils_DBCP.class.getClassLoader().getResourceAsStream("db.properties"));
			
			Class.forName(config.getProperty("driver"));
			for(int i=0;i<10;i++){
				Connection conn = DriverManager.getConnection(config.getProperty("url"), config.getProperty("username"), config.getProperty("password"));
				list.add(conn);
			}
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	
	
	//conn.close()
	/* 在实际开发，发现对象的方法满足不了开发需求时，有三种方式对其进行增强
	 * 1.写一个connecton子类，覆盖close方法，增强close方法
	 * 2.用包装设计模式
	 * 3.用动态代理    aop 面向切面编程
	 */
	public Connection getConnection() throws SQLException {
		
		if(list.size()<=0){
			throw new RuntimeException("数据库忙，请稍会再来！！");
		}
		Connection conn = list.removeFirst();   //mysqlconnection   C
		MyConnection my = new MyConnection(conn);
 		return my;      //my-------preparedStatment   commit   createStatement  close
	}
	

	//1.定义一个类，实现与被增强相同的接口
	//2.在类中定义一个变量，记住被增强对象
	//3.定义一个构造函数，接收被增强对象
	//4.覆盖想增强的方法
	//5.对于不想增强的方法，直接调用目标对象（被增强对象）的方法
	
	class MyConnection implements Connection{
		private Connection conn;
		public MyConnection(Connection conn){
			this.conn = conn;
		}
		public void close(){
			list.add(this.conn);
		}
		public void clearWarnings() throws SQLException {
			this.conn.clearWarnings();
			
		}
		public void commit() throws SQLException {
			this.conn.commit();
			
		}
		public Statement createStatement() throws SQLException {
			return this.conn.createStatement();
		}
		public Statement createStatement(int resultSetType,
				int resultSetConcurrency, int resultSetHoldability)
				throws SQLException {
			return this.conn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
		}
		public Statement createStatement(int resultSetType,
				int resultSetConcurrency) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}
		public boolean getAutoCommit() throws SQLException {
			// TODO Auto-generated method stub
			return false;
		}
		public String getCatalog() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}
		public int getHoldability() throws SQLException {
			// TODO Auto-generated method stub
			return 0;
		}
		public DatabaseMetaData getMetaData() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}
		public int getTransactionIsolation() throws SQLException {
			// TODO Auto-generated method stub
			return 0;
		}
		public Map<String, Class<?>> getTypeMap() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}
		public SQLWarning getWarnings() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}
		public boolean isClosed() throws SQLException {
			// TODO Auto-generated method stub
			return false;
		}
		public boolean isReadOnly() throws SQLException {
			// TODO Auto-generated method stub
			return false;
		}
		public String nativeSQL(String sql) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}
		public CallableStatement prepareCall(String sql, int resultSetType,
				int resultSetConcurrency, int resultSetHoldability)
				throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}
		public CallableStatement prepareCall(String sql, int resultSetType,
				int resultSetConcurrency) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}
		public CallableStatement prepareCall(String sql) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}
		public PreparedStatement prepareStatement(String sql,
				int resultSetType, int resultSetConcurrency,
				int resultSetHoldability) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}
		public PreparedStatement prepareStatement(String sql,
				int resultSetType, int resultSetConcurrency)
				throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}
		public PreparedStatement prepareStatement(String sql,
				int autoGeneratedKeys) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}
		public PreparedStatement prepareStatement(String sql,
				int[] columnIndexes) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}
		public PreparedStatement prepareStatement(String sql,
				String[] columnNames) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}
		public PreparedStatement prepareStatement(String sql)
				throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}
		public void releaseSavepoint(Savepoint savepoint) throws SQLException {
			// TODO Auto-generated method stub
			
		}
		public void rollback() throws SQLException {
			// TODO Auto-generated method stub
			
		}
		public void rollback(Savepoint savepoint) throws SQLException {
			// TODO Auto-generated method stub
			
		}
		public void setAutoCommit(boolean autoCommit) throws SQLException {
			// TODO Auto-generated method stub
			
		}
		public void setCatalog(String catalog) throws SQLException {
			// TODO Auto-generated method stub
			
		}
		public void setHoldability(int holdability) throws SQLException {
			// TODO Auto-generated method stub
			
		}
		public void setReadOnly(boolean readOnly) throws SQLException {
			// TODO Auto-generated method stub
			
		}
		public Savepoint setSavepoint() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}
		public Savepoint setSavepoint(String name) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}
		public void setTransactionIsolation(int level) throws SQLException {
			// TODO Auto-generated method stub
			
		}
		public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
			// TODO Auto-generated method stub
			
		}
	}
	
	
	

	public Connection getConnection(String username, String password)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setLogWriter(PrintWriter arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setLoginTimeout(int arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

}
