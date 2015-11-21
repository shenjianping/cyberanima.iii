package org.cyberanima.iii.common.jms;

import java.util.Map;

/**
 * Message Factory
 */
public class MessageFactory
{

    private MessageFactory()
    {

    }

    /**
     * Create message
     */
    public static Message createMessage(MessageType messageType)
    {
        Message msg = new Message(messageType.name());
        return msg;
    }

    /**
     * Create message
     */
    public static Message createMessage(MessageType messageType, Map<String, ? extends Object> paramMap)
    {
        Message msg = new Message(messageType.name(), paramMap);
        return msg;
    }

    /**
     * Create message
     */
    public static Message createMessage(MessageType messageType, Object object, Map<String, ? extends Object> paramMap)
    {
        Message msg = new Message(messageType.name(), object, paramMap);
        return msg;
    }

    /**
     * Create message
     */
    public static Message createMessage(MessageType messageType, Object object)
    {
        Message msg = new Message(messageType.name(), object);
        return msg;
    }

    /**
     * Create message
     */
    public static Message createMessageName(MessageType messageType)
    {
        Message msg = new Message(messageType.name());
        return msg;
    }

    /**
     * Message type
     */
    public static enum MessageType
    {
        userLogin("User Login", 1);

        private String name;
        private int index;

        MessageType(String name, int index)
        {
            this.name = name;
            this.index = index;
        }

        public String getName()
        {
            return name;
        }

        public int getIndex()
        {
            return index;
        }

    }
}
