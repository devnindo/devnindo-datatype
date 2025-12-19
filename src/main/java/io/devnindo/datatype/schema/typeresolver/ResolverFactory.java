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

import java.util.IdentityHashMap;
import java.util.Map;


public final class ResolverFactory {

    private static final Map<Class, TypeResolver> resolverMap = new IdentityHashMap<>(6){
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
    private ResolverFactory(){}

    public static final TypeResolver resolver(Class type){
        return resolverMap.get(type);
    }

}

