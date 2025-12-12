package io.devnindo.datatype.json.jackson;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import io.devnindo.datatype.json.DecodeException;

import java.io.IOException;

public class LazyCodec {

    private static final JsonFactory jacksonFactory = new JsonFactory();

    static {
        // Non-standard JSON but we allow C style comments in our JSON
        jacksonFactory.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        jacksonFactory.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    public static JsonParser createParser(String str) {
        try {
            return jacksonFactory.createParser(str);
        } catch (IOException e) {
            throw new DecodeException("Failed to decode:" + e.getMessage(), e);
        }
    }

    public static JsonParser createParser(byte[] byteData) {
        try {
            return jacksonFactory.createParser(byteData);
        } catch (IOException e) {
            throw new DecodeException("Failed to decode:" + e.getMessage(), e);
        }
    }

}
