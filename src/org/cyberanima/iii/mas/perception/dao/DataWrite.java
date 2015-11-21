package org.cyberanima.iii.mas.perception.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import org.cyberanima.iii.common.dao.DBUtil;

public class DataWrite {
	public static void UBUpateByID(int id,
			int pstatus) throws Exception {
		String sqlComText = "UPDATE userbehavior SET pstatus = " + pstatus
				+ " WHERE userbehavior.id = " + id;
		DBUtil.executeUpdateW(sqlComText);
	}
}