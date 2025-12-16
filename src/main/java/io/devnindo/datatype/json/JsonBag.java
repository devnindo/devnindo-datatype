package io.devnindo.datatype.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;

public class JsonBag {
    public static final JsonFactory factory = new JsonFactory();

    static {
        // Non-standard JSON but we allow C style comments in our JSON
        factory.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        factory.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    public static JsonParser createParser(String str) {
        try {
            return factory.createParser(str);
        } catch (IOException e) {
            throw new DecodeException("Failed to decode:" + e.getMessage(), e);
        }
    }

    public static JsonParser createParser(byte[] byteData) {
        try {
            return factory.createParser(byteData);
        } catch (IOException e) {
            throw new DecodeException("Failed to decode:" + e.getMessage(), e);
        }
    }
}
