package cn.carrental.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractDao{
	public int update(String sql,Object[] objs){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			//2.建立连接
			conn=JdbcUtils.getConnection();
			//3.创建语句
			ps = conn.prepareStatement(sql);
			for(int i=0;i<objs.length;i++){
				ps.setObject(i+1,objs[i]);
			}
			//4.执行语句
			return ps.executeUpdate();
		}catch(SQLException e){
			throw new RuntimeException();
		}finally{
			JdbcUtils.free(rs,ps,conn);
		}
	}
	//update支持增删改
	//查用模板设计模式
	public Object find(String sql,Object[] objs){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			//2.建立连接
			conn=JdbcUtils.getConnection();
			//3.创建语句
			ps = conn.prepareStatement(sql);
			for(int i=0;i<objs.length;i++){
				ps.setObject(i+1,objs[i]);
			}
			//4.执行语句
			rs = ps.executeQuery();
			//5.处理结果
			Object obj = null;
			while(rs.next()){
				obj = rowMapper(rs);
			}
			return obj;
		}catch(SQLException e){
			throw new RuntimeException();
		}finally{
			JdbcUtils.free(rs,ps,conn);
		}
	}
	abstract protected Object rowMapper(ResultSet rs) throws SQLException;
}
