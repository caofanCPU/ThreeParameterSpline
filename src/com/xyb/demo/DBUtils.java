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
 * DBUtils�ࣺ��Ŀ�����ݿ⹤����
 * ���ܣ���ָ�������ļ���ȡ�������ݿ�ı�Ҫ��Ϣ
 *     �������ݿ�
 *     �ر��������ݿ����Դ
 */
public class DBUtils {
	private static String driverClass = null;
	private static String url = null;
	private static String user = null;
	private static String password = null;
	
	/**
	 * ��̬�����
	 * ��srcĿ¼�£���ȡ�����ļ�����ȡ��̬��Ա���Եı�Ҫ�ֶ���Ϣ
	 * ִ�д���ǰ����DOS����ȷ��MySQL�����˷���WIN + R --> net start mysql57
	 * 	                  ���У�mysql57�ǰ�װMySQL���ʱ���õķ���������Ϣ�����Ը��ĵ�
	 * ע�⣺�߰汾��MySQL��ǿ��ָ���Ƿ�ʹ��SSL���ӣ��������MySQL��URL�ַ�����ʽ���£�
	 * 	   "jdbc:mysql://localhost:3306/���ݿ���?useSSL=true"
	 *     "jdbc:mysql:///���ݿ���?useSSL=true"
	 *     "jdbc:mysql://127.0.0.1:3306/���ݿ���?useSSL=true"
	 * ������url = "jdbc:mysql:///jdbc?useSSL=true";
	 */
	/*static {
		driverClass = "com.mysql.jdbc.Driver";
		url = "jdbc:mysql:///jdbc?useSSL=true";
		user = "root";
		password = "root";
	}*/
	static {
		//ʹ��ResourceBundle.getBundle��������Ҫ�������ļ�����srcĿ¼��
		ResourceBundle rb = ResourceBundle.getBundle("dbInfo");
		driverClass = rb.getString("driverClass");
		url = rb.getString("url");
		user = rb.getString("user");
		password = rb.getString("password");
		try {
			Class.forName(driverClass);
		} catch (ClassNotFoundException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();;
		}
	}
	/**
	 * ���java��mysql������
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
			//�������գ������ͷ���Դ
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
