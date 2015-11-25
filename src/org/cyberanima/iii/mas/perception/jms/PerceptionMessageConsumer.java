package org.cyberanima.iii.mas.perception.jms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.cyberanima.iii.mas.perception.processor.*;
import org.cyberanima.iii.common.jms.*;

public class PerceptionMessageConsumer {
	private static final Log log = LogFactory.getLog(PerceptionMessageConsumer.class);

//	private List<PayProcesser> processerList = new ArrayList<PayProcesser>();
	
	@Autowired
	private PerceptionMessageProcessor userLoginProcessor;
	
	private String destName;

	public void receive(Message message) {
		log.info(message + " -- " + destName);
		System.out.println("helloworldProcesser: "+ userLoginProcessor);
		System.out.println(message + " -- " + destName);
		userLoginProcessor.process(message);
	}

	public void setDestName(String destName) {
		this.destName = destName;
	}
}
