package org.cyberanima.iii.common.jms;

import java.util.HashMap;
import java.util.Map;

/**
 * To protect the lost of message.
 * 
 * @author Alex Wang
 */
public class MessageProtector {
	private static MessageProtector instance;
	private Map<String, String[]> topicMap = new HashMap<String, String[]>();

	/**
	 * MAP XML. <virtualDestinations> <compositeTopic name="ActiveMQ.ORDER">
	 * <forwardTo> <queue physicalName="ActiveMQ.ORDER.back" /> <!--queue
	 * physicalName="ActiveMQ.ORDER.cash" /--> <queue
	 * physicalName="ActiveMQ.ORDER.order" /> <!--queue
	 * physicalName="ActiveMQ.ORDER.passport" /--> </forwardTo>
	 * </compositeTopic> <compositeTopic name="ActiveMQ.PASSPORT"> <forwardTo>
	 * <queue physicalName="ActiveMQ.PASSPORT.back" /> <queue
	 * physicalName="ActiveMQ.PASSPORT.order" /> <queue
	 * physicalName="ActiveMQ.PASSPORT.passport" /> </forwardTo>
	 * </compositeTopic> <compositeTopic name="ActiveMQ.PRODUCT"> <forwardTo>
	 * <queue physicalName="ActiveMQ.PRODUCT.back" /> <!--queue
	 * physicalName="ActiveMQ.PRODUCT.order" /--> </forwardTo> </compositeTopic>
	 * <compositeTopic name="ActiveMQ.POLICY"> <forwardTo> <queue
	 * physicalName="ActiveMQ.POLICY.back" /> <!--queue
	 * physicalName="ActiveMQ.POLICY.order" /--> </forwardTo> </compositeTopic>
	 * <!--compositeTopic name="ActiveMQ.SMS"> <forwardTo> <queue
	 * physicalName="ActiveMQ.PASSPORT.back" /> <queue
	 * physicalName="ActiveMQ.PASSPORT.order" /> </forwardTo>
	 * </compositeTopic--> <compositeTopic name="ActiveMQ.SSO"> <forwardTo>
	 * <queue physicalName="ActiveMQ.SSO.sso" /> <queue
	 * physicalName="ActiveMQ.SSO.clutter" /> </forwardTo> </compositeTopic>
	 * </virtualDestinations>
	 */
	private void init() {
//		String[] activeMqOrder = new String[] { "ActiveMQ.ORDER.back", "ActiveMQ.ORDER.order", "ActiveMQ.ORDER.payment", "ActiveMQ.ORDER.clutter", "ActiveMQ.ORDER.passport" };
//		topicMap.put("ActiveMQ.ORDER", activeMqOrder);
//
//		String[] activeMqPassport = new String[] { "ActiveMQ.PASSPORT.back", "ActiveMQ.PASSPORT.order", "ActiveMQ.PASSPORT.passport", "ActiveMQ.PASSPORT.ebk_push" };
//		topicMap.put("ActiveMQ.PASSPORT", activeMqPassport);
//
//		String[] activeMqPassportChimelong = new String[] { "ActiveMQ.PASSPORT.CHIMELONG.passport" };
//		topicMap.put("ActiveMQ.PASSPORT.CHIMELONG", activeMqPassportChimelong);
//
//		String[] activeMqProduct = new String[] { "ActiveMQ.PRODUCT.back", "ActiveMQ.PRODUCT.clutter" };
//		topicMap.put("ActiveMQ.PRODUCT", activeMqProduct);
//
//		String[] activeMqPolicy = new String[] { "ActiveMQ.POLICY.back" };
//		topicMap.put("ActiveMQ.POLICY", activeMqPolicy);
//
//		String[] activeMqSso = new String[] { "ActiveMQ.SSO.sso" };
//		topicMap.put("ActiveMQ.SSO", activeMqSso);
//
//		String[] activeMqResource = new String[] { "ActiveMQ.RESOURCE.job", "ActiveMQ.RESOURCE.payment", "ActiveMQ.RESOURCE.search1", "ActiveMQ.RESOURCE.search2" };
//		topicMap.put("ActiveMQ.RESOURCE", activeMqResource);
		
		String[] activeMqVstOrder = new String[] {"ActiveMQ.VST_ORDER.back","ActiveMQ.VST_ORDER.order"};
		topicMap.put("ActiveMQ.VST_ORDER", activeMqVstOrder);
		
		// Search the message
		String[] activeMqVstSearch = new String[] {"ActiveMQ.RESOURCE.vstSearch1","ActiveMQ.RESOURCE.vstSearch2","ActiveMQ.RESOURCE.vstSearch3","ActiveMQ.RESOURCE.vstSearch4","ActiveMQ.RESOURCE.vstSearch5","ActiveMQ.RESOURCE.vstSearch6"};
		topicMap.put("ActiveMQ.RESOURCE", activeMqVstSearch);
	}

	public static MessageProtector getInstance() {
		if (instance == null) {
			MessageProtector protector = new MessageProtector();
			protector.init();
			instance = protector;
		}
		return instance;
	}

	public String[] getQueues(String topic) {
		return topicMap.get(topic);
	}

}
