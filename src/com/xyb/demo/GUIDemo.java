package com.xyb.demo;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author CY_XYZ
 * GUIDemo�ࣺ��ĿGUI��ͼ��ϰ�࣬��������Ŀ����
 */
@SuppressWarnings("serial")
public class GUIDemo extends JPanel {
	/**
	 * ��Ա���ԣ�List<Integer>������values�����ڱ�����յ�������
	 * 		  ��̬����MAX_VALUE�����ƽ������ݵ����ֵ
	 * 		  ��̬����MAX_COUNT_OF_VALUES����������values���������
	 */
	private List<Integer> values = null; 
	private static final int MAX_VALUE = 200;
	private static final int MAX_COUNT_OF_VALUES = 50;
	
	/**
	 * GUIDemo����޲ι��캯��
	 * ���ܣ�����GUI��ʱ������һ���������ݵĽ���
	 */
	public GUIDemo() {
		values = Collections.synchronizedList(new ArrayList<Integer>());
		//ʹ�������ڲ��ഴ��һ���̣߳�ģ���������.
		new Thread(new Runnable() {
			//��дRunnable�ӿڵ�run����
			public void run() {
				//�����������rand����
				Random rand = new Random();
				try {
					while (true) {
						//����[0, MAX_VALUE)������ֵ
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
	 * GUIDemo���������main��GUIDemo��ͼ��ϰ�ĳ������
	 */
	public static void main(String[] args) {
		createGuiAndShow();
	}
	
	private static void createGuiAndShow() {
		JFrame frame = new JFrame("��̬����ͼ");
		frame.getContentPane().add(new GUIDemo());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 400);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	//��дpaintComponet����
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
	 * addValue����
	 * ���룺int����ֵvalue
	 * ���أ���
	 * ���ܣ���̬ѭ��ʹ�ý������ݵ��ڴ�ռ䣬List<Integer>��������values�Ŀռ�
	 * 	        �ռ�ռ��ʱ���Ƴ���һ��Ԫ��
	 */
	private void addValue(int value) {
		//�жϿռ��Ƿ�ռ��
		if (values.size() > MAX_COUNT_OF_VALUES) {
			//�Ƴ���һ��Ԫ��
			values.remove(0);
		}
		//���Ԫ��
		values.add(value);
	}
	
	/**
	 * 
	 * normalizeValueForYAxis����
	 * ���룺int������value��int�͸߶�ֵheight
	 * ���أ�value�Ĺ�һ��int��ֵ
	 * ���ܣ���value��y���һ����ʹ��valueλ��[0, height]֮��.
	 *
	 */
	private int normalizeValueForYAxis(int value, int height) {
		return (int) ((double) height / MAX_VALUE * value);
	}
}
