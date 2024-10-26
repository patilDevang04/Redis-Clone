package org.redis.commands;

public interface Handler {
    byte[] handle(String[] arguments);
}
