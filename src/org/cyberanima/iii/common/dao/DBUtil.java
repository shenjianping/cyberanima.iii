package org.cyberanima.iii.common.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

import org.cyberanima.iii.common.utils.SpringContextUtil;
import com.zwlsoft.service.dao.datasource.DataSourceConstant;
import com.zwlsoft.service.dao.datasource.DataSourceHolder;

public class DBUtil
{

    static JdbcTemplate jdbcTemplateR = (JdbcTemplate) SpringContextUtil.getBean("jdbcTemplate");
    static JdbcTemplate jdbcTemplateW = (JdbcTemplate) SpringContextUtil.getBean("jdbcTemplate");

    public static void executeUpdateW(String sql) throws Exception
    {
        DataSourceHolder.setCustomerType(DataSourceConstant.mssqlWrite);
        jdbcTemplateW.update(sql);
    }

    public static void executeUpdateS(String sql) throws Exception
    {
        DataSourceHolder.setCustomerType(DataSourceConstant.mssqlRead);
        jdbcTemplateR.update(sql);
    }

    public static String getDataString(String sql) throws Exception
    {
        DataSourceHolder.setCustomerType(DataSourceConstant.mssqlRead);
        String str = null;
        SqlRowSet rs = jdbcTemplateR.queryForRowSet(sql);
        if (rs.next())
        {
            str = rs.getString(1);
        }

        return str;
    }

    public static Integer getDataInteger(String sql) throws Exception
    {
        DataSourceHolder.setCustomerType(DataSourceConstant.mssqlRead);
        Integer a = null;
        SqlRowSet rs = jdbcTemplateR.queryForRowSet(sql);
        if (rs.next())
        {
            a = rs.getInt(1);
        }

        return a;
    }

    public static Boolean getDataBoolean(String sql) throws Exception
    {
        DataSourceHolder.setCustomerType(DataSourceConstant.mssqlRead);
        Boolean a = null;
        SqlRowSet rs = jdbcTemplateR.queryForRowSet(sql);
        if (rs.next())
        {
            a = rs.getBoolean(1);
        }

        return a;
    }

    public static List<DataRow> getDataTable(String sql) throws Exception
    {
        DataSourceHolder.setCustomerType(DataSourceConstant.mssqlRead);
        SqlRowSet rs = jdbcTemplateR.queryForRowSet(sql);
        List<DataRow> rowList = new ArrayList<DataRow>();
        SqlRowSetMetaData rsmd = rs.getMetaData();
        List<DataColumn> col = null;
        DataRow dataRow = null;
        DataColumn dataColumn = null;

        String columnName;
        Object value;
        while (rs.next())
        {
            col = new ArrayList<DataColumn>();
            for (int i = 1; i <= rsmd.getColumnCount(); i++)
            {
                columnName = rsmd.getColumnName(i);
                value = rs.getObject(columnName);
                dataColumn = new DataColumn(columnName, value);
                col.add(dataColumn);
            }
            dataRow = new DataRow(col);
            rowList.add(dataRow);
        }
        return rowList;
    }

    public static List<String> getDataListString(String sql) throws Exception
    {
        DataSourceHolder.setCustomerType(DataSourceConstant.mssqlRead);
        List<String> lsReturn = new ArrayList<String>();
        SqlRowSet rs = jdbcTemplateR.queryForRowSet(sql);
        while (rs.next())
        {
            lsReturn.add(rs.getString(1));
        }
        return lsReturn;
    }

    public static DataRow getDataRowW(String sql) throws Exception
    {
        DataSourceHolder.setCustomerType(DataSourceConstant.mssqlWrite);
        final DataRow dataRow = new DataRow();
        jdbcTemplateW.query(sql, new RowCallbackHandler()
        { // editing
            public void processRow(ResultSet rs) throws SQLException
            {
                DataColumn dataColumn = null;
                String columnName;
                Object value;
                ResultSetMetaData rsmd = rs.getMetaData();
                List<DataColumn> col = new ArrayList<DataColumn>();

                for (int i = 1; i <= rsmd.getColumnCount(); i++)
                {
                    columnName = rsmd.getColumnName(i);
                    value = rs.getObject(columnName);
                    dataColumn = new DataColumn(columnName, value);
                    col.add(dataColumn);
                }
                dataRow.setDataColumn(col);
                return;
            }
        });
        return dataRow;
    }

    public static DataRow getDataRow(String sql) throws Exception
    {
        DataSourceHolder.setCustomerType(DataSourceConstant.mssqlRead);
        // System.out.println("sql: "+sql);
        final DataRow dataRow = new DataRow();
        jdbcTemplateR.query(sql, new RowCallbackHandler()
        { // editing
            public void processRow(ResultSet rs) throws SQLException
            {
                DataColumn dataColumn = null;
                String columnName;
                Object value;
                ResultSetMetaData rsmd = rs.getMetaData();
                List<DataColumn> col = new ArrayList<DataColumn>();

                for (int i = 1; i <= rsmd.getColumnCount(); i++)
                {
                    columnName = rsmd.getColumnName(i);
                    value = rs.getObject(columnName);
                    dataColumn = new DataColumn(columnName, value);
                    col.add(dataColumn);
                }
                dataRow.setDataColumn(col);
                return;
            }

        });

        return dataRow;

        // DataRow dataRow = null;
        // Connection connection = getConnectionS(properties);
        // try {
        // if (connection != null) {
        // Statement stmt = connection.createStatement();
        // ResultSet rs = stmt.executeQuery(sql);
        //
        // DataColumn dataColumn = null;
        // String columnName;
        // Object value;
        //
        // if (rs.next()) {
        // ResultSetMetaData rsmd = rs.getMetaData();
        // List<DataColumn> col = new ArrayList<DataColumn>();
        //
        // for (int i = 1; i <= rsmd.getColumnCount(); i++) {
        // columnName = rsmd.getColumnName(i);
        // value = rs.getObject(columnName);
        // dataColumn = new DataColumn(columnName, value);
        // col.add(dataColumn);
        // }
        // dataRow = new DataRow(col);
        // }
        // }
        // } finally {
        // if (connection != null) {
        // connection.close();
        // connection = null;
        // }
        // }

        // return null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void runProcedure(final String teaserID, final String receiverID, final String senderUserID, final BigDecimal finalScore, final String report) throws Exception
    {
        DataSourceHolder.setCustomerType(DataSourceConstant.mssqlWrite);
        String sql = "{call NotificationInsert(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        jdbcTemplateW.execute(sql, new CallableStatementCallback()
        {
            public Object doInCallableStatement(CallableStatement c) throws SQLException, DataAccessException
            {
                c.setString(1, UUID.randomUUID().toString());
                c.setString(2, teaserID);
                c.setString(3, receiverID);
                c.setInt(4, 0);
                c.setBoolean(5, false);
                c.setString(6, senderUserID);
                c.setInt(7, 0);
                c.setBigDecimal(8, finalScore);
                c.setString(9, report);
                c.execute();
                return null;
            }
        });
    }
}
