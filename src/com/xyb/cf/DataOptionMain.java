package com.xyb.cf;

import org.ujmp.core.Matrix;
import org.ujmp.core.calculation.Calculation.Ret;

/**
 * @author CY_XYZ
 * DataOptionMain��
 * DataOptionMain�ࣺ��Ŀ���ģ��
 * ���ܣ���ȡԭʼ�����ļ�
 * 	        �������������⣬��ȡ������
 *     ���洦����
 */
public class DataOptionMain {

	public static void main(String[] args) {
		//�������ݵķŴ���magnification��ɰ��Բ�뾶circleRadius
		
		int magnification = 1;
		double circleRadius = magnification;
		//���ñ���getData������ͨ���Զ������ݺ��Զ����д���
		getData(magnification);
		//������getDataByFile������ͨ����ȡ�ļ����ݽ����Զ�����
		//getDataByFile("��ֵ������.txt");
		new PointData("1.0000", circleRadius).
		getParaData("���β�����������������.txt", "�ۼ��ҳ���.txt");
		
		new DataVisualization();
	}
	
	/**
	 * getDataByFile����
	 * ���룺String���ļ�������fileName
	 * ��������б�׼�����ݵ�'��׼����ֵ��.txt'�ļ�
	 * ���أ���
	 * ���ܣ�ָ��ԭʼ�����ļ�·�������жϸ��ļ�������ڣ������׳�RuntimeEXception�쳣
	 *     ����DataCleaning��������
	 *     ���ö���ķ�����ȡԭʼ�����ļ����ݣ���������ƥ��������ϴ
	 *     ��������ϴ���������Matrix�;���coordMatrix�У����ڿ���̨���
	 * �쳣��ԭʼ�����ļ������ڣ������ƶ�ԭʼ����·������
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
	 * getData����
	 * ���룺��
	 * ���أ���
	 * ���ܣ�����(��ȡ)��ֵ���ά���꣬������double[][]�Ͷ�ά���飬coodinateArray
	 * 	        �����������ݱ����Ŵ���int�ͣ�magnification
	 * 	        ���ñ���calculation����
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
		//ԭʼ�����ļ���������󣬱���Ϊ'��׼����ֵ��.txt'�ļ�
		//String.format("%6.2f",data)����ʽ�����ݵ��ַ�����ʽ���Ҷ��롢��6λ��2λС����f������
		StringBuilder sB = new StringBuilder();
		for (int i = 0; i < coordinateArray[0].length; i++) {
			sB.append("X" + String.format("%8.2f", coordinateArray[0][i]) + ", Y"
					  + String.format("%8.2f", coordinateArray[1][i]) + ",\r\n");
		}
		String fileName = "��׼����ֵ��.txt";
		new FileOperation().writeResult2File(sB, fileName);
	}
	
	
	/**
	 * 
	 * calculation����
	 * ���룺double[][]�Ͷ�ά���飬coodinateArray��int�ͱ�����magnification
	 * ���أ���
	 * ���ܣ�����magnification����ParameterSolution����
	 * 	        ����coodinateArray�����ҳ�
	 * 	        ����ParameterSolution����ķ����������������󷽳�AX = B 
	 */
	public static void calculation(double[][] coordinateArray) {
		ParameterSolution ps = new ParameterSolution();
		ps.getChrodLength(coordinateArray);
		//��ȡ��������mXB��mYB
		Matrix mXB = ps.getMatrixB(ps.getParaX());
		Matrix mYB = ps.getMatrixB(ps.getParaY());
		
		//��ȡϵ������mXB��mYB
		Matrix mXA = ps.getMatrixA(ps.getChrodLength());
		Matrix mYA = mXA;
		
		//��ȡx��δ֪����mX����ȡy��δ֪����mY
		Matrix mX = ps.NoSinglarMartixSolve(mXA, mXB);
		Matrix mY = ps.NoSinglarMartixSolve(mYA, mYB);
		//mX.showGUI();
		//��������
		ps.solutionResult(mX, mY);


		//����ע�ʹ����ʹ����������Ƕ�׵��õȼ����
		/*ps.solutionResult(ps.NoSinglarMartixSolve(ps.getMatrixA(ps.getChrodLength()),
												  ps.getMatrixB(ps.getParaX())),
						  ps.NoSinglarMartixSolve(ps.getMatrixA(ps.getChrodLength()),
								  				  ps.getMatrixB(ps.getParaY())));*/
	}
	
	/**
	 * 
	 * sop����
	 * ���룺�������Object
	 * ������ڿ���̨���������ַ���������ʽ
	 * ���أ���
	 */
	public static void sop(Object obj) {
		System.out.println(obj);
	}
	
	/**
	 * lineSplit����
	 * ���룺��
	 * ������ڿ���̨���----------------------------�ָ���
	 * ���أ���
	 */
	public static void lineSplit() {
		
		sop("----------------------------");
	}
	
	/**
	 * UJMPPractice����
	 * ������ϰ��UJMP�У� 1.ͨ�����鴴������
	 * 			     2.����ļӷ������������ˡ�����˷�
	 * 				 3.�����������
	 * 				 4.���Է���������
	 * 				 5.��ȡ����Ĺ�ģ(����, ����)
	 * 				 6.ͨ�����л�ȡ����Ԫ�ء�ͨ���������þ���Ԫ��(ע�����������(0,0)��ʼ)
	 */
	public static void UJMPPractice() {
		
		/**		
		 * Java����Ԫ�س˷�.times()
		 * 		����˷�.mtimes()
		 * 		����������.reshape(Ret.NEW, ������, ������);
		 * 		�����ж���ϲ�(ˮƽ���).appendHorizontally(Ret.NEW, ����)
		 * 		�����ж���ϲ�(��ֱ���).appendVertically(Ret.NEW, ����)
		 */
		//����3��3�еĵ�λ����mXA
		Matrix mXA = Matrix.Factory.ones(3, 3);
		//����mXA������Ԫ��ͬ����8����������
		Matrix mX = mXA.times(8);
		//����mXA���������β������˲��������ı����mXA������Ret.NEWΪ������ʽ
		Matrix mXB = mXA.reshape(Ret.NEW, 1, 9);
		//��ӡ����mX������Ԫ��
		sop(mX);
		//��ӡ�ָ���
		lineSplit();
		//��ӡ����mXB
		sop(mXB);
		//������mXBָ�����mXA
		mXB = mXA;
		//����mXA�����mXB����ֱ�ϲ�(�ж���ϲ�)
		Matrix mXC = mXA.appendVertically(Ret.NEW, mXB);
		//����mXA�����mXB��ˮƽ�ϲ�(�ж���ϲ�)
		Matrix mXD = mXA.appendHorizontally(Ret.NEW, mXB);
		//��ӡ����mXC
		sop(mXC);
		//��ӡ����mXD
		sop(mXD);
		//��ӡ����mXA��������������mXA�е�ά��
		sop(mXA.getColumnCount());
		//��ӡ����mXA��������������mXA�е�ά��
		sop(mXA.getRowCount());
		//����3��1�еĵ�λ����mb
		Matrix mb = Matrix.Factory.ones(3, 1);
		//���ھ��󷽳�(ϵ������mXA)x = (��������mb)��ʹ��mXA.solve(mb)���x
		//ע�⣺ʹ��.solve()����ǰ�᣺ϵ������mXA������(����ʽֵ��=0)
		Matrix x = mXA.solve(mb);
		//����mXA���Ծ���x������˷���ע�����˷������ж�Ӧ����
		Matrix Rb = mXA.mtimes(x);
		//��ӡ����mXA������Ԫ��
		sop(mXA);
		//��ӡ�ָ���
		lineSplit();
		//��ӡ����mb������Ԫ��
		sop(mb);
		//��ӡ�ָ���
		lineSplit();
		//��ӡ����x������Ԫ��
		sop(x);
		//��ӡ�ָ���
		lineSplit();
		//��ӡ����mXA������Ԫ��
		sop(mXA);
		//��ӡ�ָ���
		lineSplit();
		//��ӡ����Rb������Ԫ��
		sop(Rb);
	}
}