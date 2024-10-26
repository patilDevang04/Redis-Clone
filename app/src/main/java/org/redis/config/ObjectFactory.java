package org.redis.config;

import org.redis.commands.CommandFactory;
import org.redis.protocol.ProtocolDeserializer;
import org.redis.protocol.ProtocolSerializer;

import java.lang.reflect.InvocationTargetException;

public class ObjectFactory {

    private ProtocolDeserializer protocolDeserializer;
    private ProtocolSerializer protocolSerializer;
    private CommandFactory commandFactory;


    public ObjectFactory() throws InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {

        init();
    }

    public ProtocolDeserializer getProtocolDeserializer() {
        return protocolDeserializer;
    }

    public ProtocolSerializer getProtocolSerializer() {
        return protocolSerializer;
    }

    public CommandFactory getCommandFactory() {
        return commandFactory;
    }


    private void init() throws InvocationTargetException, NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException {
        protocolDeserializer = new ProtocolDeserializer();
        protocolSerializer = new ProtocolSerializer();
        commandFactory = new CommandFactory(this);
    }
}