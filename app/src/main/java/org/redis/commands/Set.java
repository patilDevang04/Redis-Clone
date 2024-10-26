package org.redis.commands;

import org.redis.config.ObjectFactory;
import org.redis.store.KeyValueStore;

public class Set extends AbstractHandler{
    public Set(ObjectFactory objectFactory) {
        super(objectFactory);
    }

    @Override
    public byte[] handle(String[] arguments) {
        if (arguments.length > 3) {
            String parameter = arguments[3].toLowerCase();
            switch (parameter) {
                case "px":
                    Long expiration = Long.parseLong(arguments[4]);
                    KeyValueStore.put(arguments[1], arguments[2], expiration);
                    break;
                default:
                    throw new RuntimeException("Unknown parameter: " + parameter);
            }
        } else {
            KeyValueStore.put(arguments[1], arguments[2]);
        }
        return protocolSerializer().simpleString("OK");
    }
}
