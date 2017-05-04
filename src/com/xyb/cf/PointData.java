package com.xyb.cf;

/**
 * @author CY_XYZ
 * PointData类：项目绘图坐标数据计算
 * 功能：计算三次参数样条函数曲线的坐标点数据(X, Y)
 * 	        计算
 */
public class PointData {
	/**
	 * PointData类成员属性
	 * double型数值precision：代表三次参数样条曲线的插值间隔精度，默认0.005
	 * int型precisionNumber：代表三次参数样条曲线坐标数据的小数位数，默认小数点后4位
	 * double型circleRadius：代表砂轮圆的半径值，默认为1.0
	 */
	private double precision = 0.005;
	private int precisionNumber = 4;
	private double circleRadius = 1;
	/**
	 * PointData类的带参构造方法
	 * 输入：String型数值字符串precisionStr
	 * 返回：无
	 * 功能：调用本类calculatePrecision方法
	 *     获取数值字符串precisionStr所表示的数值精度
	 */
	public PointData(String precisionStr, double circleRadius) {
		this.precision = calculatePrecision(precisionStr);
		this.circleRadius = circleRadius;
	}
	
	/**
	 * 
	 * PointData类成员属性的set、get方法
	 */
	public double getPrecision() {
		return this.precision;
	}
	public void setPrecision(double precision) {
		this.precision = precision;
	}
	public int getPrecisionNumber() {
		return this.precisionNumber;
	}
	public void setPrecisionNumber(int precisionNumber) {
		this.precisionNumber = precisionNumber;
	}
	public double getCircleRadius() {
		return this.circleRadius;
	}
	public void setCircleRadius(int circleRadius) {
		this.circleRadius = circleRadius;
	}

	/**
	 * calculatePrecision方法
	 * 输入：String型数值字符串precisionStr
	 * 返回：经过处理后的double型数值precision
	 * 功能：获取输入数值字符串precisionStr表示的数值精度
	 *     将输入数值字符串转化成"0.0...1"的数值形式
	 * 备注：结合实际问题背景及性能，暂时限定精度为0.0001
	 */
	public double calculatePrecision(String precisionStr) {
		//获取小数位数：先找出"."的位置index，字符串的长度.length() - index - 1
		int precisionNumber = precisionStr.length() - precisionStr.indexOf('.') - 1;
		//处于精度考虑，限定插值精度最小为0.005
		if (4 != precisionNumber) {
			return 0.005;
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append("0.");
			for (int i = 0; i < precisionNumber - 1; i ++) {
				sb.append("0");
			}
			sb.append("5");
			double precision = Double.parseDouble(sb.toString());
			return precision;
		}
	}

	/**
	 * 
	 * getParaData方法
	 * 输入：String型对象paraFileName，三次参数样条函数【参数数据】文件名
	 *     String型对象summationFileName，三次参数样条函数【累加弦长数据】文件名
	 * 返回：无
	 * 功能：获取三次参数样条函数参数数据，保存在String[]型数组paraArray中
	 *     获取型值点累加弦长列表数据，保存在String[]型数组summationArray中
	 *     调用本类calculateCoordinate方法计算三次参数曲线坐标点
	 */
	public void getParaData(String paraFileName, String summationFileName) {
		StringBuilder sb = new FileOperation().readFile(paraFileName);
		String line = sb.toString();
		String[] paraArray = line.replaceAll("-*?第\\d{1,2}段:", "").
								  replaceAll("[^\\d-,\\.]", "").
								  split(",");
		//对获得的参数数据进行缺失检查
		if (0 != (paraArray.length % 8)) {
			throw new RuntimeException("文件\"" + paraFileName + "\"数据存在缺失，参数未对应！");
		}
		//获取累加弦长数据borderArray，用于三次参数样条函数解析式的分段标记
		String[] borderArray = new FileOperation().readFile("累加弦长表.txt").
				  								   toString().
				  								   split(",");
		//调用本类getCoordinate方法
		getCoordinate(paraArray, borderArray);
		//System.out.println(paraArray.length/8 + "\n" + borderArray.length);
	}
	
	/**
	 * 
	 * getCoordinate方法
	 * 输入：String[]型数组paraArray，三次参数样条函数的参数值
	 *     String[]型数组borderArray，累积弦长值作为三次参数样条函数的分段边界
	 * 输出：三次参数样条曲线坐标文件，"三次参数样条曲线原始坐标.txt"
	 *     砂轮圆心坐标文件，"砂轮圆心原始坐标.txt"
	 */
	public void getCoordinate(String[] paraArray, String[] borderArray) {
		
		//弦长左边界leftLimit，弦长右边界rightLimit
		double leftLimit;
		double rightLimit;
		double[] para = new double[8];
		StringBuilder sb = new StringBuilder();
		StringBuilder sB = new StringBuilder();
		StringBuilder[] sbArray = null;
		int segments = paraArray.length / 8;
		for (int i = 0; i < segments; i++) {
			//更新弦长边界区域，即【逐段】计算三次参数样条曲线
			leftLimit = string2double(borderArray[i]);
			rightLimit = string2double(borderArray[i+1]);
			//获取三次样条函数曲线在该段的参数
			for (int j = 0; j < 8; j++) {
				para[j] = string2double(paraArray[i * 8 + j]);
			}
			sbArray = calculateCoordinate(para, leftLimit, rightLimit);
			sb.append(sbArray[0].toString());
			sB.append(sbArray[1].toString());
		}
		new FileOperation().writeResult2File(sb, "三次参数样条曲线原始坐标.txt");
		new FileOperation().writeResult2File(sB, "砂轮圆心变换关系坐标.txt");
		//清空StringBuilder容器
		sb.delete(0, sb.length());
		sB.delete(0, sB.length());
	}
	
	/**
	 * 
	 * @param leftLimit
	 * @param rightLimit
	 * @return
	 */
	public StringBuilder[] calculateCoordinate(double[] para, 
											   double leftLimit,
											   double rightLimit) {
		int detNumber = (int)((rightLimit - leftLimit) / this.getPrecision());
		double det = this.getPrecision();
		//System.out.println("---------------------------------------------");
		double x;
		double y;
		double dx;
		double dy;
		double ds;
		double dX;
		double dY;
		double dS;
		double circleRadius = this.getCircleRadius();
		double cosTheta;
		double sinTheta;
		//StringBuilder容器sb存储三次样条曲线坐标点格式化数据(x, y)
		StringBuilder sb = new StringBuilder();
		//StringBuilder容器sB存储砂轮圆心坐标，变换关系格式化数据(ds, 0, cosTheta, sinTheta)
		StringBuilder sB = new StringBuilder();
		for (int i = 0; i < detNumber; i++) {
			//d相当于解析式中自变量L
			double d = string2double(String.format("%6.4f", (i * det)));
			double dd = d * d;
			double ddd = d * d * d;
			//切点坐标(x, y)
			x = para[0] * ddd + para[1] * dd + para[2] * d + para[3];
			y = para[4] * ddd + para[5] * dd + para[6] * d + para[7];
			sb.append("X" + String.format("%12.8f", x) + ", "
					  + "Y" + String.format("%12.8f", y) + ",\r\n");
			//切点处切向量(导数)(dx, dy)-->坐标变换前砂轮圆心坐标(dx, dy)
			//避免使用同一个变量代表多个值，且这些值参与运算，容易混淆，浪费时间测试！！！
			dx = 3 * para[0] * dd + 2 * para[1] * d + para[2];
			dy = 3 * para[4] * dd + 2 * para[5] * d + para[6];
			ds = Math.sqrt(dx * dx + dy * dy);
			dX = x + circleRadius * dy / ds;
			dY = y - circleRadius * dx / ds;
			dS = Math.sqrt(dX * dX + dY * dY);
			cosTheta = dX / dS;
			sinTheta = dY / dS;
			/**
			 * 切点坐标变为(dX, dY)，砂轮圆心坐标(ds, 0)，变换关系参数(cosTheta, sinTheta)
			 * dX = dx * cosTheta + dy * sinTheta;
			 * dY = dy * cosTheta - dx * sinTheta;
			 */
			sB.append("X" + String.format("%12.8f", dS) + ", "
					  + "cosTheta" + String.format("%12.8f", cosTheta) + ", "
					  + "sinTheta" + String.format("%12.8f", sinTheta) + ",\r\n");
		}
		//用于存储三次参数样条曲线坐标点的StringBuilder容器，  sbArray[0]存储X坐标, Y坐标
		//										  sbArray[1]存储该点对应的切向量X坐标, Y坐标
		StringBuilder[] sbArray = { sb, sB };
		return sbArray;
	}	
	
	/**
	 * 
	 * string2double方法
	 * 输入：String型对象StringData，StringData的内容为double型数值数据
	 * 返回：double型数值类型data
	 * 功能：将double型数值文本字符串，转换成对应的double型数值
	 */
	public double string2double(String StringData) {
		double data = Double.parseDouble(StringData.trim());
		return data;
	}
}
