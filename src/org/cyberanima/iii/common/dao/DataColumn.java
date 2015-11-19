package org.cyberanima.iii.common.dao;

import java.io.Serializable;

public class DataColumn implements Serializable {
	/**
     * 
     */
    private static final long serialVersionUID = -1906126053693298743L;
    String key;
	Object value;

	public DataColumn(String _key, Object _value) {
		key = _key;
		value = _value;
	}

	public String getKey() {
		return key;
	}

	public Object getValue() {
		return value;
	}

	public void setKey(String _key) {
		key = _key;
	}

	public void setValue(Object _value) {
		value = _value;
	}
}