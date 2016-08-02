package cn.itcast.dbutils.demo;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.junit.Test;

import cn.itcast.domain.User;
import cn.itcast.utils.JdbcUtils;

public class Demo1 {

	/*
	 create database day17;
	 use day17;
	 create table users(
		id int primary key,
		name varchar(40),
		password varchar(40),
		email varchar(60),
		birthday date
	);
	 */
	
	//使用dbutils完成数据库的crud
	
	@Test
	public void insert() throws SQLException{
		QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
		String sql = "insert into users(id,name,password,email,birthday) values(?,?,?,?,?)";
		Object params[] = {2,"bbb","123","aa@sina.com",new Date()};
		runner.update(sql, params);
	}
	
	@Test
	public void update() throws SQLException{
		QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
		String sql = "update users set email=? where id=?";
		Object params[] = {"aaaaaa@sina.com",1};
		runner.update(sql, params);
	}
	
	@Test
	public void delete() throws SQLException{
		QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
		String sql = "delete from users where id=?";
		runner.update(sql, 1);
	}
	
	@Test
	public void find() throws SQLException{
		QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
		String sql = "select * from users where id=?";
		User user = (User) runner.query(sql, 1, new BeanHandler(User.class));
		System.out.println(user.getEmail());
	}
	
	
	@Test
	public void getAll() throws Exception{
		QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
		String sql = "select * from users";
		List list = (List) runner.query(sql, new BeanListHandler(User.class));
		System.out.println(list);
	}
	
	@Test
	public void batch() throws SQLException{
		QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
		String sql =  "insert into users(id,name,password,email,birthday) values(?,?,?,?,?)";
		Object params[][] = new Object[3][5];
		for(int i=0;i<params.length;i++){  //3
			params[i] = new Object[]{i+1,"aa"+i,"123",i + "@sina.com",new Date()};
		}
		runner.batch(sql, params);
	}
}
