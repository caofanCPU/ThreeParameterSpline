package com.xyb.cf;

import java.sql.SQLException;
import org.ujmp.core.Matrix;
import org.ujmp.core.calculation.Calculation.Ret;
import com.xyb.dao.ParameterDao;
import com.xyb.domain.Parameter;

/**
 * @author CY_XYZ
 * ParameterSolution类：项目的算法实现核心模块
 * 功能：实现了三次参数样条函数的矩阵方程AX = B的构建以及求解
 *     输出求解结果并保存结果至文件
 */
public class ParameterSolution {
	/**
	 * 
	 * 成员属性：int型magnification，保存坐标数据的比例放大倍数，默认为1
	 * 		  int型number，保存二维坐标数据所围成的封闭图形的[弦长数目]，默认为0
	 * 		  double[]型数组paraX，保存坐标数据在x轴上的相邻间隔
	 * 		  double[]型数组paraX，保存坐标数据在y轴上的相邻间隔
	 * 		  double[]型数组chrodLength，保存相邻坐标点的弦长
	 * 		  double[]型数组dX，保存x轴向上参数d的求解结果
	 * 		  double[]型数组dY，保存y轴向上参数d的求解结果
	 */
	private int number = 0;
	private double[] paraX = null;
	private double[] paraY = null;
	private double[] chrodLength = null;
	private double[] dX = null;
	private double[] dY = null;

	/**
	 * 
	 * 默认构造函数
	 */
	public ParameterSolution() {
		
	}
	
	/**
	 * 
	 * 成员属性的set、get方法
	 */
	
	public int getNumber() {
		return this.number;
	}
	public void setNumber(int number) {
		this.number = number;
	}

	public double[] getParaX() {
		return paraX;
	}
	public void setParaX(double[] paraX) {
		this.paraX = paraX;
	}
	
	public double[] getParaY() {
		return this.paraY;
	}
	public void setParaY(double[] paraY) {
		this.paraY = paraY;
	}
	
	public double[] getChrodLength() {
		return this.chrodLength;
	}
	public void setChrodLength(double[] chrodLength) {
		this.chrodLength = chrodLength;
	}
	
	public double[] getdX() {
		return this.dX;
	}
	public void setdX(double[] dX) {
		this.dX = dX;
	}
	public double[] getdY() {
		return this.dY;
	}
	public void setdY(double[] dY) {
		this.dY = dY;
	}
	
	/**
	 * getChrodLength方法
	 * 输入：double[][]型二维数组，coordinateArray
	 * 返回：无
	 * 功能：判定调用者是否传入维度相同的二维数组
	 *     根据输入，计算每个数组维度的坐标间隔detX，detY
	 *            相邻坐标点之间的弦长(二维直线距离)chrodLength
	 *     根据弦长chrodLength，计算累加弦长表并保存文件，"累加弦长表.txt"
	 * 异常：当输入为不同维度的二维数组时，抛出RuntimeException
	 */
	public void getChrodLength(double[][] coordinateArray) {
		double[] chrodLength = new double[coordinateArray[0].length - 1];
		int number = chrodLength.length;
		try {
			if (number != coordinateArray[1].length - 1) {
				throw new RuntimeException("输入二维坐标数组维度不一致，请检查！");
			}
			if (number < 1) {
				throw new RuntimeException("输入二维坐标数组长度太小(不满足封闭条件)，请检查！");
			}
		}
		catch (Exception e) {
			sop(e.toString());
		}
		this.setNumber(number);
		double detX, detY;
		double[] paraX = new double[number];
		double[] paraY = new double[number];
		double[] dX = new double[number];
		double[] dY = new double[number];
		for(int i = 1; i < coordinateArray[0].length; i++) {
			dX[i-1] = coordinateArray[0][i - 1];
			dY[i-1] = coordinateArray[1][i - 1];
			detX = (coordinateArray[0][i] - coordinateArray[0][i - 1]);
			detY = (coordinateArray[1][i] - coordinateArray[1][i - 1]);
			chrodLength[i - 1] = Math.sqrt(detX * detX + detY * detY);
			paraX[i - 1] = detX;
			paraY[i - 1] = detY;
		}
		this.setdX(dX);
		this.setdY(dY);
		this.setParaX(paraX);
		this.setParaY(paraY);
		this.setChrodLength(chrodLength);
		//根据弦长ChrodLength构建累加弦长表
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%20.16f", 0.0) + ",\r\n");
		double sum = 0;
		for (int i = 0; i < chrodLength.length; i++) {
			//对于最后一个元素，形式上其后不应该出现','，但这不影响本项目
			sum += chrodLength[i];
			sb.append(String.format("%20.16f", sum) + ",\r\n");
		}
		new FileOperation().writeResult2File(sb, "累加弦长表.txt");
	}

	/**
	 * getMatrixB方法
	 * 输入：double[]型一维数组，paraS(常数)
	 * 返回：由paraS所构建的目的常数矩阵：Matrix型的mXB
	 * 功能：获取矩阵方程AX = B中的常数矩阵B，矩阵规模为：(3*number行, 1列)
	 */
	public Matrix getMatrixB(double[] paraS) {
		int number = this.getNumber();
		Matrix mParaS = Matrix.Factory.importFromArray(paraS);
		Matrix mXB = mParaS.appendVertically(Ret.NEW,
											 Matrix.Factory.zeros(2, number)).
							reshape(Ret.NEW, 3 * number, 1);
		return mXB;
	}
	
	/**
	 * 
	 * getMatrixA方法
	 * 输入：double[]型一维数组，chrodLength(弦长)
	 * 返回：由chrodLength计算及矩阵重塑形得到的系数矩阵：Matrix型的mXA
	 * 功能：获取矩阵方程AX = B中的系数矩阵A，矩阵规模为：(3*number行, 3*number列)
	 */ 
	public Matrix getMatrixA(double[] chrodLength) {
		int number = this.getNumber();
		//目标系数矩阵mXA初始化为0矩阵，矩阵规模：(3*number, 3*number)
		Matrix mXA = Matrix.Factory.zeros(3 * number, 3 * number);
		for (int i = 0; i < number; i++) {
			Double d = chrodLength[i];
			Double dd = d * d;
			Double ddd = d * d * d;
			//系数矩阵位于对角线的核心非零小矩阵ratioArray，矩阵规模：(3行, 3列)
			Double[] ratioArray = { ddd, dd, d,
									3 * d, 1.0, 0.0,
									3 * dd, 2 * d, 1.0 };
			/**
			 * Matrix.Factory.importFromArray(double[])
			 * 将数组按照列填充规则填充矩阵，因而注意转置.transpose()
			 */
			Matrix mRatio = Matrix.Factory.
							importFromArray(ratioArray).
							reshape(Ret.NEW, 3, 3).
							transpose();
			Matrix mZ1 = Matrix.Factory.zeros(3 * i, 3 * number);
			Matrix mZ2 = Matrix.Factory.zeros(3, 3 * i).
						 appendHorizontally(Ret.NEW, mRatio).
						 appendHorizontally(Ret.NEW,
									   		Matrix.Factory.
									   		zeros(3, 3 * (number - i - 1)));
			Matrix mZ3 = Matrix.Factory.zeros(3 * (number - i - 1),
											  3 * number);
			//矩阵mZ规模为：(3*number, 3*number)
			Matrix mZ = mZ1.appendVertically(Ret.NEW, mZ2).
							appendVertically(Ret.NEW, mZ3);
			//利用for循环，将mZ沿着对角线填充至矩阵mXA中
			mXA = mXA.plus(mZ);
			//非对角线上非零元素规律明显
			if (i == number - 1) {
				mXA.setAsDouble(-1.0, 3 * (i + 1) - 2, 1);
				mXA.setAsDouble(-1.0, 3 * (i + 1) - 1, 2);
			} else {
				mXA.setAsDouble(-1.0, 3 * (i + 1) - 2, 3 * (i + 2) - 2);
				mXA.setAsDouble(-1.0, 3 * (i + 1) - 1, 3 * (i + 2) - 1);
			}
		}
		return mXA;
	}
	
	/**
	 * 
	 * NoSinglarMartixSolve方法
	 * 输入：针对矩阵方程AX = B，非奇异系数矩阵A(方阵)，常数矩阵B(列矩阵)
	 * 返回：无
	 * 功能：求解三次参数样条函数的参数，并将参数求解结果保存至矩阵mX并返回
	 */
	public Matrix NoSinglarMartixSolve(Matrix A, Matrix B) {
		//未知数矩阵mX规模为：(3*number行,1列)
		Matrix mX = A.solve(B);	
		return mX;
	}
	
	/**
	 * 
	 * solutionResult方法
	 * 输入：x轴向的参数矩阵结果mX，y轴向的参数矩阵结果mY
	 * 返回：无
	 * 功能：针对x轴向的三次参数样条函数解析式：x(L) = a*L^3 + b*L^2 + c*L + d
	 * 	       求解参数a、b、c、d，通过调用本类solutionPara方法将求解结果分类保存至数组
	 * 	       因为三次参数样条函数解析式是依照弦长分段的，因而参数求解结果保存在double[4][]型二维数组中
	 * 	       针对y轴向的，同理
	 * 	       将结果同时保存至本目录下的文本文件中
	 */
	public void solutionResult(Matrix mX, Matrix mY) {
		//分别调用本类solutionPara方法获取参数求解的分类结果
		double[][] paraX = solutionPara(mX, this.dX);
		double[][] paraY = solutionPara(mY, this.dY);
		//获取累加弦长数据，用于设定解析式变量数值区间
		//其中文件名"累加弦长表.txt"，需要重构，可考虑作为对象的静态属性
		String[] summationArray = new FileOperation().readFile("累加弦长表.txt").
													  toString().
													  split(",");
		saveResult(paraX, paraY, summationArray);
	}
	
	/**
	 * 
	 * saveResult方法
	 * 输入：double[][]型二维数组 paraX，存有x轴向的解析式参数数据
	 *     double[][]型二维数组 paraY，存有y轴向的解析式参数数据
	 *     String[]型一维数组summationArray，存有累加弦长的数值字符串
	 * 输出："三次参数样条函数完整解析式.txt"
	 *     "三次参数样条函数参数表.txt"
	 * 功能：创建匿名类FileOperation，调用writeResult2File方法保存结果至文件
	 *     保存三次参数样条函数的参数求解结果
	 *     保存三次参数样条函数完整解析式结果
	 * @throws SQLException 
	 */
	
	public void saveResult(double[][] paraX,
						   double[][] paraY,
						   String[] summationArray) {
		//定义一个StringBuilder容器sb，存储结果信息，注意行文本末尾添加"\r\n"
		StringBuilder sb = new StringBuilder();
		StringBuilder sB = new StringBuilder();
		String line = null;
		//获取完整的三次参数样条解析式，在控制台及输出，同时存入文件
		for (int i = 0; i < number; i++) {
			double leftLimit = Double.parseDouble(summationArray[i].trim());
			double rightLimit = Double.parseDouble(summationArray[i+1].trim());
			line = ("------------------------------------------------"
					+ "\r\n第" + (i+1) + "段三次曲线解析式: "
					+ "(" +leftLimit + "<= L <" + rightLimit + ")\r\n");
			sb.append(line);
			line = ("x" + (i+1) + "(L) = ("
					+ String.format("%22.18f", paraX[0][i]) + ")*L^3 + ("
					+ String.format("%22.18f", paraX[1][i]) + ")*L^2 + ("
					+ String.format("%22.18f", paraX[2][i]) + ")*L + ("
					+ String.format("%22.18f", paraX[3][i]) + ")");
			sb.append(line + "\r\n");
			line = ("y" + (i+1) + "(L) = ("
					+ String.format("%22.18f", paraY[0][i]) + ")*L^3 + ("
					+ String.format("%22.18f", paraY[1][i]) + ")*L^2 + ("
					+ String.format("%22.18f", paraY[2][i]) + ")*L + ("
					+ String.format("%22.18f", paraY[3][i]) + ")");
			sb.append(line + "\r\n");
			line = ("-------------------------------------"
					+ "第" + (i+1) + "段:\r\n");
			sB.append(line);
			line = (  "a = (" + String.format("%22.18f", paraX[0][i]) + "), "
					+ "b = (" + String.format("%22.18f", paraX[1][i]) + "), "
					+ "c = (" + String.format("%22.18f", paraX[2][i]) + "), "
					+ "d = (" + String.format("%22.18f", paraX[3][i]) + "),\r\n"
					+ "e = (" + String.format("%22.18f", paraY[0][i]) + "), "
					+ "f = (" + String.format("%22.18f", paraY[1][i]) + "), "
					+ "g = (" + String.format("%22.18f", paraY[2][i]) + "), "
					+ "h = (" + String.format("%22.18f", paraY[3][i]) + "),\r\n");
			sB.append(line);			
		}
		save2DB(paraX, paraY);
		//将StringBuilder容器sb中字符串内容写入到指定文件中
		new FileOperation().writeResult2File(sb, "三次参数样条函数完整解析式.txt");
		new FileOperation().writeResult2File(sB, "三次参数样条函数参数表.txt");
	}
	
	public void save2DB(double[][] paraX, double[][] paraY) {
		/**
		 * 将每段曲线的参数以Parameter实体保存至数据库
		 */
		for (int i = 0; i < number; i++) {
			Parameter para = new Parameter();
			para.setxA(paraX[0][i]);
			para.setxB(paraX[1][i]);
			para.setxC(paraX[2][i]);
			para.setxD(paraX[3][i]);
			para.setyE(paraY[0][i]);
			para.setyF(paraY[1][i]);
			para.setyG(paraY[2][i]);
			para.setyH(paraY[3][i]);
			ParameterDao paraDao = new ParameterDao();
			try {
				paraDao.addParameter(para);
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				sop(e.toString());
			}
		}
	}
	
	/**
	 * 
	 * solutionPara方法
	 * 输入：Matrix型的矩阵m，其规模为(3*number行, 1列)，double[]型数组paraD，数组长度为number
	 * 返回：double[4][]型的二维数组para
	 * 功能：按照m = [a0, b0, c0, a1, b1, c1,...]，同类参数间隔3的规律
	 * 	        从求解结果矩阵m中提取出同类参数，存入临时数组paraA、paraB、paraC中
	 * 	        输入参数paraD与paraA、paraB、paraC共同构成完整的参数解
	 */
	public double[][] solutionPara(Matrix m, double[] paraD) {
		int number = this.getNumber();
		double[] paraA = new double[number];
		double[] paraB = new double[number];
		double[] paraC = new double[number];
		for (int i = 0; i < number; i++) {
			// m.getAsDouble(行角标, 列角标);
			// m.setAsDouble(double值, 行角标, 列角标);
			paraA[i] = m.getAsDouble(3 * i, 0);
			paraB[i] = m.getAsDouble(3 * i + 1, 0);
			paraC[i] = m.getAsDouble(3 * i + 2, 0);
		}
		double[][] para = new double[][] { paraA, paraB, paraC, paraD};
		return para;
	}	
	
	/**
	 * 
	 * sop方法
	 * 输入：任意对象Object
	 * 输出：在控制台输入对象的字符串表现形式
	 * 返回：无
	 */
	public void sop(Object obj) {
		
		System.out.println(obj);
	}
	
	/**
	 * lineSplit方法
	 * 输入：无
	 * 输出：在控制台输出----------------------------分隔符
	 * 返回：无
	 */
	public void lineSplit() {
		
		sop("----------------------------");
	}
}
