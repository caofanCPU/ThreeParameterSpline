package com.xyb.demo;

import java.util.Scanner;

/**
 * 
 * @author CY_XYZ
 * LoginClient�ࣺ�ͻ��ˣ���Ŀ�������
 * ���ܣ���ȡ�û���¼��Ϣ
 *     �������ݿ���û���¼����
 */
public class LoginClient {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.println("�������û�����");
		String user = input.nextLine();
		System.out.println("���������룺");
		String password = input.nextLine();
		input.close();
		LoginService ls = new LoginService();
		User u = ls.findUser(user, password);
		if (null != u) {
			System.out.println("��ӭ�㣬 " + user + "!");
		} else {
			System.out.println("�û����������������");
		}
	}
}
