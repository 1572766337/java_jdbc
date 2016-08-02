package cn.itcast.dbutils.demo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.KeyedHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

import cn.itcast.utils.JdbcUtils;

public class Demo2 {
	//测试dbutils的各个结果集处理器
	
	@Test
	public void test1() throws SQLException{
		QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
		String sql = "select * from users";
		Object result[] = (Object[]) runner.query(sql, new ArrayHandler());
		System.out.println(result[0]);
		System.out.println(result[1]);
	}
	
	
	@Test
	public void test2() throws SQLException{
		QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
		String sql = "select * from users";
		List list = (List) runner.query(sql, new ArrayListHandler());
		System.out.println(list);
	}
	
	@Test
	public void test3() throws SQLException{
		QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
		String sql = "select * from users";
		List list = (List) runner.query(sql, new ColumnListHandler1("name"));
		System.out.println(list);
	}
	
	
	@Test
	public void test4() throws SQLException{
		QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
		String sql = "select * from users";
		Map<Integer,Map<String,Object>> map = (Map) runner.query(sql, new KeyedHandler("id"));
		for(Map.Entry<Integer,Map<String,Object>> me : map.entrySet()){
			int id = me.getKey();
			for(Map.Entry<String, Object> entry : me.getValue().entrySet()){
				String name = entry.getKey();
				Object value = entry.getValue();
				System.out.println(name + "=" + value);
			}
		}
	}
	
	@Test
	public void test5() throws SQLException{
		QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
		String sql = "select count(*) from users";
		//Object result[] = (Object[]) runner.query(sql, new ArrayHandler());
		/*long totalrecord = (Long)result[0];
		int num = (int)totalrecord;
		System.out.println(num);
		int totalrecord = ((Long)result[0]).intValue();
		*/
		
		int totalrecord = ((Long)runner.query(sql, new ScalarHandler(1))).intValue();
		System.out.println(totalrecord);
	}
	
	
}


class ColumnListHandler1 implements ResultSetHandler{

	private String columnName;
	public ColumnListHandler1(String columnName){
		this.columnName = columnName;
	}
	public Object handle(ResultSet rs) throws SQLException {
		List list = new ArrayList();
		while(rs.next()){
			list.add(rs.getObject(columnName));
		}
		return list;
	}
}
