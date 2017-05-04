package com.xyb.cf;

/**
 * @author CY_XYZ
 * DataCleaning�ࣺ��Ŀ����������ϴģ��
 * ���ܣ���ȡ���������ļ�����������ƥ�䴦���õ���׼������
 * 	        ����׼�����ݴ������ݿ�
 *     �����ݿ��ȡ��׼������
 */
public class DataCleaning {

	public DataCleaning() {
	
	}
	
	/**
	 * 
	 * dataRegx����
	 * ���룺StringBuilder����sb��sb��Ϊ�����ı��ļ���ȫ������
	 * �������
	 * ���أ�double[][]�Ͷ�ά����
	 * ���ܣ���sb���ı����ݽ�������ƥ�䴦��
	 *     ��ʽ���������ݣ��������ļ������ݿ���
	 * �쳣��
	 *     �ļ���Ч���ݲ����������
	 *     �ļ���Ч���ݲ�����������
	 */
	public double[][] dataRegx(StringBuilder sb) {
		/**
		 * ����StringBuilder����sb��.toString()�������õ�ԭʼ�����ļ����ݵ�һ���ַ�����ʾline
		 * ��line�У���'.'��','��"\\d"�����ַ�,'-'�����������ַ��滻Ϊ�գ��õ��Ľ���Ծ�Ϊline
		 * �˴���line������Ϊ:doubleֵ,doubleֵ,...,doubleֵ
		 * ����','�ַ���line���зָ���õ�һ��String[]������valueString
		 * ����ѭ����Double.parseDouble()������valueString�����е��ַ����滻Ϊͬ�ȵ�doubleֵ
		 */
		String line = sb.toString();
		String[] valueString = line.replaceAll("[^.,\\d-]", "").split(",");
		//����ַ���Ԫ��Ϊ�������׳�RuntimeException�쳣
		if (0 != (valueString.length % 2)) {
			throw new RuntimeException("ԭʼ�����ļ�����������δ�ɶԳ��֣����鼰�޸ģ�");
		}
		int midPosition = valueString.length / 2;
		double[][] coordinateArray = new double[2][midPosition];
		for (int i = 0; i < valueString.length; i++) {
			double value = Double.parseDouble(valueString[i]);
			if (i < midPosition) {
				coordinateArray[0][i] = value;
			} else {
				coordinateArray[1][i-midPosition] = value;
			}
		}
		//����������ݲ��������������׳�RuntimeException�쳣
		if (coordinateArray[0][0] != coordinateArray[0][midPosition-1]
			|| coordinateArray[1][0] != coordinateArray[1][midPosition-1]) {
			throw new RuntimeException("ԭʼ�����ļ�����������������յ㲻һ�£��޷�������������");
		} else {
			return coordinateArray;
		}
	}
}
