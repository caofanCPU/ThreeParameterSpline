package com.xyb.cf;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JFrame;

/**
 * @author CY_XYZ
 * DataVisualization类：项目绘图类
 * 功能：把曲线坐标数据可视化为函数图形
 */

@SuppressWarnings("serial")
public class DataVisualization extends JFrame {
	private static final int START_WIDTH = 200;
	private static final int START_HEIGHT = 40;
	private static final int MAX_WIDTH = 1500;
	private static final int MAX_HEIGHT = 1000;
	private static final int pixelMagnification = 300;
	private static final int det = 2;
	private int coordinateNumber = 0;
	private static final int detX = -200;
	private static final int detY = -100;
	public int cX;
	private Map<PixelTreeMap, Integer> coordinateMap =
			Collections.synchronizedMap(new TreeMap<PixelTreeMap, Integer>());
	private List<Integer> xList = 
			Collections.synchronizedList(new ArrayList<Integer>());
	private List<Integer> yList = 
			Collections.synchronizedList(new ArrayList<Integer>());
	private List<Integer> cXList = 
			Collections.synchronizedList(new ArrayList<Integer>());
	private List<Integer> cYList = 
			Collections.synchronizedList(new ArrayList<Integer>());
	private Graphics g = null;
	public DataVisualization() {
		initFrame();
		this.plot();
	}
	public void initFrame() {
		this.setBounds(START_WIDTH, START_HEIGHT, MAX_WIDTH, MAX_HEIGHT);
		this.setVisible(true);
		Container p = this.getContentPane();
		p.setBackground(new Color(0xf5f5f5));
		this.setLayout(null);
		this.setResizable(false);
		this.setTitle("凸轮轮廓曲线");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.g = this.getGraphics();
	}
	
	public void plot() {
		//coordinateStr内容：X坐标  Y坐标
		String[] coordinateStr = pixelData(new FileOperation().
										   readFile("三次参数样条曲线原始坐标.txt"));
		if (0 != (coordinateStr.length % 2)) {
			throw new RuntimeException("");
		}
		//circleCenterStr内容：cx坐标 cosTheta值 sinTheta值
		String[] circleCenterStr = pixelData(new FileOperation().
											 readFile("砂轮圆心变换关系坐标.txt"));
		if (0 != (circleCenterStr.length % 3)) {
			throw new RuntimeException("");
		}
		pixelCoordinate(coordinateStr,circleCenterStr);
	}
	
	/**
	 * 
	 * pixelCoordinate方法
	 * 输入：String[]型coordinateStr
	 */
	public void pixelCoordinate(String[] coordinateStr, String[] circleCenterStr) {
		double x;
		double y;
		double cosTheta;
		double sinTheta;
		this.coordinateNumber = coordinateStr.length / 2;
		int[][] coordinate = new int[2][this.coordinateNumber];
		for (int i = 0; i < circleCenterStr.length / 3; i += 250) {
			//计算变换后圆心像素坐标
			this.cX = string2int(circleCenterStr[3 * i]);
			cosTheta = string2double(circleCenterStr[3 * i + 1]);
			sinTheta = string2double(circleCenterStr[3 * i + 2]);
			//计算变换后的三次参数样条曲线坐标点，包含绘图界面的坐标轴平移量
			for (int j = 0; j < this.coordinateNumber; j++) {
				x = string2double(coordinateStr[2 * j]);
				y = string2double(coordinateStr[2 * j + 1]);
				coordinate[0][j] = double2int(x * cosTheta + y * sinTheta) + detX;
				coordinate[1][j] = double2int(y * cosTheta - x * sinTheta) + detY;
				//coordinateMap中存储数据：坐标点索引排序依据，X坐标，Y坐标，砂轮圆心cX坐标
				coordinateMap.put(new PixelTreeMap(j,
												   coordinate[0][j],
												   coordinate[1][j]),
								  cX);
			}
			this.getSplineData();
			this.getCircleData();
			this.paintPicture(this.g);
			//this.paint(this.g, coordinate, cX);
		}
	}
	
/*	public void dataThread() {
		new Thread(new Runnable() {
			//复写Runnable接口的run方法
			public void run() {
				for (Iterator<Map.Entry<PixelTreeMap, Integer>> entry =
					 coordinateMap.entrySet().iterator(); entry.hasNext();) {
					Map.Entry<PixelTreeMap, Integer> me = entry.next();
					PixelTreeMap ptm = me.getKey();
					xList.add(ptm.getX());
					yList.add(ptm.getY());
				}
			}
		}).start();
	}*/
	
	/**
	 * 
	 * paintPicture方法
	 * 输入：当前画图对象g
	 * 返回：无
	 * 功能：绘制三次参数样条曲线、砂轮圆
	 *     以动态方式绘制同一幅图形
	 * 问题：清楚xList、yList、cXList、cYList耗时长，导致等待现象
	 * 解决：暂无
	 */
	public void paintPicture(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
							RenderingHints.VALUE_ANTIALIAS_ON);
		//设置画笔(线条)的颜色
		g2.setColor(Color.MAGENTA);
		
		int cY = det * pixelMagnification;
		//画三次参数样条曲线中心小十字
		g2.setStroke(new BasicStroke(4));
		g2.drawLine(cY - 10 + detX, cY + detY, cY + 10 + detX, cY + detY);
		g2.drawLine(cY + detX, cY - 10 + detY, cY + detX, cY + 10 + detY);
		//设置画笔(线条)的宽度
		g2.setStroke(new BasicStroke(2));
		int length = this.xList.size();
		for (int i = 0; i < length - 1; ++i) {
			g2.drawLine(xList.get(i), yList.get(i),
						xList.get(i + 1), yList.get(i + 1));
		}
		xList.clear();
		yList.clear();
		g2.setColor(Color.BLUE);
		//画砂轮圆中心小十字
		g2.setStroke(new BasicStroke(4));
		g2.drawLine(this.cX - 10 + detX ,cY + detY, 
					this.cX + 10 + detX, cY + detY);
		g2.drawLine(this.cX + detX, cY - 10 + detY,
					this.cX + detX, cY + 10 + detY);
		//画砂轮圆
		g2.setStroke(new BasicStroke(2));
		int cLength = this.cXList.size();
		for (int i = 0; i < cLength - 1; ++i) {
			g2.drawLine(cXList.get(i), cYList.get(i),
						cXList.get(i + 1), cYList.get(i + 1));
		}
		cXList.clear();
		cYList.clear();
/*		int cx = this.cX - pixelMagnification;
		int cy = (det - 1) * pixelMagnification;
		g2.drawOval(cx + detX,
					cy + detY,
					2 * pixelMagnification,
					2 * pixelMagnification);*/
/*		try {
			Thread.sleep(50);
		}
		catch (Exception e) {
			e.printStackTrace();
		}*/
		g.clearRect(0, 0, MAX_WIDTH, MAX_HEIGHT);
	}
	/**
	 * 
	 * getSplineData方法
	 * 输入：无
	 * 返回：无
	 * 功能：从coordinateMap存储对象中获取三次参数样条曲线坐标值
	 *     分别存入xList、yList集合中
	 */
	public void getSplineData() {
		for (Iterator<Map.Entry<PixelTreeMap, Integer>> entry =
			 coordinateMap.entrySet().iterator(); entry.hasNext();) {
			Map.Entry<PixelTreeMap, Integer> me = entry.next();
			PixelTreeMap ptm = me.getKey();
			xList.add(ptm.getX());
			yList.add(ptm.getY());
		}
		coordinateMap.clear();
	}
	
	/**
	 * 
	 * getCircleData方法
	 * 输入：无
	 * 返回：无
	 * 功能：通过圆心及半径，获取圆的坐标点数据
	 *     分别存入cXList、cYList集合中
	 */
	public void getCircleData() {
		int cx = this.cX + detX;
		int cy = det * pixelMagnification + detY;
		double theta;
		for (int i = 0; i < 36000; i++) {
			theta = Math.toRadians(i * 0.05);
			cXList.add((int)(cx + pixelMagnification * Math.cos(theta)));
			cYList.add((int)(cy + pixelMagnification * Math.sin(theta)));
		}
	}
	
	/**
	 * 重载paint方法
	 * 输入：当前画图对象g，int[][]型样条函数的二维坐标，int型圆心横坐标
	 * 返回：无
	 * 功能：计算在画图界面，三次参数样条曲线的坐标值，圆心坐标值
	 *     一次性绘制一幅图形
	 * 说明：该方法被本类paintPicture方法取缔
	 */
	public void paint(Graphics g, int[][] coordinate, int circleCenter) {
		g.clearRect(0, 0, 1500, 1000);
		//平移量
		int detX = -200;
		int detY = -100;
		//父类引用指向子类对象
		Graphics2D g2 = (Graphics2D)g;
		//设置画笔(线条)的颜色
		g2.setColor(Color.MAGENTA);
		//设置画笔(线条)的宽度
		g2.setStroke(new BasicStroke(2));
		for (int i = 0; i < coordinate[0].length - 1; i++) {
			g2.drawLine(coordinate[0][i] + detX ,coordinate[1][i] + detY,
					    coordinate[0][i+1] + detX, coordinate[1][i+1] + detY);
		}
		int cY = det * pixelMagnification;
		//画三次参数样条曲线中心小十字
		g2.setStroke(new BasicStroke(4));
		g2.drawLine(cY - 10 + detX, cY + detY, cY + 10 + detX, cY + detY);
		g2.drawLine(cY + detX, cY - 10 + detY, cY + detX, cY + 10 + detY);
		g2.setColor(Color.BLUE);
		int cx = circleCenter - pixelMagnification;
		int cy = (det - 1) * pixelMagnification;
		g2.drawOval(cx + detX,
					cy + detY,
					2 * pixelMagnification,
					2 * pixelMagnification);
		//画砂轮圆中心小十字
		g2.setStroke(new BasicStroke(4));
		g2.drawLine(circleCenter - 10 + detX ,cY + detY, 
					circleCenter + 10 + detX, cY + detY);
		g2.drawLine(circleCenter + detX, cY - 10 + detY,
					circleCenter + detX, cY + 10 + detY);
		try {
			Thread.sleep(520);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * pixelData方法
	 * 输入：StringBuilder型容器对象sb
	 * 返回：String[]型数组line
	 * 功能：对文本提取有效坐标数据
	 */
	public String[] pixelData(StringBuilder sb) {
		String[] line = sb.toString().
						 replaceAll("[^-\\d,.]", "").
						 split(",");
		return line;
	}
	
	/**
	 * 
	 * double2int方法
	 * 输入：double型data
	 * 返回：int型数值类型pixel
	 * 功能：将double型数值文本字符串，转换成显示屏像素int型数值
	 */
	public int double2int(double data) {
		//先平移，再放大
		int pixel = (int)((data + det) * pixelMagnification);;
		return pixel;
	}
	
	/**
	 * 
	 * string2int方法
	 * 输入：String型对象StringData，StringData的内容为double型数值数据
	 * 返回：int型数值类型pixel
	 * 功能：将double型数值文本字符串，转换成显示屏像素int型数值
	 */
	public int string2int(String StringData) {
		return double2int(string2double(StringData));
	}
	
	/**
	 * 
	 * string2double方法
	 * 输入：String型对象StringData，StringData的内容为double型数值数据
	 * 返回：double型数值类型
	 * 功能：将double型数值文本字符串，转换成double型数值
	 */
	public double string2double(String StringData) {
		return Double.parseDouble(StringData);
	}
}
