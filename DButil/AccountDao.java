package cn.itcast.dbutils.demo;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import cn.itcast.domain.Account;
import cn.itcast.utils.JdbcUtils;

public class AccountDao {
	
	
	
	public AccountDao() {
		super();
		// TODO Auto-generated constructor stub
	}



	private Connection conn;
	public AccountDao(Connection conn){
		this.conn = conn;
	}
	
	
	public void update(Account a){
		try{
			QueryRunner runner = new QueryRunner();
			String sql = "update account set money=? where id=?";
			Object params[] = {a.getMoney(),a.getId()};
			runner.update(JdbcUtils.getConnection(),sql, params);
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Account find(int id){
		try{
			QueryRunner runner = new QueryRunner();
			String sql = "select * from account where id=?";
			return (Account) runner.query(JdbcUtils.getConnection(),sql, id, new BeanHandler(Account.class));
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	//从a--->b帐户转100元
	public void transfer() throws SQLException{
		Connection conn = null;
		try{
			conn = JdbcUtils.getConnection();
			conn.setAutoCommit(false);
			
			QueryRunner runner = new QueryRunner();
			String sql1 = "update account set money=money-100 where name='aaa'";
			runner.update(conn,sql1);
			
			String sql2 = "update account set money=money+100 where name='bbb'";
			runner.update(conn,sql2);
			
			conn.commit();
		}finally{
			if(conn!=null){
				conn.close();
			}
		}
	}
	
}
