package com.xyb.cf;

/**
 * 
 * @author CY_XYZ
 * PixcelTreeMap�ࣺ��Ŀ��ͼʱ�����Ĵ洢�ṹ
 */
public class PixelTreeMap implements Comparable<PixelTreeMap> {
	private int id;
	private int x;
	private int y;
	
	public PixelTreeMap(int id, int x, int y) {
		this.id = id;
		this.x = x;
		this.y = y;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	/**
	 * 
	 * ��дcompareTo����
	 * ���룺PixelTreeMap����ptm
	 * ���ܣ�ʵ��Comparable<T t>�ӿ�
	 *     ����public int compareTo(T t)���󷽷�
	 *     ��ĳ���Ƚ�����ʵ�ֱȽ���Comparator<T t>��������Ҫ����public int compare(T t1, T t2)
	 */
	public int compareTo(PixelTreeMap ptm) {
		int temp = this.getId() - ptm.getId();
		if (0 != temp) {
		    return temp;
		} else {
			return 0;
		}
	}
}
