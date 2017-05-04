package com.xyb.cf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author CY_XYZ
 * FileOperation�ࣺ��Ŀ�����ļ�����
 * ���ܣ���ȡ�����ļ�
 *     д�������ļ�
 */
public class FileOperation {
	
	public FileOperation() {
		
	}
	
	/**
	 * readFile����
	 * ���룺String���ļ�������fileName
	 * ���أ������ļ����ݵ�StringBuilder��������sb
	 * ���ܣ�ָ��ԭʼ�����ļ�·�������жϸ��ļ�������ڣ������׳�RuntimeEXception�쳣
	 * 	        ��ȡ�ı��ļ����ݣ�������StringBuilder��������sb��
	 * �쳣��ԭʼ�����ļ������ڣ������ƶ�ԭʼ����·������
	 *     BufferedReader�������FileReader����ʧ��
	 *     BufferedReader����ر���Դʧ��
	 */
	public StringBuilder readFile(String fileName) {
		File dataFile = new File(System.getProperty("user.dir"), fileName);
		if (!dataFile.exists()) {
			throw new RuntimeException("ԭʼ�����ļ�·��������ļ������ڣ�");
		}
		BufferedReader bufr = null;
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			bufr = new BufferedReader(new FileReader(dataFile));
			while (null != (line = bufr.readLine())) {
				sb.append(line);
			}
		}
		catch (IOException ioe) {
			sop(ioe.toString() + "\n��ȡ�ļ�ʧ�ܣ�");
		}
		finally {
			try {
				if (null != bufr) {
					bufr.close();
				}
			}
			catch (IOException ioe) {
				sop(ioe.toString() + "\n��ȡ�ļ��ر�ʧ�ܣ�");
			}
		}
		return sb;
	}
	
	/**
	 * 
	 * writeResult2File����
	 * ���룺StringBuilder��������sb��String�ļ�������fileName
	 * �������sb�е��ַ�������д��ָ���ļ�
	 * ���ܣ������������β���������������ʽ��ʽ������ָ���ļ���
	 * 	        �ļ�Ĭ�����û���ǰ����Ŀ¼��
	 * 	        �ж��ļ��������򴴽��ļ����ļ�������ʹ�ø��ǵķ�ʽ�����ļ�����
	 *     �����쳣��try=catch-finally���������һ�������쳣���׳�RuntimeException�쳣
	 * �쳣�����ܳ����ļ�����ʧ�ܣ�
	 * 	   BufferedWriter����FileWriter����ʧ��
	 * 	   BufferedWriter.write����д���ļ�ʧ��
	 * 	   BufferedWriter.close�����ر��ļ�ʧ��
	 */
	public void writeResult2File(StringBuilder sb, String fileName) {
		//��ȡ�û���ǰ����Ŀ¼
		String currentDir = System.getProperty("user.dir");
		File resultFile = new File(currentDir, fileName);
		if (!resultFile.exists()) {
			try {
				resultFile.createNewFile();
			}
			catch(IOException ioe) {
				//������
				throw new RuntimeException("Ŀ���ļ��������Ҵ���ʧ�ܣ�");
			}
		}
		BufferedWriter bufw = null;
		try {
			//�������䶼���ܲ����쳣
			bufw = new BufferedWriter(new FileWriter(resultFile));
			//д�������뻺�������ȴ�ˢ�²�����������
			bufw.write(sb.toString());
			//ˢ��д��������д������������д���ļ�
			bufw.flush();
		}
		catch(IOException ioe) {
			//������
			throw new RuntimeException("Ŀ���ļ�д��ʧ�ܣ�");
		}
		finally {
			try {
				if (null != bufw) {
					//bufw.close()ִ��ǰ��������bufw.flush()
					//��Ϊ�˲����������з���ǿ�ҽ���bufw.write();�����bufw.flush()
					bufw.close();	
				}
			}
			catch(IOException ioe) {
				//������
				throw new RuntimeException("Ŀ���ļ�д��ر�ʧ�ܣ�");
			}
		}
	}
	
	/**
	 * 
	 * sop����
	 * ���룺�������Object
	 * ������ڿ���̨���������ַ���������ʽ
	 * ���أ���
	 */
	public static void sop(Object obj) {
		System.out.println(obj);
	}
}
