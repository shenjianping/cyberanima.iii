package org.cyberanima.iii.mas.perception.processor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.cyberanima.iii.common.jms.*;
import org.cyberanima.iii.mas.perception.jms.*;

@Service(value = "perceptionMessageProcessor")
public class PerceptionMessageProcessor implements MessageProcesser
{

    protected transient final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private PerceptionMessageProducer perceptionMessageProducer;

    @SuppressWarnings("unchecked")
    @Override
    public void process(Message message)
    {
        logger.info("Perception gets message: " + message.toString());
        perceptionMessageProducer.sendMsg(MessageFactory.createMessage(MessageFactory.MessageType.userLogin));
        System.out.println("process message:" + message);
    }
}
