package org.cyberanima.iii.common.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class DataRow implements Serializable {
	/**
     * 
     */
    private static final long serialVersionUID = 3426558338986570602L;
    public List<DataColumn> col;

	public List<DataColumn> getDataColumn()
	{
	    return col;
	}
	
	public void setDataColumn(List<DataColumn> col)
	{
	    this.col=col;
	}
	
	public DataRow()
    {
        // TODO Auto-generated constructor stub
    }
	
	public DataRow(List<DataColumn> _col) {
		col = _col;
	}

	public String getColumnString(String colName) {
		for (DataColumn c : col) {
			if (c.getKey().equals(colName)) {
				if (c.getValue() != null) {
					return c.getValue().toString();
				}
			}
		}
		return null;
	}

	public Integer getColumnInteger(String colName) {
		for (DataColumn c : col) {
			if (c.getKey().equals(colName)) {
				if (c.getValue() != null) {
					try {
						return Integer.valueOf(c.getValue().toString());
					} catch (Exception e) {
						return null;
					}
				}
			}
		}
		return null;
	}

	public BigDecimal getColumnBigDecimal(String colName) {
		for (DataColumn c : col) {
			if (c.getKey().equals(colName)) {
				if (c.getValue() != null) {
					return new BigDecimal(c.getValue().toString());
				}
			}
		}
		return null;
	}

	public Double getColumnDouble(String colName) {
		for (DataColumn c : col) {
			if (c.getKey().equals(colName)) {
				if (c.getValue() != null) {
					return Double.valueOf(c.getValue().toString());
				}
			}
		}
		return null;
	}
}