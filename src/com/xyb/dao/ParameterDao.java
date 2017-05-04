package com.xyb.dao;

import java.sql.SQLException;
import org.apache.commons.dbutils.QueryRunner;
import com.xyb.domain.Parameter;
import com.xyb.util.C3P0Util;

public class ParameterDao {

	public void addParameter(Parameter para) throws SQLException {
		QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
		String sql = "INSERT INTO parameter(xA,xB,xC,xD,yE,yF,yG,yH) "
				+ "VALUES(?,?,?,?,?,?,?,?)";
		qr.update(sql,
				para.getxA(), para.getxB(), para.getxC(), para.getxD(),
				para.getyE(), para.getyF(), para.getyG(), para.getyH());
	}
}
