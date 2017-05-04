package com.xyb.cf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author CY_XYZ
 * FileOperation类：项目数据文件操作
 * 功能：读取数据文件
 *     写入数据文件
 */
public class FileOperation {
	
	public FileOperation() {
		
	}
	
	/**
	 * readFile方法
	 * 输入：String型文件名对象，fileName
	 * 返回：包含文件内容的StringBuilder容器对象sb
	 * 功能：指定原始数据文件路径，并判断该文件必须存在，否则抛出RuntimeEXception异常
	 * 	        读取文本文件内容，保存在StringBuilder容器对象sb中
	 * 异常：原始数据文件不存在，或者制定原始数据路径错误
	 *     BufferedReader对象关联FileReader对象失败
	 *     BufferedReader对象关闭资源失败
	 */
	public StringBuilder readFile(String fileName) {
		File dataFile = new File(System.getProperty("user.dir"), fileName);
		if (!dataFile.exists()) {
			throw new RuntimeException("原始数据文件路径错误或文件不存在！");
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
			sop(ioe.toString() + "\n读取文件失败！");
		}
		finally {
			try {
				if (null != bufr) {
					bufr.close();
				}
			}
			catch (IOException ioe) {
				sop(ioe.toString() + "\n读取文件关闭失败！");
			}
		}
		return sb;
	}
	
	/**
	 * 
	 * writeResult2File方法
	 * 输入：StringBuilder容器对象sb，String文件名对象fileName
	 * 输出：将sb中的字符串内容写入指定文件
	 * 功能：将完整的三次参数样条函数解析式格式化存入指定文件中
	 * 	        文件默认在用户当前工作目录下
	 * 	        判断文件不存在则创建文件，文件存在则使用覆盖的方式更新文件内容
	 *     进行异常的try=catch-finally处理操作，一旦捕获异常，抛出RuntimeException异常
	 * 异常：可能出现文件创建失败，
	 * 	   BufferedWriter关联FileWriter对象失败
	 * 	   BufferedWriter.write方法写入文件失败
	 * 	   BufferedWriter.close方法关闭文件失败
	 */
	public void writeResult2File(StringBuilder sb, String fileName) {
		//获取用户当前工作目录
		String currentDir = System.getProperty("user.dir");
		File resultFile = new File(currentDir, fileName);
		if (!resultFile.exists()) {
			try {
				resultFile.createNewFile();
			}
			catch(IOException ioe) {
				//待处理
				throw new RuntimeException("目标文件不存在且创建失败！");
			}
		}
		BufferedWriter bufw = null;
		try {
			//下面两句都可能产生异常
			bufw = new BufferedWriter(new FileWriter(resultFile));
			//写入流进入缓冲区，等待刷新操作出缓冲区
			bufw.write(sb.toString());
			//刷新写缓冲流，写缓冲流才真正写入文件
			bufw.flush();
		}
		catch(IOException ioe) {
			//待处理
			throw new RuntimeException("目标文件写入失败！");
		}
		finally {
			try {
				if (null != bufw) {
					//bufw.close()执行前会限制性bufw.flush()
					//当为了不再网络编程中犯错，强烈建议bufw.write();后紧跟bufw.flush()
					bufw.close();	
				}
			}
			catch(IOException ioe) {
				//待处理
				throw new RuntimeException("目标文件写入关闭失败！");
			}
		}
	}
	
	/**
	 * 
	 * sop方法
	 * 输入：任意对象Object
	 * 输出：在控制台输入对象的字符串表现形式
	 * 返回：无
	 */
	public static void sop(Object obj) {
		System.out.println(obj);
	}
}
