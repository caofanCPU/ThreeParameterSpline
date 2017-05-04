package com.xyb.demo;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author CY_XYZ
 * GUIDemo类：项目GUI绘图练习类，独立于项目运行
 */
@SuppressWarnings("serial")
public class GUIDemo extends JPanel {
	/**
	 * 成员属性：List<Integer>型容器values，用于保存接收到的数据
	 * 		  静态常量MAX_VALUE，限制接收数据的最大值
	 * 		  静态常量MAX_COUNT_OF_VALUES，限制容器values的最大容量
	 */
	private List<Integer> values = null; 
	private static final int MAX_VALUE = 200;
	private static final int MAX_COUNT_OF_VALUES = 50;
	
	/**
	 * GUIDemo类的无参构造函数
	 * 功能：创建GUI类时即启动一个接收数据的进程
	 */
	public GUIDemo() {
		values = Collections.synchronizedList(new ArrayList<Integer>());
		//使用匿名内部类创建一个线程，模拟产生数据.
		new Thread(new Runnable() {
			//复写Runnable接口的run方法
			public void run() {
				//创建随机对象rand引用
				Random rand = new Random();
				try {
					while (true) {
						//产生[0, MAX_VALUE)的整数值
						addValue(rand.nextInt(MAX_VALUE));
						repaint();
						Thread.sleep(100);
					}
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	/***
	 * 
	 * GUIDemo类的主函数main，GUIDemo绘图练习的程序入口
	 */
	public static void main(String[] args) {
		createGuiAndShow();
	}
	
	private static void createGuiAndShow() {
		JFrame frame = new JFrame("动态波形图");
		frame.getContentPane().add(new GUIDemo());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 400);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	//复写paintComponet方法
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
							RenderingHints.VALUE_ANTIALIAS_ON);
		int w = this.getWidth();
		int h = this.getHeight();
		int xDelta = w / MAX_COUNT_OF_VALUES;
		int length = values.size();
		for (int i = 0; i < length - 1; ++i) {
			g2.drawLine(xDelta * (MAX_COUNT_OF_VALUES - length + i),
						normalizeValueForYAxis(values.get(i), h),
						xDelta * (MAX_COUNT_OF_VALUES - length + i + 1),
						normalizeValueForYAxis(values.get(i + 1), h));
		}
	}

	/**
	 * 
	 * addValue方法
	 * 输入：int型数值value
	 * 返回：无
	 * 功能：动态循环使用接收数据的内存空间，List<Integer>容器对象values的空间
	 * 	        空间占满时，移除第一个元素
	 */
	private void addValue(int value) {
		//判断空间是否占满
		if (values.size() > MAX_COUNT_OF_VALUES) {
			//移除第一个元素
			values.remove(0);
		}
		//添加元素
		values.add(value);
	}
	
	/**
	 * 
	 * normalizeValueForYAxis方法
	 * 输入：int型数据value，int型高度值height
	 * 返回：value的归一化int数值
	 * 功能：将value沿y轴归一化，使得value位于[0, height]之间.
	 *
	 */
	private int normalizeValueForYAxis(int value, int height) {
		return (int) ((double) height / MAX_VALUE * value);
	}
}
