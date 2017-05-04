package com.xyb.cf;

import java.sql.SQLException;
import org.ujmp.core.Matrix;
import org.ujmp.core.calculation.Calculation.Ret;
import com.xyb.dao.ParameterDao;
import com.xyb.domain.Parameter;

/**
 * @author CY_XYZ
 * ParameterSolution�ࣺ��Ŀ���㷨ʵ�ֺ���ģ��
 * ���ܣ�ʵ�������β������������ľ��󷽳�AX = B�Ĺ����Լ����
 *     �������������������ļ�
 */
public class ParameterSolution {
	/**
	 * 
	 * ��Ա���ԣ�int��magnification�������������ݵı����Ŵ�����Ĭ��Ϊ1
	 * 		  int��number�������ά����������Χ�ɵķ��ͼ�ε�[�ҳ���Ŀ]��Ĭ��Ϊ0
	 * 		  double[]������paraX����������������x���ϵ����ڼ��
	 * 		  double[]������paraX����������������y���ϵ����ڼ��
	 * 		  double[]������chrodLength�����������������ҳ�
	 * 		  double[]������dX������x�����ϲ���d�������
	 * 		  double[]������dY������y�����ϲ���d�������
	 */
	private int number = 0;
	private double[] paraX = null;
	private double[] paraY = null;
	private double[] chrodLength = null;
	private double[] dX = null;
	private double[] dY = null;

	/**
	 * 
	 * Ĭ�Ϲ��캯��
	 */
	public ParameterSolution() {
		
	}
	
	/**
	 * 
	 * ��Ա���Ե�set��get����
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
	 * getChrodLength����
	 * ���룺double[][]�Ͷ�ά���飬coordinateArray
	 * ���أ���
	 * ���ܣ��ж��������Ƿ���ά����ͬ�Ķ�ά����
	 *     �������룬����ÿ������ά�ȵ�������detX��detY
	 *            ���������֮����ҳ�(��άֱ�߾���)chrodLength
	 *     �����ҳ�chrodLength�������ۼ��ҳ��������ļ���"�ۼ��ҳ���.txt"
	 * �쳣��������Ϊ��ͬά�ȵĶ�ά����ʱ���׳�RuntimeException
	 */
	public void getChrodLength(double[][] coordinateArray) {
		double[] chrodLength = new double[coordinateArray[0].length - 1];
		int number = chrodLength.length;
		try {
			if (number != coordinateArray[1].length - 1) {
				throw new RuntimeException("�����ά��������ά�Ȳ�һ�£����飡");
			}
			if (number < 1) {
				throw new RuntimeException("�����ά�������鳤��̫С(������������)�����飡");
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
		//�����ҳ�ChrodLength�����ۼ��ҳ���
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%20.16f", 0.0) + ",\r\n");
		double sum = 0;
		for (int i = 0; i < chrodLength.length; i++) {
			//�������һ��Ԫ�أ���ʽ�����Ӧ�ó���','�����ⲻӰ�챾��Ŀ
			sum += chrodLength[i];
			sb.append(String.format("%20.16f", sum) + ",\r\n");
		}
		new FileOperation().writeResult2File(sb, "�ۼ��ҳ���.txt");
	}

	/**
	 * getMatrixB����
	 * ���룺double[]��һά���飬paraS(����)
	 * ���أ���paraS��������Ŀ�ĳ�������Matrix�͵�mXB
	 * ���ܣ���ȡ���󷽳�AX = B�еĳ�������B�������ģΪ��(3*number��, 1��)
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
	 * getMatrixA����
	 * ���룺double[]��һά���飬chrodLength(�ҳ�)
	 * ���أ���chrodLength���㼰���������εõ���ϵ������Matrix�͵�mXA
	 * ���ܣ���ȡ���󷽳�AX = B�е�ϵ������A�������ģΪ��(3*number��, 3*number��)
	 */ 
	public Matrix getMatrixA(double[] chrodLength) {
		int number = this.getNumber();
		//Ŀ��ϵ������mXA��ʼ��Ϊ0���󣬾����ģ��(3*number, 3*number)
		Matrix mXA = Matrix.Factory.zeros(3 * number, 3 * number);
		for (int i = 0; i < number; i++) {
			Double d = chrodLength[i];
			Double dd = d * d;
			Double ddd = d * d * d;
			//ϵ������λ�ڶԽ��ߵĺ��ķ���С����ratioArray�������ģ��(3��, 3��)
			Double[] ratioArray = { ddd, dd, d,
									3 * d, 1.0, 0.0,
									3 * dd, 2 * d, 1.0 };
			/**
			 * Matrix.Factory.importFromArray(double[])
			 * �����鰴�������������������ע��ת��.transpose()
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
			//����mZ��ģΪ��(3*number, 3*number)
			Matrix mZ = mZ1.appendVertically(Ret.NEW, mZ2).
							appendVertically(Ret.NEW, mZ3);
			//����forѭ������mZ���ŶԽ������������mXA��
			mXA = mXA.plus(mZ);
			//�ǶԽ����Ϸ���Ԫ�ع�������
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
	 * NoSinglarMartixSolve����
	 * ���룺��Ծ��󷽳�AX = B��������ϵ������A(����)����������B(�о���)
	 * ���أ���
	 * ���ܣ�������β������������Ĳ����������������������������mX������
	 */
	public Matrix NoSinglarMartixSolve(Matrix A, Matrix B) {
		//δ֪������mX��ģΪ��(3*number��,1��)
		Matrix mX = A.solve(B);	
		return mX;
	}
	
	/**
	 * 
	 * solutionResult����
	 * ���룺x����Ĳ���������mX��y����Ĳ���������mY
	 * ���أ���
	 * ���ܣ����x��������β���������������ʽ��x(L) = a*L^3 + b*L^2 + c*L + d
	 * 	       ������a��b��c��d��ͨ�����ñ���solutionPara��������������ౣ��������
	 * 	       ��Ϊ���β���������������ʽ�������ҳ��ֶεģ�������������������double[4][]�Ͷ�ά������
	 * 	       ���y����ģ�ͬ��
	 * 	       �����ͬʱ��������Ŀ¼�µ��ı��ļ���
	 */
	public void solutionResult(Matrix mX, Matrix mY) {
		//�ֱ���ñ���solutionPara������ȡ�������ķ�����
		double[][] paraX = solutionPara(mX, this.dX);
		double[][] paraY = solutionPara(mY, this.dY);
		//��ȡ�ۼ��ҳ����ݣ������趨����ʽ������ֵ����
		//�����ļ���"�ۼ��ҳ���.txt"����Ҫ�ع����ɿ�����Ϊ����ľ�̬����
		String[] summationArray = new FileOperation().readFile("�ۼ��ҳ���.txt").
													  toString().
													  split(",");
		saveResult(paraX, paraY, summationArray);
	}
	
	/**
	 * 
	 * saveResult����
	 * ���룺double[][]�Ͷ�ά���� paraX������x����Ľ���ʽ��������
	 *     double[][]�Ͷ�ά���� paraY������y����Ľ���ʽ��������
	 *     String[]��һά����summationArray�������ۼ��ҳ�����ֵ�ַ���
	 * �����"���β�������������������ʽ.txt"
	 *     "���β�����������������.txt"
	 * ���ܣ�����������FileOperation������writeResult2File�������������ļ�
	 *     �������β������������Ĳ��������
	 *     �������β�������������������ʽ���
	 * @throws SQLException 
	 */
	
	public void saveResult(double[][] paraX,
						   double[][] paraY,
						   String[] summationArray) {
		//����һ��StringBuilder����sb���洢�����Ϣ��ע�����ı�ĩβ���"\r\n"
		StringBuilder sb = new StringBuilder();
		StringBuilder sB = new StringBuilder();
		String line = null;
		//��ȡ���������β�����������ʽ���ڿ���̨�������ͬʱ�����ļ�
		for (int i = 0; i < number; i++) {
			double leftLimit = Double.parseDouble(summationArray[i].trim());
			double rightLimit = Double.parseDouble(summationArray[i+1].trim());
			line = ("------------------------------------------------"
					+ "\r\n��" + (i+1) + "���������߽���ʽ: "
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
					+ "��" + (i+1) + "��:\r\n");
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
		//��StringBuilder����sb���ַ�������д�뵽ָ���ļ���
		new FileOperation().writeResult2File(sb, "���β�������������������ʽ.txt");
		new FileOperation().writeResult2File(sB, "���β�����������������.txt");
	}
	
	public void save2DB(double[][] paraX, double[][] paraY) {
		/**
		 * ��ÿ�����ߵĲ�����Parameterʵ�屣�������ݿ�
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
				// TODO �Զ����ɵ� catch ��
				sop(e.toString());
			}
		}
	}
	
	/**
	 * 
	 * solutionPara����
	 * ���룺Matrix�͵ľ���m�����ģΪ(3*number��, 1��)��double[]������paraD�����鳤��Ϊnumber
	 * ���أ�double[4][]�͵Ķ�ά����para
	 * ���ܣ�����m = [a0, b0, c0, a1, b1, c1,...]��ͬ��������3�Ĺ���
	 * 	        �����������m����ȡ��ͬ�������������ʱ����paraA��paraB��paraC��
	 * 	        �������paraD��paraA��paraB��paraC��ͬ���������Ĳ�����
	 */
	public double[][] solutionPara(Matrix m, double[] paraD) {
		int number = this.getNumber();
		double[] paraA = new double[number];
		double[] paraB = new double[number];
		double[] paraC = new double[number];
		for (int i = 0; i < number; i++) {
			// m.getAsDouble(�нǱ�, �нǱ�);
			// m.setAsDouble(doubleֵ, �нǱ�, �нǱ�);
			paraA[i] = m.getAsDouble(3 * i, 0);
			paraB[i] = m.getAsDouble(3 * i + 1, 0);
			paraC[i] = m.getAsDouble(3 * i + 2, 0);
		}
		double[][] para = new double[][] { paraA, paraB, paraC, paraD};
		return para;
	}	
	
	/**
	 * 
	 * sop����
	 * ���룺�������Object
	 * ������ڿ���̨���������ַ���������ʽ
	 * ���أ���
	 */
	public void sop(Object obj) {
		
		System.out.println(obj);
	}
	
	/**
	 * lineSplit����
	 * ���룺��
	 * ������ڿ���̨���----------------------------�ָ���
	 * ���أ���
	 */
	public void lineSplit() {
		
		sop("----------------------------");
	}
}
