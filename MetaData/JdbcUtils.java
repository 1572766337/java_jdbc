package cn.itcast.utils;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;

public class JdbcUtils {
	
	private static DataSource ds = null;
	static{
		try{
			InputStream in = JdbcUtils.class.getClassLoader().getResourceAsStream("dbcpconfig.properties");
			Properties prop = new Properties();
			prop.load(in);
			BasicDataSourceFactory factory = new BasicDataSourceFactory();
			ds = factory.createDataSource(prop);
		}catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	public static Connection getConnection() throws SQLException{
		return ds.getConnection();
	}
	
	
	public static void release(Connection conn,Statement st,ResultSet rs){
		
		if(rs!=null){
			try{
				rs.close();   //throw new 
			}catch (Exception e) {
				e.printStackTrace();
			}
			rs = null;
		}
		if(st!=null){
			try{
				st.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
			st = null;
		}
		if(conn!=null){
			try{
				conn.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//String sql = "insert into account(id,name,money) values(?,?,?)"   object[]{1,"aaa","10000"};
	public static void update(String sql,Object params[]) throws SQLException{
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try{
			conn = getConnection();
			st = conn.prepareStatement(sql);
			for(int i=0;i<params.length;i++){
				st.setObject(i+1,params[i]);
			}
			st.executeUpdate();
		}finally{
			release(conn, st, rs);
		}
	}
	
	//
	public static Object query(String sql,Object params[],ResultSetHandler handler) throws SQLException{
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try{
			conn = getConnection();
			st = conn.prepareStatement(sql);
			for(int i=0;i<params.length;i++){
				st.setObject(i+1,params[i]);
			}
			rs = st.executeQuery();
			return handler.handler(rs);
		}finally{
			release(conn, st, rs);
		}
	}
}	

interface ResultSetHandler{
	public Object handler(ResultSet rs);
}


class BeanHandler implements ResultSetHandler{

	private Class clazz;
	public BeanHandler(Class clazz){
		this.clazz = clazz;
	}
	
	public Object handler(ResultSet rs) {
		try{
			if(!rs.next()){
				return null;
			}
			
			//创建封装结果集的bean
			Object bean = clazz.newInstance();
			
			//得到结果集的元数据，以获取结果集的信息
			ResultSetMetaData meta = rs.getMetaData();
			int count = meta.getColumnCount();
			for(int i=0;i<count;i++){
				String name = meta.getColumnName(i+1);   //获取到结果集每列的列名  id
				Object value = rs.getObject(name);     //1
				
				//反射出bean上与列名相应的属性
				Field f = bean.getClass().getDeclaredField(name);
				f.setAccessible(true);
				f.set(bean, value);
			}
		
			return bean;
		
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

class BeanListHandler implements ResultSetHandler{

	private Class clazz;
	public BeanListHandler(Class clazz){
		this.clazz = clazz;
	}
	public Object handler(ResultSet rs) {
		List list = new ArrayList();
		try{
			while(rs.next()){
				Object bean = clazz.newInstance();
				ResultSetMetaData  meta = rs.getMetaData();
				int count = meta.getColumnCount();
				for(int i=0;i<count;i++){
					String name = meta.getColumnName(i+1);
					Object value = rs.getObject(name);
					
					Field f = bean.getClass().getDeclaredField(name);
					f.setAccessible(true);
					f.set(bean, value);
				}
				list.add(bean);
			}
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
		return list;
		
	}
	
	
	
}

