package org.redis.commands;

import org.redis.config.ObjectFactory;
import org.redis.protocol.ProtocolSerializer;

public abstract class AbstractHandler implements Handler{
    protected ObjectFactory objectFactory;

    protected AbstractHandler(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    protected ProtocolSerializer protocolSerializer() {
        return objectFactory.getProtocolSerializer();
    }
}
