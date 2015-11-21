package org.cyberanima.iii.common.jms;

import java.io.Serializable;
import java.util.Map;


public class Message implements Serializable {

	private static final long serialVersionUID = -1118236567096207803L;
	
	//Message Type
	private String type;
	 
	/**
	 * Message content object
	 */
	private Object object;
	
	private Map<String, ? extends Object > paramMap;

    public Message()
    {
        
    }
    
    

    public Message(String type, Map<String, ? extends Object > paramMap)
    {
        super();
        this.type = type;
        this.paramMap = paramMap;
    }



    public Message(String type, Object object)
    {
        super();
        this.type = type;
        this.object = object;
    }

    public Message(String type, Object object, Map<String, ? extends Object > paramMap)
    {
        super();
        this.type = type;
        this.object = object;
        this.paramMap = paramMap;
    }



    public Message(String type)
    {
      this.type=type;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public Object getObject()
    {
        return object;
    }

    public void setObject(Object object)
    {
        this.object = object;
    }

    public Map<String, ? extends Object > getParamMap()
    {
        return paramMap;
    }

    public void setParamMap(Map<String, ? extends Object > paramMap)
    {
        this.paramMap = paramMap;
    }



    @Override
    public String toString()
    {
        return "Message [type=" + type + ", object=" + object + ", paramMap=" + paramMap + "]";
    }
    
}