package com.xyb.cf;

/**
 * 
 * @author CY_XYZ
 * PixcelTreeMap类：项目绘图时坐标点的存储结构
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
	 * 复写compareTo方法
	 * 输入：PixelTreeMap对象ptm
	 * 功能：实现Comparable<T t>接口
	 *     覆盖public int compareTo(T t)抽象方法
	 *     与某个比较器类实现比较器Comparator<T t>的区别，需要覆盖public int compare(T t1, T t2)
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
