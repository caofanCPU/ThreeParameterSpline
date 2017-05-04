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
 * DataVisualization�ࣺ��Ŀ��ͼ��
 * ���ܣ��������������ݿ��ӻ�Ϊ����ͼ��
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
		this.setTitle("͹����������");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.g = this.getGraphics();
	}
	
	public void plot() {
		//coordinateStr���ݣ�X����  Y����
		String[] coordinateStr = pixelData(new FileOperation().
										   readFile("���β�����������ԭʼ����.txt"));
		if (0 != (coordinateStr.length % 2)) {
			throw new RuntimeException("");
		}
		//circleCenterStr���ݣ�cx���� cosThetaֵ sinThetaֵ
		String[] circleCenterStr = pixelData(new FileOperation().
											 readFile("ɰ��Բ�ı任��ϵ����.txt"));
		if (0 != (circleCenterStr.length % 3)) {
			throw new RuntimeException("");
		}
		pixelCoordinate(coordinateStr,circleCenterStr);
	}
	
	/**
	 * 
	 * pixelCoordinate����
	 * ���룺String[]��coordinateStr
	 */
	public void pixelCoordinate(String[] coordinateStr, String[] circleCenterStr) {
		double x;
		double y;
		double cosTheta;
		double sinTheta;
		this.coordinateNumber = coordinateStr.length / 2;
		int[][] coordinate = new int[2][this.coordinateNumber];
		for (int i = 0; i < circleCenterStr.length / 3; i += 250) {
			//����任��Բ����������
			this.cX = string2int(circleCenterStr[3 * i]);
			cosTheta = string2double(circleCenterStr[3 * i + 1]);
			sinTheta = string2double(circleCenterStr[3 * i + 2]);
			//����任������β���������������㣬������ͼ�����������ƽ����
			for (int j = 0; j < this.coordinateNumber; j++) {
				x = string2double(coordinateStr[2 * j]);
				y = string2double(coordinateStr[2 * j + 1]);
				coordinate[0][j] = double2int(x * cosTheta + y * sinTheta) + detX;
				coordinate[1][j] = double2int(y * cosTheta - x * sinTheta) + detY;
				//coordinateMap�д洢���ݣ�����������������ݣ�X���꣬Y���꣬ɰ��Բ��cX����
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
			//��дRunnable�ӿڵ�run����
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
	 * paintPicture����
	 * ���룺��ǰ��ͼ����g
	 * ���أ���
	 * ���ܣ��������β����������ߡ�ɰ��Բ
	 *     �Զ�̬��ʽ����ͬһ��ͼ��
	 * ���⣺���xList��yList��cXList��cYList��ʱ�������µȴ�����
	 * ���������
	 */
	public void paintPicture(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
							RenderingHints.VALUE_ANTIALIAS_ON);
		//���û���(����)����ɫ
		g2.setColor(Color.MAGENTA);
		
		int cY = det * pixelMagnification;
		//�����β���������������Сʮ��
		g2.setStroke(new BasicStroke(4));
		g2.drawLine(cY - 10 + detX, cY + detY, cY + 10 + detX, cY + detY);
		g2.drawLine(cY + detX, cY - 10 + detY, cY + detX, cY + 10 + detY);
		//���û���(����)�Ŀ��
		g2.setStroke(new BasicStroke(2));
		int length = this.xList.size();
		for (int i = 0; i < length - 1; ++i) {
			g2.drawLine(xList.get(i), yList.get(i),
						xList.get(i + 1), yList.get(i + 1));
		}
		xList.clear();
		yList.clear();
		g2.setColor(Color.BLUE);
		//��ɰ��Բ����Сʮ��
		g2.setStroke(new BasicStroke(4));
		g2.drawLine(this.cX - 10 + detX ,cY + detY, 
					this.cX + 10 + detX, cY + detY);
		g2.drawLine(this.cX + detX, cY - 10 + detY,
					this.cX + detX, cY + 10 + detY);
		//��ɰ��Բ
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
	 * getSplineData����
	 * ���룺��
	 * ���أ���
	 * ���ܣ���coordinateMap�洢�����л�ȡ���β���������������ֵ
	 *     �ֱ����xList��yList������
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
	 * getCircleData����
	 * ���룺��
	 * ���أ���
	 * ���ܣ�ͨ��Բ�ļ��뾶����ȡԲ�����������
	 *     �ֱ����cXList��cYList������
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
	 * ����paint����
	 * ���룺��ǰ��ͼ����g��int[][]�����������Ķ�ά���꣬int��Բ�ĺ�����
	 * ���أ���
	 * ���ܣ������ڻ�ͼ���棬���β����������ߵ�����ֵ��Բ������ֵ
	 *     һ���Ի���һ��ͼ��
	 * ˵�����÷���������paintPicture����ȡ��
	 */
	public void paint(Graphics g, int[][] coordinate, int circleCenter) {
		g.clearRect(0, 0, 1500, 1000);
		//ƽ����
		int detX = -200;
		int detY = -100;
		//��������ָ���������
		Graphics2D g2 = (Graphics2D)g;
		//���û���(����)����ɫ
		g2.setColor(Color.MAGENTA);
		//���û���(����)�Ŀ��
		g2.setStroke(new BasicStroke(2));
		for (int i = 0; i < coordinate[0].length - 1; i++) {
			g2.drawLine(coordinate[0][i] + detX ,coordinate[1][i] + detY,
					    coordinate[0][i+1] + detX, coordinate[1][i+1] + detY);
		}
		int cY = det * pixelMagnification;
		//�����β���������������Сʮ��
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
		//��ɰ��Բ����Сʮ��
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
	 * pixelData����
	 * ���룺StringBuilder����������sb
	 * ���أ�String[]������line
	 * ���ܣ����ı���ȡ��Ч��������
	 */
	public String[] pixelData(StringBuilder sb) {
		String[] line = sb.toString().
						 replaceAll("[^-\\d,.]", "").
						 split(",");
		return line;
	}
	
	/**
	 * 
	 * double2int����
	 * ���룺double��data
	 * ���أ�int����ֵ����pixel
	 * ���ܣ���double����ֵ�ı��ַ�����ת������ʾ������int����ֵ
	 */
	public int double2int(double data) {
		//��ƽ�ƣ��ٷŴ�
		int pixel = (int)((data + det) * pixelMagnification);;
		return pixel;
	}
	
	/**
	 * 
	 * string2int����
	 * ���룺String�Ͷ���StringData��StringData������Ϊdouble����ֵ����
	 * ���أ�int����ֵ����pixel
	 * ���ܣ���double����ֵ�ı��ַ�����ת������ʾ������int����ֵ
	 */
	public int string2int(String StringData) {
		return double2int(string2double(StringData));
	}
	
	/**
	 * 
	 * string2double����
	 * ���룺String�Ͷ���StringData��StringData������Ϊdouble����ֵ����
	 * ���أ�double����ֵ����
	 * ���ܣ���double����ֵ�ı��ַ�����ת����double����ֵ
	 */
	public double string2double(String StringData) {
		return Double.parseDouble(StringData);
	}
}
