package org.redis.commands;


import org.redis.config.ObjectFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.EnumMap;
import java.util.Map;

public class CommandFactory {
    private final Map<Command, Handler> commandHandlers = new EnumMap<>(Command.class);

    public CommandFactory(ObjectFactory objectFactory) throws NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {
        initHandlers(objectFactory);
    }

    public Handler getCommandHandler(String command) {
        Handler handler = commandHandlers.get(Command.valueOf(command));
        if (handler == null) {
            throw new RuntimeException("Unknown command: " + command);
        }
        return handler;
    }

    private void initHandlers(ObjectFactory objectFactory) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        for (Command command : Command.values()) {
            Constructor<? extends Handler> handlerConstructor = handlerConstructor = command.handler.getConstructor(ObjectFactory.class);
            Handler handler = handlerConstructor.newInstance(objectFactory);
            commandHandlers.put(command, handler);
        }
    }

    private enum Command {

        GET(Get.class),
        SET(Set.class),
        PING(Ping.class);
        private final Class<? extends Handler> handler;

        Command(Class<? extends Handler> handler) {
            this.handler = handler;
        }
    }
}