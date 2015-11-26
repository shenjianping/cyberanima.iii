package org.cyberanima.iii.mas.perception.dao;

import org.cyberanima.iii.common.dao.DBUtil;
import org.cyberanima.iii.common.dao.DataRow;
import org.cyberanima.iii.common.dao.TypeProperties;

public class DataSearch {

	public static DataRow getUnperceivedUB() throws Exception
    {
        String sqlComText = "SELECT * FROM userbehavior WHERE pstatus = " + TypeProperties.UB_Unperceived
        		+ " ORDER BY time ASC LIMIT 0, 1";
        return DBUtil.getDataRow(sqlComText);
    }

}
