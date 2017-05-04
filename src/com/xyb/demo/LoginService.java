package com.xyb.demo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

/**
 * @author CY_XYZ
 * LoginService类：服务端
 * 功能：创建客户端与数据库的连接对象
 *     根据客户端传来的登录信息，查询数据库
 */
public class LoginService {
	public User findUser(String user, String password) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		User u = null;
		try {
			//获取连接对象conn
			conn = DBUtils.getConnection();
			//获取执行SQL语句的对象stmt
			String sql ="SELECT * FROM users WHERE user=? AND password=?";
			stmt = conn.prepareStatement(sql);//得到执行sql语句的对象Statement
			//给？赋值
			stmt.setString(1, user);
			stmt.setString(2, password);
			rs = stmt.executeQuery();
			if (rs.next()) {
				u = new User();
				u.setUserName(rs.getString("user"));
				u.setPassword(rs.getString("password"));
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			DBUtils.closeAll(rs, stmt, conn);
		}
		return u;
	}
}
