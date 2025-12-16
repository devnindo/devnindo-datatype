package io.devnindo.datatype.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;

public class JsonBag {
    public static final JsonFactory factory = new JsonFactory();

    static {
        // Non-standard JSON but we allow C style comments in our JSON
        factory.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        factory.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }
}
