package org.cyberanima.iii.mas.perception.dao;

import org.cyberanima.iii.common.dao.DBUtil;
import org.cyberanima.iii.common.dao.DataRow;
import org.cyberanima.iii.common.dao.TypeProperties;

public class DataSearch {

	public static DataRow getUnperceivedUB() throws Exception
    {
        String sqlComText = "SELECT TOP 1 * FROM userbehavior WHERE pstatus = " + TypeProperties.UB_Unperceived
        		+ " ORDER BY time";
        return DBUtil.getDataRow(sqlComText);
    }

}
