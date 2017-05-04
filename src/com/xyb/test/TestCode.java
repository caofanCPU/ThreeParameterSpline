package com.xyb.test;

import java.sql.SQLException;

import org.junit.Test;

import com.xyb.dao.ParameterDao;
import com.xyb.domain.Parameter;

public class TestCode {
	@Test
	public void testSQL() {
		Parameter para = new Parameter();
		para.setxA(52.0);
		para.setxB(5.20);
		para.setxC(0.52);
		para.setxD(5.2);
		para.setyE(2.5);
		para.setyF(2.50);
		para.setyG(25.0);
		para.setyH(0.25);
		ParameterDao paraDao = new ParameterDao();
		try {
			paraDao.addParameter(para);
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} 
	}
}
