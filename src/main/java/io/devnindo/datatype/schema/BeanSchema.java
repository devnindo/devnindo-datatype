/*
 * Copyright 2023 devnindo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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
 */
package io.devnindo.datatype.schema;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import io.devnindo.datatype.json.DefaultCodec;
import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.schema.typeresolver.TypeResolverFactory;
import io.devnindo.datatype.schema.typeresolver.TypeResolver;
import io.devnindo.datatype.util.ClzUtil;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.ObjViolation;
import io.devnindo.datatype.validation.Violation;

import javax.annotation.processing.Generated;
import javax.xml.crypto.Data;
import javax.xml.validation.Schema;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class BeanSchema<T extends DataBean> {

    private static final Map<String, BeanSchema> SCHEMA_MAP;
    private static final Map<String, SchemaField> FIELD_MAP;
    public static final String SCHEMA_IDX = ".schema_index";

    static {
        SCHEMA_MAP = new HashMap<>();
        FIELD_MAP = new HashMap<>();
        scanIndex();
    }

    // using class.forName to trigger static initializer of each schema
    // taking advantage of compiler ensurance single time execution of static context
    private static final void scanIndex(){
        try (InputStream inputStream = BeanSchema.class.getResourceAsStream("/" + SCHEMA_IDX);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            if (inputStream == null) {
                System.out.println("/resource/" + SCHEMA_IDX+" not found");
            } else {
                reader.lines().forEach(str -> {
                try {
                    Class.forName(str);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static final void regSchema(Class<? extends DataBean> beanClz, BeanSchema beanSchema, SchemaField... fieldArr){
        String clzName = beanSchema.getClass().getName();
        //System.out.println(beanSchema.getClass().getA);
        SCHEMA_MAP.put(beanClz.getName(), beanSchema);
        for(SchemaField f : fieldArr){
            FIELD_MAP.put(clzName+"."+f.name, f);
        }
    }

    protected static final BeanSchema getSchema(String clzName){
        return SCHEMA_MAP.get(clzName);
    }

    protected static final SchemaField getField(String dataClzName, String name){
        return FIELD_MAP.get(dataClzName+"."+name);
    }

    public static void printSchemaMap(){
        System.out.println(SCHEMA_MAP);
    }

    public static <D extends DataBean> BeanSchema<D> of(Class<D> modelClz$) {
        BeanSchema schema = SCHEMA_MAP.get(modelClz$.getName());
        if (schema == null)
            throw new IllegalStateException("No schema found for DataBean: " + modelClz$.getName());

        return schema;
    }

    /**
     * @param clzName fully qualified name of the target DataBean
     * */
    public static <D extends DataBean> BeanSchema<D> of(String clzName) {
        BeanSchema schema = SCHEMA_MAP.get(clzName);
        if (schema == null)
            throw new IllegalStateException("No schema found for DataBean: " + clzName);

        return schema;
    }



    public abstract Map<String, SchemaField> fieldMap();
    public abstract Supplier<T> newBean();

    public abstract JsonObject toJsonObj(T dataBean$);

    public abstract String toJsonStr(T dataBean$);



    // kept for backward compatibility
    public abstract Either<Violation, T> fromJsonObj(JsonObject js);

    //json to object
    public Either<Violation, T> fromJsonStr(String reqObj){

        JsonParser parser = DefaultCodec.createParser(reqObj);
        T bean = newBean().get();
        Map<String, SchemaField> fmap = fieldMap();

        try{
            if (parser.nextToken() != JsonToken.START_OBJECT) {
                return Either.left(Violation.withCtx("VALID_JSON", "INVALID_JSON_STRING"));
            }

            while (parser.nextToken() != JsonToken.END_OBJECT) {
                // The current token should be the field name (key)
                if (parser.currentToken() == JsonToken.FIELD_NAME) {
                    String fieldName = parser.getCurrentName();
                    // Move to the next token, which is the value
                    Object val = parser.nextToken();

                }
            }


        } catch(IOException excp){
            return Either.left(Violation.of("VALID_JSON").withCtx("PARSE_ERROR", excp.getMessage()));
        }

        return Either.right(bean);
    }

    void  fromJsonObj(JsonParser parser, T t, String fieldName, Object fieldVal){
        SchemaField f = fieldMap().get(fieldName);
        // if field value is object type
        // find schema:

    }



    public abstract DataDiff<T> diff(T from$, T to$);

    protected final Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    @java.io.Serial
    private void readObject(ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        throw new InvalidObjectException("can't deserialize enum");
    }

    @java.io.Serial
    private void readObjectNoData() throws ObjectStreamException {
        throw new InvalidObjectException("can't deserialize enum");
    }

}

