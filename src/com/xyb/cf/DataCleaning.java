package com.xyb.cf;

/**
 * @author CY_XYZ
 * DataCleaning类：项目输入数据清洗模块
 * 功能：读取输入数据文件，进行正则匹配处理，得到标准化数据
 * 	        将标准化数据存入数据库
 *     从数据库获取标准化数据
 */
public class DataCleaning {

	public DataCleaning() {
	
	}
	
	/**
	 * 
	 * dataRegx方法
	 * 输入：StringBuilder对象sb，sb中为数据文本文件的全部内容
	 * 输出：无
	 * 返回：double[][]型二维数组
	 * 功能：对sb中文本数据进行正则匹配处理
	 *     格式化输入数据，保存至文件及数据库中
	 * 异常：
	 *     文件有效内容不满足坐标对
	 *     文件有效内容不满足封闭条件
	 */
	public double[][] dataRegx(StringBuilder sb) {
		/**
		 * 利用StringBuilder容器sb的.toString()方法，得到原始数据文件内容的一行字符串表示line
		 * 将line中，非'.'、','、"\\d"数字字符,'-'的其他所有字符替换为空，得到的结果仍旧为line
		 * 此处，line的内容为:double值,double值,...,double值
		 * 依据','字符将line进行分割，将得到一个String[]型数组valueString
		 * 利用循环及Double.parseDouble()方法将valueString数组中的字符串替换为同等的double值
		 */
		String line = sb.toString();
		String[] valueString = line.replaceAll("[^.,\\d-]", "").split(",");
		//如果字符串元素为单数，抛出RuntimeException异常
		if (0 != (valueString.length % 2)) {
			throw new RuntimeException("原始数据文件中坐标数据未成对出现，请检查及修改！");
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
		//如果坐标数据不满足封闭条件，抛出RuntimeException异常
		if (coordinateArray[0][0] != coordinateArray[0][midPosition-1]
			|| coordinateArray[1][0] != coordinateArray[1][midPosition-1]) {
			throw new RuntimeException("原始数据文件中坐标数据起点与终点不一致，无法满足封闭条件！");
		} else {
			return coordinateArray;
		}
	}
}
