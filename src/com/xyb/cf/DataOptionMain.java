package com.xyb.cf;

import org.ujmp.core.Matrix;
import org.ujmp.core.calculation.Calculation.Ret;

/**
 * @author CY_XYZ
 * DataOptionMain类
 * DataOptionMain类：项目入口模块
 * 功能：读取原始数据文件
 * 	        创建对象进行求解，获取处理结果
 *     保存处理结果
 */
public class DataOptionMain {

	public static void main(String[] args) {
		//基础数据的放大倍数magnification，砂轮圆半径circleRadius
		
		int magnification = 1;
		double circleRadius = magnification;
		//调用本类getData方法，通过自定义数据后自动进行处理
		getData(magnification);
		//调本类getDataByFile方法，通过读取文件内容进行自动处理
		//getDataByFile("型值点坐标.txt");
		new PointData("1.0000", circleRadius).
		getParaData("三次参数样条函数参数表.txt", "累加弦长表.txt");
		
		new DataVisualization();
	}
	
	/**
	 * getDataByFile方法
	 * 输入：String型文件名对象，fileName
	 * 输出：存有标准化数据的'标准化型值点.txt'文件
	 * 返回：无
	 * 功能：指定原始数据文件路径，并判断该文件必须存在，否则抛出RuntimeEXception异常
	 *     创建DataCleaning匿名对象
	 *     调用对象的方法读取原始数据文件内容，进行正则匹配数据清洗
	 *     将数据清洗结果保存在Matrix型矩阵coordMatrix中，并在控制台输出
	 * 异常：原始数据文件不存在，或者制定原始数据路径错误
	 */
	public static void getDataByFile(String fileName) {
		StringBuilder sb = new FileOperation().readFile(fileName);
		double[][] coordinateArray = new DataCleaning().dataRegx(sb);
		Matrix coordMatrix = Matrix.Factory.importFromArray(coordinateArray);
		sop(coordMatrix);
		saveStandardCoordinate(coordinateArray);
	}
	
	/**
	 * 
	 * getData方法
	 * 输入：无
	 * 返回：无
	 * 功能：定义(读取)型值点二维坐标，保存在double[][]型二维数组，coodinateArray
	 * 	        定义坐标数据比例放大倍数int型，magnification
	 * 	        调用本类calculation方法
	 */
	public static void getData(int magnification) {		
		double[][] coordinateArray = { { 0,-0.06,-0.10,-0.15,-0.30,
										 -0.43,-0.48,-0.48,-0.45,-0.4,
										 -0.3,-0.15,0,0.17,0.31,
										 0.49,0.64,0.83,0.94,1.01,
										 1.03,1.02,0.98,0.92,0.86,
										 0.75,0.64,0.50,0.39,0.28,
										 0.17,0.12,0.05,0 },
									   { 0.46,0.44,0.43,0.42,0.35,
										 0.25,0.12,-0.06,-0.22,-0.31,
										 -0.39,-0.44,-0.47,-0.485,-0.48,
										 -0.45,-0.41,-0.33,-0.24,-0.14,
										 -0.05,0.07,0.17,0.25,0.3,
										 0.35,0.40,0.44,0.47,0.48,
										 0.48,0.47,0.465,0.46 } };
		Matrix coordinateMatrix = Matrix.Factory.
										 importFromArray(coordinateArray).
										 times(magnification);
		coordinateArray = coordinateMatrix.toDoubleArray();
		saveStandardCoordinate(coordinateArray);
		calculation(coordinateArray);
	}
	
	public static void saveStandardCoordinate(double[][] coordinateArray) {
		//原始数据文件数据清理后，保存为'标准化型值点.txt'文件
		//String.format("%6.2f",data)，格式化数据的字符串形式：右对齐、共6位、2位小数，f浮点型
		StringBuilder sB = new StringBuilder();
		for (int i = 0; i < coordinateArray[0].length; i++) {
			sB.append("X" + String.format("%8.2f", coordinateArray[0][i]) + ", Y"
					  + String.format("%8.2f", coordinateArray[1][i]) + ",\r\n");
		}
		String fileName = "标准化型值点.txt";
		new FileOperation().writeResult2File(sB, fileName);
	}
	
	
	/**
	 * 
	 * calculation方法
	 * 输入：double[][]型二维数组，coodinateArray，int型变量，magnification
	 * 返回：无
	 * 功能：传递magnification创建ParameterSolution对象
	 * 	        传递coodinateArray计算弦长
	 * 	        调用ParameterSolution对象的方法，构建并求解矩阵方程AX = B 
	 */
	public static void calculation(double[][] coordinateArray) {
		ParameterSolution ps = new ParameterSolution();
		ps.getChrodLength(coordinateArray);
		//获取常数矩阵mXB、mYB
		Matrix mXB = ps.getMatrixB(ps.getParaX());
		Matrix mYB = ps.getMatrixB(ps.getParaY());
		
		//获取系数矩阵mXB、mYB
		Matrix mXA = ps.getMatrixA(ps.getChrodLength());
		Matrix mYA = mXA;
		
		//获取x轴未知参数mX，获取y轴未知参数mY
		Matrix mX = ps.NoSinglarMartixSolve(mXA, mXB);
		Matrix mY = ps.NoSinglarMartixSolve(mYA, mYB);
		//mX.showGUI();
		//输出求解结果
		ps.solutionResult(mX, mY);


		//上述注释代码可使用下述函数嵌套调用等价完成
		/*ps.solutionResult(ps.NoSinglarMartixSolve(ps.getMatrixA(ps.getChrodLength()),
												  ps.getMatrixB(ps.getParaX())),
						  ps.NoSinglarMartixSolve(ps.getMatrixA(ps.getChrodLength()),
								  				  ps.getMatrixB(ps.getParaY())));*/
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
	
	/**
	 * lineSplit方法
	 * 输入：无
	 * 输出：在控制台输出----------------------------分隔符
	 * 返回：无
	 */
	public static void lineSplit() {
		
		sop("----------------------------");
	}
	
	/**
	 * UJMPPractice方法
	 * 用于练习，UJMP中： 1.通过数组创建矩阵
	 * 			     2.矩阵的加法、减法、数乘、矩阵乘法
	 * 				 3.矩阵的重塑形
	 * 				 4.线性方程组的求解
	 * 				 5.获取矩阵的规模(行数, 列数)
	 * 				 6.通过行列获取矩阵元素、通过行列设置矩阵元素(注意矩阵索引从(0,0)开始)
	 */
	public static void UJMPPractice() {
		
		/**		
		 * Java矩阵元素乘法.times()
		 * 		矩阵乘法.mtimes()
		 * 		矩阵重塑性.reshape(Ret.NEW, 新行数, 新列数);
		 * 		矩阵行对齐合并(水平添加).appendHorizontally(Ret.NEW, 矩阵)
		 * 		矩阵列对齐合并(垂直添加).appendVertically(Ret.NEW, 矩阵)
		 */
		//创建3行3列的单位矩阵mXA
		Matrix mXA = Matrix.Factory.ones(3, 3);
		//矩阵mXA的所有元素同乘以8，矩阵数乘
		Matrix mX = mXA.times(8);
		//矩阵mXA进行重塑形操作，此操作并不改变矩阵mXA，参数Ret.NEW为操作方式
		Matrix mXB = mXA.reshape(Ret.NEW, 1, 9);
		//打印矩阵mX的所有元素
		sop(mX);
		//打印分割线
		lineSplit();
		//打印矩阵mXB
		sop(mXB);
		//将引用mXB指向矩阵mXA
		mXB = mXA;
		//矩阵mXA与矩阵mXB，垂直合并(列对齐合并)
		Matrix mXC = mXA.appendVertically(Ret.NEW, mXB);
		//矩阵mXA与矩阵mXB，水平合并(行对齐合并)
		Matrix mXD = mXA.appendHorizontally(Ret.NEW, mXB);
		//打印矩阵mXC
		sop(mXC);
		//打印矩阵mXD
		sop(mXD);
		//打印矩阵mXA的列数，即矩阵mXA列的维度
		sop(mXA.getColumnCount());
		//打印矩阵mXA的行数，即矩阵mXA行的维度
		sop(mXA.getRowCount());
		//创建3行1列的单位矩阵mb
		Matrix mb = Matrix.Factory.ones(3, 1);
		//对于矩阵方程(系数矩阵mXA)x = (常数矩阵mb)，使用mXA.solve(mb)求解x
		//注意：使用.solve()方法前提：系数矩阵mXA非奇异(行列式值！=0)
		Matrix x = mXA.solve(mb);
		//矩阵mXA乘以矩阵x，矩阵乘法，注意矩阵乘法的行列对应规则
		Matrix Rb = mXA.mtimes(x);
		//打印矩阵mXA的所有元素
		sop(mXA);
		//打印分割线
		lineSplit();
		//打印矩阵mb的所有元素
		sop(mb);
		//打印分割线
		lineSplit();
		//打印矩阵x的所有元素
		sop(x);
		//打印分割线
		lineSplit();
		//打印矩阵mXA的所有元素
		sop(mXA);
		//打印分割线
		lineSplit();
		//打印矩阵Rb的所有元素
		sop(Rb);
	}
}