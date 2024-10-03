package org.redis.store;

import java.time.Instant;

public record StorageRecord(ValueType valueType, Object value, Instant expiration) {
}