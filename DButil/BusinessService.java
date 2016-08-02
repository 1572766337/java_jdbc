package cn.itcast.service;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import cn.itcast.dbutils.demo.AccountDao;
import cn.itcast.domain.Account;
import cn.itcast.utils.JdbcUtils;

public class BusinessService {
	
	/*
	
	
	create table account(
		id int primary key auto_increment,
		name varchar(40),
		money float
	)character set utf8 collate utf8_general_ci;
	
	insert into account(name,money) values('aaa',1000);
	insert into account(name,money) values('bbb',1000);
	insert into account(name,money) values('ccc',1000); 
	*/
	
	@Test
	public void test() throws SQLException{
		transfer2(1, 2, 100);
	}
	
	
	public void transfer1(int sourceid,int targetid,double money) throws SQLException{
		
		Connection conn = null;
		try{
			conn = JdbcUtils.getConnection();
			conn.setAutoCommit(false);
			
			AccountDao dao = new AccountDao(conn);
			
			Account a = dao.find(sourceid);   //select
			Account b = dao.find(targetid);   //select
			
			a.setMoney(a.getMoney()-money);  
			b.setMoney(b.getMoney()+money);   
			
			dao.update(a); //update
			
			dao.update(b);//update
			
			conn.commit();
		}finally{
			if(conn!=null) conn.close();
		}
	}
	
	
	//用上ThreadLocal的事务管理
	public void transfer2(int sourceid,int targetid,double money) throws SQLException{
		
		try{
			JdbcUtils.startTransaction();
			AccountDao dao = new AccountDao();
			Account a = dao.find(sourceid);   //select
			Account b = dao.find(targetid);   //select
			a.setMoney(a.getMoney()-money);  
			b.setMoney(b.getMoney()+money);   
			dao.update(a); //update
			dao.update(b);//update
			JdbcUtils.commitTransaction();
		}finally{
			JdbcUtils.closeConnection();
		}
	}
}
