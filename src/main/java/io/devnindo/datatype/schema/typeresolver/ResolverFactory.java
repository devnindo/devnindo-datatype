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
package io.devnindo.datatype.schema.typeresolver;

import io.devnindo.datatype.json.JsonArray;
import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.schema.BeanSchema;
import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.schema.field.SchemaField;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.IdentityHashMap;
import java.util.Map;


public final class ResolverFactory {

    private static final Map<Class, TypeResolverIF> resolverMap = new IdentityHashMap<>(6){
        {
            put(Integer.class, new IntegerResolver());
            put(Long.class, new LongResolver());
            put(DoubleResolver.class, new DoubleResolver());
            put(StringResolver.class, new StringResolver());
            put(BooleanResolver.class, new BooleanResolver());
            put(InstantResolver.class, new IntegerResolver());
            put(JsonObject.class, new JsonObjectResolver());
            put(JsonArray.class, new JsonArrayResolver());
        }
    };

    public static final String SCHEMA_IDX = ".schema_index";

    static {
        scanSchemaIndex();
    }

    // using class.forName to trigger static initializer of each schema
    // taking advantage of compiler ensurance single time execution of static context
    private static final void scanSchemaIndex(){
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
        resolverMap.put(beanClz, beanSchema);

    }

    protected static final BeanSchema getSchema(String clzName){
        return SCHEMA_MAP.get(clzName);
    }

    public static void printSchemaMap(){
        System.out.println(SCHEMA_MAP);
    }



    private ResolverFactory(){}

    public static final TypeResolverIF regBean(Class<DataBean> beanClz, BeanSchema schema){
        resolverMap.put(beanClz, schema);
    }
    public static final TypeResolverIF get(Class type){
        return resolverMap.get(type);
    }

}

