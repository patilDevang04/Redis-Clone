package org.redis.protocol;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.stream.IntStream;
import org.redis.Pair;
import org.redis.exception.EndOfStreamException;

public class ProtocolDeserializer {
    
    

    public Pair<String, Long> parseInput(DataInputStream inputStream) {
        try {
            char c = (char) readByteWithDebug(inputStream);

            var parsedResult = switch (c) {
                case '*' -> parseArray(inputStream);
                case '$' -> parseBulkString(inputStream);
                case '+' -> parseSimpleString(inputStream);
                default -> throw new RuntimeException("Unknown character: " + c);
            };
            return new Pair<>(parsedResult.left.trim(), parsedResult.right + 1);
        } catch (EOFException e) {
            throw new EndOfStreamException();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
    }

    private Pair<String, Long> parseArray(DataInputStream inputStream) throws IOException {
        Pair<Integer, Long> parsedResult = parseDigits(inputStream);
        return IntStream.range(0, parsedResult.left)
                .mapToObj(i -> parseInput(inputStream))
                .reduce(new Pair<>("", parsedResult.right),
                        (pair1, pair2) -> new Pair<>(
                                pair1.left + " " + pair2.left,
                                pair1.right + pair2.right));
    }

    private Pair<String, Long> parseBulkString(DataInputStream inputStream) throws IOException {
        Pair<Integer, Long> parsedResult = parseDigits(inputStream);
        long bytesCounter = parsedResult.right;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < parsedResult.left; i++) {
            stringBuilder.append((char) readByteWithDebug(inputStream));
            bytesCounter++;
        }
        
        readByteWithDebug(inputStream); // skip terminating '\r'
        readByteWithDebug(inputStream); // skip terminating '\n'
        bytesCounter += 2;
        return new Pair<>(stringBuilder.toString(), bytesCounter);
    }

    private Pair<String, Long> parseSimpleString(DataInputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        long bytesCount = 0L;
        byte b = readByteWithDebug(inputStream);
        bytesCount++;
        while (b != 13) { // 13 means '\r'
            buffer.write(b);
            b = readByteWithDebug(inputStream);
            bytesCount++;
        }
        
        readByteWithDebug(inputStream);
        bytesCount++;

        return new Pair<>(buffer.toString(), bytesCount);
    }

    private Pair<Integer, Long> parseDigits(DataInputStream inputStream) throws IOException {
        Pair<String, Long> parsedResult = parseSimpleString(inputStream);
        return new Pair<>(Integer.parseInt(parsedResult.left), parsedResult.right);
    }

    private byte readByteWithDebug(DataInputStream inputStream) throws IOException {
        byte b = inputStream.readByte();
        System.out.println(Thread.currentThread().getName() + ": Got byte: " + b);
        return b;
    }
}


