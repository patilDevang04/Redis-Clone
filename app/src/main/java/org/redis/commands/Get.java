package org.redis.commands;

import org.redis.config.ObjectFactory;
import org.redis.store.StorageRecord;
import org.redis.store.KeyValueStore;
import org.redis.store.ValueType;

import java.util.Optional;

public class Get extends AbstractHandler {

    public Get(ObjectFactory objectFactory) {
        super(objectFactory);
    }

    @Override
    public byte[] handle(String[] arguments) {
        String value;

        StorageRecord storageRecord = KeyValueStore.get(arguments[1]);
        value = Optional.ofNullable(storageRecord)
                    .filter(r -> r.valueType() == ValueType.STRING)
                    .map(StorageRecord::value)
                    .map(Object::toString)
                    .orElse(null);

        return protocolSerializer().bulkString(value);
    }
}
