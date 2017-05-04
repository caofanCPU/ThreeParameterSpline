package com.xyb.cf;

/**
 * @author CY_XYZ
 * PointData�ࣺ��Ŀ��ͼ�������ݼ���
 * ���ܣ��������β��������������ߵ����������(X, Y)
 * 	        ����
 */
public class PointData {
	/**
	 * PointData���Ա����
	 * double����ֵprecision���������β����������ߵĲ�ֵ������ȣ�Ĭ��0.005
	 * int��precisionNumber���������β������������������ݵ�С��λ����Ĭ��С�����4λ
	 * double��circleRadius������ɰ��Բ�İ뾶ֵ��Ĭ��Ϊ1.0
	 */
	private double precision = 0.005;
	private int precisionNumber = 4;
	private double circleRadius = 1;
	/**
	 * PointData��Ĵ��ι��췽��
	 * ���룺String����ֵ�ַ���precisionStr
	 * ���أ���
	 * ���ܣ����ñ���calculatePrecision����
	 *     ��ȡ��ֵ�ַ���precisionStr����ʾ����ֵ����
	 */
	public PointData(String precisionStr, double circleRadius) {
		this.precision = calculatePrecision(precisionStr);
		this.circleRadius = circleRadius;
	}
	
	/**
	 * 
	 * PointData���Ա���Ե�set��get����
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
	 * calculatePrecision����
	 * ���룺String����ֵ�ַ���precisionStr
	 * ���أ�����������double����ֵprecision
	 * ���ܣ���ȡ������ֵ�ַ���precisionStr��ʾ����ֵ����
	 *     ��������ֵ�ַ���ת����"0.0...1"����ֵ��ʽ
	 * ��ע�����ʵ�����ⱳ�������ܣ���ʱ�޶�����Ϊ0.0001
	 */
	public double calculatePrecision(String precisionStr) {
		//��ȡС��λ�������ҳ�"."��λ��index���ַ����ĳ���.length() - index - 1
		int precisionNumber = precisionStr.length() - precisionStr.indexOf('.') - 1;
		//���ھ��ȿ��ǣ��޶���ֵ������СΪ0.005
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
	 * getParaData����
	 * ���룺String�Ͷ���paraFileName�����β��������������������ݡ��ļ���
	 *     String�Ͷ���summationFileName�����β��������������ۼ��ҳ����ݡ��ļ���
	 * ���أ���
	 * ���ܣ���ȡ���β������������������ݣ�������String[]������paraArray��
	 *     ��ȡ��ֵ���ۼ��ҳ��б����ݣ�������String[]������summationArray��
	 *     ���ñ���calculateCoordinate�����������β������������
	 */
	public void getParaData(String paraFileName, String summationFileName) {
		StringBuilder sb = new FileOperation().readFile(paraFileName);
		String line = sb.toString();
		String[] paraArray = line.replaceAll("-*?��\\d{1,2}��:", "").
								  replaceAll("[^\\d-,\\.]", "").
								  split(",");
		//�Ի�õĲ������ݽ���ȱʧ���
		if (0 != (paraArray.length % 8)) {
			throw new RuntimeException("�ļ�\"" + paraFileName + "\"���ݴ���ȱʧ������δ��Ӧ��");
		}
		//��ȡ�ۼ��ҳ�����borderArray���������β���������������ʽ�ķֶα��
		String[] borderArray = new FileOperation().readFile("�ۼ��ҳ���.txt").
				  								   toString().
				  								   split(",");
		//���ñ���getCoordinate����
		getCoordinate(paraArray, borderArray);
		//System.out.println(paraArray.length/8 + "\n" + borderArray.length);
	}
	
	/**
	 * 
	 * getCoordinate����
	 * ���룺String[]������paraArray�����β������������Ĳ���ֵ
	 *     String[]������borderArray���ۻ��ҳ�ֵ��Ϊ���β������������ķֶα߽�
	 * ��������β����������������ļ���"���β�����������ԭʼ����.txt"
	 *     ɰ��Բ�������ļ���"ɰ��Բ��ԭʼ����.txt"
	 */
	public void getCoordinate(String[] paraArray, String[] borderArray) {
		
		//�ҳ���߽�leftLimit���ҳ��ұ߽�rightLimit
		double leftLimit;
		double rightLimit;
		double[] para = new double[8];
		StringBuilder sb = new StringBuilder();
		StringBuilder sB = new StringBuilder();
		StringBuilder[] sbArray = null;
		int segments = paraArray.length / 8;
		for (int i = 0; i < segments; i++) {
			//�����ҳ��߽����򣬼�����Ρ��������β�����������
			leftLimit = string2double(borderArray[i]);
			rightLimit = string2double(borderArray[i+1]);
			//��ȡ�����������������ڸöεĲ���
			for (int j = 0; j < 8; j++) {
				para[j] = string2double(paraArray[i * 8 + j]);
			}
			sbArray = calculateCoordinate(para, leftLimit, rightLimit);
			sb.append(sbArray[0].toString());
			sB.append(sbArray[1].toString());
		}
		new FileOperation().writeResult2File(sb, "���β�����������ԭʼ����.txt");
		new FileOperation().writeResult2File(sB, "ɰ��Բ�ı任��ϵ����.txt");
		//���StringBuilder����
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
		//StringBuilder����sb�洢������������������ʽ������(x, y)
		StringBuilder sb = new StringBuilder();
		//StringBuilder����sB�洢ɰ��Բ�����꣬�任��ϵ��ʽ������(ds, 0, cosTheta, sinTheta)
		StringBuilder sB = new StringBuilder();
		for (int i = 0; i < detNumber; i++) {
			//d�൱�ڽ���ʽ���Ա���L
			double d = string2double(String.format("%6.4f", (i * det)));
			double dd = d * d;
			double ddd = d * d * d;
			//�е�����(x, y)
			x = para[0] * ddd + para[1] * dd + para[2] * d + para[3];
			y = para[4] * ddd + para[5] * dd + para[6] * d + para[7];
			sb.append("X" + String.format("%12.8f", x) + ", "
					  + "Y" + String.format("%12.8f", y) + ",\r\n");
			//�е㴦������(����)(dx, dy)-->����任ǰɰ��Բ������(dx, dy)
			//����ʹ��ͬһ������������ֵ������Щֵ�������㣬���׻������˷�ʱ����ԣ�����
			dx = 3 * para[0] * dd + 2 * para[1] * d + para[2];
			dy = 3 * para[4] * dd + 2 * para[5] * d + para[6];
			ds = Math.sqrt(dx * dx + dy * dy);
			dX = x + circleRadius * dy / ds;
			dY = y - circleRadius * dx / ds;
			dS = Math.sqrt(dX * dX + dY * dY);
			cosTheta = dX / dS;
			sinTheta = dY / dS;
			/**
			 * �е������Ϊ(dX, dY)��ɰ��Բ������(ds, 0)���任��ϵ����(cosTheta, sinTheta)
			 * dX = dx * cosTheta + dy * sinTheta;
			 * dY = dy * cosTheta - dx * sinTheta;
			 */
			sB.append("X" + String.format("%12.8f", dS) + ", "
					  + "cosTheta" + String.format("%12.8f", cosTheta) + ", "
					  + "sinTheta" + String.format("%12.8f", sinTheta) + ",\r\n");
		}
		//���ڴ洢���β�����������������StringBuilder������  sbArray[0]�洢X����, Y����
		//										  sbArray[1]�洢�õ��Ӧ��������X����, Y����
		StringBuilder[] sbArray = { sb, sB };
		return sbArray;
	}	
	
	/**
	 * 
	 * string2double����
	 * ���룺String�Ͷ���StringData��StringData������Ϊdouble����ֵ����
	 * ���أ�double����ֵ����data
	 * ���ܣ���double����ֵ�ı��ַ�����ת���ɶ�Ӧ��double����ֵ
	 */
	public double string2double(String StringData) {
		double data = Double.parseDouble(StringData.trim());
		return data;
	}
}
