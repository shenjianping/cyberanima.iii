package org.cyberanima.iii.mas.perception.jms;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.jms.DeliveryMode;
import org.cyberanima.iii.common.jms.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.JmsTemplate;

import com.zwlsoft.utils.ConfigHelper;

public class PerceptionMessageProducer
{
    private static final Log log = LogFactory.getLog(PerceptionMessageProducer.class);

    private JmsTemplate template;
    private String destination;

    private static String[] allNodes;
    private static String[] autoNodes;
    private static String localNode;

    static
    {
        Properties prop = new Properties();
        InputStream inputStream = ConfigHelper.class.getClassLoader().getResourceAsStream("conf/jms.properties");

        try
        {
            prop.load(inputStream);
            localNode = prop.getProperty("jms_send_node");
            allNodes = prop.getProperty("jms_allNodes").split(",");
            autoNodes = prop.getProperty("jms_autoNodes").split(",");

            System.out.println("localNode: " + localNode);

            inputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Send message to local node
     */
    public void sendMsg(Message msg)
    {
        System.out.println("template: " + template);
        String fullDest = destination + '.' + localNode;
        log.info(fullDest + " # " + msg);
        int mode = template.getDeliveryMode();
        System.out.println("mode: " + mode + " , DeliveryMode.PERSISTENT: " + DeliveryMode.PERSISTENT);
        template.convertAndSend(fullDest, msg);
    }

    /**
     * Send message to local node
     */
    public void sendMsg(Message msg, String nodeName)
    {
        System.out.println("template: " + template);
        String fullDest = destination + '.' + nodeName;
        log.info(fullDest + " # " + msg);
        int mode = template.getDeliveryMode();
        System.out.println("mode: " + mode + " , DeliveryMode.PERSISTENT: " + DeliveryMode.PERSISTENT);
        template.convertAndSend(fullDest, msg);
    }

    /**
     * Broadcast message to all nodes
     */
    public void broadcastMsg(Message msg)
    {
        log.info(StringUtils.join(allNodes, ',') + " # " + msg);
        for (String nodeStr : allNodes)
        {
            template.convertAndSend(nodeStr, msg);
        }
    }

    /**
     * Automatically resend all broadcasted message to particular node
     */
    public void broadAutoServerMsg(Message msg)
    {
        log.info(StringUtils.join(autoNodes, ',') + " # " + msg);
        for (String nodeStr : autoNodes)
        {
            template.convertAndSend(nodeStr, msg);
        }
    }

    public void setTemplate(JmsTemplate template)
    {
        this.template = template;
    }

    public void setDestination(String destination)
    {
        this.destination = destination;
    }
}
