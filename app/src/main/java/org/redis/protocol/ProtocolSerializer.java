package org.redis.protocol;

import java.util.ArrayList;
import java.util.List;

public class ProtocolSerializer {
    private static final String CRLF_TERMINATOR = "\r\n";

    public byte[] simpleString(String value) {
        return ("+" + value + CRLF_TERMINATOR).getBytes();
    }

    public byte[] simpleError(String value) {
        return ("-" + value + CRLF_TERMINATOR).getBytes();
    }

    public byte[] bulkString(String value) {
        if (value == null) {
            return ("$-1" + CRLF_TERMINATOR).getBytes();
        }
        return ("$" + value.length() + CRLF_TERMINATOR + value + CRLF_TERMINATOR).getBytes();
    }

    public byte[] array(List<?> values) {
        byte[] response = ("*" + values.size() + CRLF_TERMINATOR).getBytes();
        List<Byte> responseList = new ArrayList<>();
        for (byte b : response) {
            responseList.add(b);
        }


        List<byte[]> serializedElements = new ArrayList<>();
        for (Object value : values) {
            if (value instanceof String stringValue) {
                serializedElements.add(bulkString(stringValue));
            } else if (value instanceof List<?> listValue) {
                serializedElements.add(array(listValue)); // Recursive call for nested list
            } else {
                throw new IllegalArgumentException("Unsupported type: " + value.getClass());
            }
        }


        for (byte[] element : serializedElements) {
            for (byte b : element) {
                responseList.add(b);
            }
        }


        byte[] finalResponse = new byte[responseList.size()];
        for (int i = 0; i < responseList.size(); i++) {
            finalResponse[i] = responseList.get(i);
        }

        return finalResponse;
    }

    public byte[] arrayOfSerialized(List<byte[]> elements) {

        byte[] header = ("*" + elements.size() + CRLF_TERMINATOR).getBytes();
        List<Byte> responseList = new ArrayList<>();

        // Add header bytes to responseList
        for (byte b : header) {
            responseList.add(b);
        }


        elements.forEach(element -> {
            for (byte b : element) {
                responseList.add(b);
            }
        });


        byte[] finalResponse = new byte[responseList.size()];
        for (int i = 0; i < responseList.size(); i++) {
            finalResponse[i] = responseList.get(i);
        }

        return finalResponse;
    }

    public byte[] integer(Long value) {
        return (":" + value + CRLF_TERMINATOR).getBytes();
    }
}
