package com.xyb.demo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

/**
 * @author CY_XYZ
 * LoginService�ࣺ�����
 * ���ܣ������ͻ��������ݿ�����Ӷ���
 *     ���ݿͻ��˴����ĵ�¼��Ϣ����ѯ���ݿ�
 */
public class LoginService {
	public User findUser(String user, String password) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		User u = null;
		try {
			//��ȡ���Ӷ���conn
			conn = DBUtils.getConnection();
			//��ȡִ��SQL���Ķ���stmt
			String sql ="SELECT * FROM users WHERE user=? AND password=?";
			stmt = conn.prepareStatement(sql);//�õ�ִ��sql���Ķ���Statement
			//������ֵ
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
