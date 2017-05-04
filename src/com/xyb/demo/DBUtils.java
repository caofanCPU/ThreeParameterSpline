package com.xyb.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * 
 * @author CY_XYZ
 * DBUtils类：项目中数据库工具类
 * 功能：从指定配置文件读取连接数据库的必要信息
 *     连接数据库
 *     关闭连接数据库的资源
 */
public class DBUtils {
	private static String driverClass = null;
	private static String url = null;
	private static String user = null;
	private static String password = null;
	
	/**
	 * 静态代码块
	 * 从src目录下，读取配置文件，获取静态成员属性的必要字段信息
	 * 执行代码前，在DOS界面确认MySQL开启了服务：WIN + R --> net start mysql57
	 * 	                  其中，mysql57是安装MySQL软件时配置的服务名称信息，可以更改的
	 * 注意：高版本的MySQL，强制指定是否使用SSL连接，因而访问MySQL的URL字符串形式如下：
	 * 	   "jdbc:mysql://localhost:3306/数据库名?useSSL=true"
	 *     "jdbc:mysql:///数据库名?useSSL=true"
	 *     "jdbc:mysql://127.0.0.1:3306/数据库名?useSSL=true"
	 * 本例：url = "jdbc:mysql:///jdbc?useSSL=true";
	 */
	/*static {
		driverClass = "com.mysql.jdbc.Driver";
		url = "jdbc:mysql:///jdbc?useSSL=true";
		user = "root";
		password = "root";
	}*/
	static {
		//使用ResourceBundle.getBundle方法，需要将配置文件放在src目录下
		ResourceBundle rb = ResourceBundle.getBundle("dbInfo");
		driverClass = rb.getString("driverClass");
		url = rb.getString("url");
		user = rb.getString("user");
		password = rb.getString("password");
		try {
			Class.forName(driverClass);
		} catch (ClassNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();;
		}
	}
	/**
	 * 获得java与mysql的连接
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}
	
	public static void closeAll(ResultSet rs, Statement stmt, Connection conn) {
		if (null != rs) {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			//垃圾回收，快速释放资源
			rs = null;
		}
		
		if (null != stmt) {
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			stmt = null;
		}
		
		if (null != conn) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			conn = null;
		}
	}
}
