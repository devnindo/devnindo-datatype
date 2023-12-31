/*
 * Copyright 2023 devnindo
 *
 * Portions Copyright (c) 2011-2019 Contributors to the Eclipse Foundation
 *
 * The part of this software written by devnindo is licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * The part of this software derived from the project by the Contributors to the Eclipse Foundation
 * is available under the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
 * which is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 */
package io.devnindo.datatype.json.jackson;

import io.devnindo.datatype.json.*;
import com.fasterxml.jackson.core.*;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.devnindo.datatype.util.JsonUtil.*;
import static java.time.format.DateTimeFormatter.ISO_INSTANT;

public class JacksonCodec {

    private static final JsonFactory factory = new JsonFactory();

    static {
        // Non-standard JSON but we allow C style comments in our JSON
        factory.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        factory.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    /**
     * encode JsonObject or JsonArray to buffer
     */
  /*public Buffer encodeToBuffer(Object object, boolean pretty) throws EncodeException {
    ByteBuf buf = Unpooled.buffer();
    // There is no need to use a try with resources here as jackson
    // is a well-behaved and always calls the closes all streams in the
    // "finally" block bellow.
    ByteBufOutputStream out = new ByteBufOutputStream(buf);
    JsonGenerator generator = createGenerator(out, pretty);

    try {
      encodeJson0(object, generator);
      generator.flush();
      return Buffer.buffer(buf);
    } catch (IOException e) {
      throw new EncodeException(e.getMessage(), e);
    } finally {
      close(generator);
    }
  }*/
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

    private static JsonGenerator createGenerator(Writer out, boolean pretty) {
        try {
            JsonGenerator generator = factory.createGenerator(out);
            if (pretty) {
                generator.useDefaultPrettyPrinter();
            }
            return generator;
        } catch (IOException e) {
            throw new DecodeException("Failed to decode:" + e.getMessage(), e);
        }
    }

    private static JsonGenerator createGenerator(OutputStream out, boolean pretty) {
        try {
            JsonGenerator generator = factory.createGenerator(out);
            if (pretty) {
                generator.useDefaultPrettyPrinter();
            }
            return generator;
        } catch (IOException e) {
            throw new DecodeException("Failed to decode:" + e.getMessage(), e);
        }
    }

    public static <T> T fromParser(JsonParser parser, Class<T> type) throws DecodeException {
        Object res;
        JsonToken remaining;
        try {
            parser.nextToken();
            res = parseAny(parser);
            remaining = parser.nextToken();
        } catch (IOException e) {
            throw new DecodeException(e.getMessage(), e);
        } finally {
            close(parser);
        }
        if (remaining != null) {
            throw new DecodeException("Unexpected trailing token");
        }
        return cast(res, type);
    }

    private static Object parseAny(JsonParser parser) throws IOException, DecodeException {
        switch (parser.currentTokenId()) {
            case JsonTokenId.ID_START_OBJECT:
                return parseObject(parser);
            case JsonTokenId.ID_START_ARRAY:
                return parseArray(parser);
            case JsonTokenId.ID_STRING:
                return parser.getText();
            case JsonTokenId.ID_NUMBER_FLOAT:
            case JsonTokenId.ID_NUMBER_INT:
                return parser.getNumberValue();
            case JsonTokenId.ID_TRUE:
                return Boolean.TRUE;
            case JsonTokenId.ID_FALSE:
                return Boolean.FALSE;
            case JsonTokenId.ID_NULL:
                return null;
            default:
                throw new DecodeException("Unexpected token"/*, parser.getCurrentLocation()*/);
        }
    }

    private static Map<String, Object> parseObject(JsonParser parser) throws IOException {
        String key1 = parser.nextFieldName();
        if (key1 == null) {
            return new LinkedHashMap<>(2);
        }
        parser.nextToken();
        Object value1 = parseAny(parser);
        String key2 = parser.nextFieldName();
        if (key2 == null) {
            LinkedHashMap<String, Object> obj = new LinkedHashMap<>(2);
            obj.put(key1, value1);
            return obj;
        }
        parser.nextToken();
        Object value2 = parseAny(parser);
        String key = parser.nextFieldName();
        if (key == null) {
            LinkedHashMap<String, Object> obj = new LinkedHashMap<>(2);
            obj.put(key1, value1);
            obj.put(key2, value2);
            return obj;
        }
        // General case
        LinkedHashMap<String, Object> obj = new LinkedHashMap<>();
        obj.put(key1, value1);
        obj.put(key2, value2);
        do {
            parser.nextToken();
            Object value = parseAny(parser);
            obj.put(key, value);
            key = parser.nextFieldName();
        } while (key != null);
        return obj;
    }

    private static List<Object> parseArray(JsonParser parser) throws IOException {
        List<Object> array = new ArrayList<>();
        while (true) {
            parser.nextToken();
            int tokenId = parser.currentTokenId();
            if (tokenId == JsonTokenId.ID_FIELD_NAME) {
                throw new UnsupportedOperationException();
            } else if (tokenId == JsonTokenId.ID_END_ARRAY) {
                return array;
            }
            Object value = parseAny(parser);
            array.add(value);
        }
    }

    static void close(Closeable parser) {
        try {
            parser.close();
        } catch (IOException ignore) {
        }
    }

    // In recursive calls, the callee is in charge of opening and closing the data structure
    private static void encodeJson0(Object json, JsonGenerator generator) throws EncodeException {
        try {
            if (json instanceof JsonObject) {
                json = ((JsonObject) json).getMap();
            } else if (json instanceof JsonArray) {
                json = ((JsonArray) json).getList();
            }
            if (json instanceof Map) {
                generator.writeStartObject();
                for (Map.Entry<String, ?> e : ((Map<String, ?>) json).entrySet()) {
                    generator.writeFieldName(e.getKey());
                    encodeJson0(e.getValue(), generator);
                }
                generator.writeEndObject();
            } else if (json instanceof List) {
                generator.writeStartArray();
                for (Object item : (List<?>) json) {
                    encodeJson0(item, generator);
                }
                generator.writeEndArray();
            } else if (json instanceof String) {
                generator.writeString((String) json);
            } else if (json instanceof Number) {
                if (json instanceof Short) {
                    generator.writeNumber((Short) json);
                } else if (json instanceof Integer) {
                    generator.writeNumber((Integer) json);
                } else if (json instanceof Long) {
                    generator.writeNumber((Long) json);
                } else if (json instanceof Float) {
                    generator.writeNumber((Float) json);
                } else if (json instanceof Double) {
                    generator.writeNumber((Double) json);
                } else if (json instanceof Byte) {
                    generator.writeNumber((Byte) json);
                } else if (json instanceof BigInteger) {
                    generator.writeNumber((BigInteger) json);
                } else if (json instanceof BigDecimal) {
                    generator.writeNumber((BigDecimal) json);
                } else {
                    generator.writeNumber(((Number) json).doubleValue());
                }
            } else if (json instanceof Boolean) {
                generator.writeBoolean((Boolean) json);
            } else if (json instanceof Instant) {
                // RFC-7493
                generator.writeString((ISO_INSTANT.format((Instant) json)));
            } else if (json instanceof byte[]) {
                // RFC-7493
                generator.writeString(BASE64_ENCODER.encodeToString((byte[]) json));
            } else if (json instanceof Enum) {
                // vert.x extra (non standard but allowed conversion)
                generator.writeString(((Enum<?>) json).name());
            } else if (json instanceof Jsonable) // to support encoding of DataBean and other Jsonable
            {
                encodeJson0(Jsonable.class.cast(json).toJson(), generator);
            } else if (json == null) {
                generator.writeNull();
            } else {
                throw new EncodeException("Mapping " + json.getClass().getName() + "  is not available without Jackson Databind on the classpath");
            }
        } catch (IOException e) {
            throw new EncodeException(e.getMessage(), e);
        }
    }

    private static <T> T cast(Object o, Class<T> clazz) {
        if (o instanceof Map) {
            if (!clazz.isAssignableFrom(Map.class)) {
                throw new DecodeException("Failed to decode");
            }
            if (clazz == Object.class) {
                o = new JsonObject((Map) o);
            }
            return clazz.cast(o);
        } else if (o instanceof List) {
            if (!clazz.isAssignableFrom(List.class)) {
                throw new DecodeException("Failed to decode");
            }
            if (clazz == Object.class) {
                o = new JsonArray((List) o);
            }
            return clazz.cast(o);
        } else if (o instanceof String) {
            String str = (String) o;
            if (clazz.isEnum()) {
                o = Enum.valueOf((Class<Enum>) clazz, str);
            } else if (clazz == byte[].class) {
                o = BASE64_DECODER.decode(str);
            } else if (clazz == Instant.class) {
                o = Instant.from(ISO_INSTANT.parse(str));
            } else if (!clazz.isAssignableFrom(String.class)) {
                throw new DecodeException("Failed to decode");
            }
            return clazz.cast(o);
        } else if (o instanceof Boolean) {
            if (!clazz.isAssignableFrom(Boolean.class)) {
                throw new DecodeException("Failed to decode");
            }
            return clazz.cast(o);
        } else if (o == null) {
            return null;
        } else {
            Number number = (Number) o;
            if (clazz == Integer.class) {
                o = number.intValue();
            } else if (clazz == Long.class) {
                o = number.longValue();
            } else if (clazz == Float.class) {
                o = number.floatValue();
            } else if (clazz == Double.class) {
                o = number.doubleValue();
            } else if (clazz == Byte.class) {
                o = number.byteValue();
            } else if (clazz == Short.class) {
                o = number.shortValue();
            } else if (clazz == Object.class || clazz.isAssignableFrom(Number.class)) {
                // Nothing
            } else {
                throw new DecodeException("Failed to decode");
            }
            return clazz.cast(o);
        }
    }

    public <T> T fromString(String json, Class<T> clazz) throws DecodeException {
        return fromParser(createParser(json), clazz);
    }

    public <T> T fromByteData(byte[] byteData, Class<T> clazz) throws DecodeException {
        return fromParser(createParser(byteData), clazz);
    }

    public String encodeToString(Object object, boolean pretty) throws EncodeException {
        StringWriter sw = new StringWriter();
        JsonGenerator generator = createGenerator(sw, pretty);
        try {
            encodeJson0(object, generator);
            generator.flush();
            return sw.toString();
        } catch (IOException e) {
            throw new EncodeException(e.getMessage(), e);
        } finally {
            close(generator);
        }
    }


}
