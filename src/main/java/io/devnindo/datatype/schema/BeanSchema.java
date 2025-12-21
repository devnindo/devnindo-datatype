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

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.schema.field.PlainField;
import io.devnindo.datatype.schema.field.SchemaField;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class BeanSchema<T extends DataBean> {



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


    public abstract DataDiff<T> diff(T from$, T to$);




    public static final <D extends DataBean, VAL> SchemaField<D, VAL>
    plainField(String name$, Function<D, VAL> getter$, BiConsumer<D, VAL> setter$, Class<VAL> typeClz$, boolean required$) {
        return new PlainField<>(name$, typeClz$, getter$, setter$, required$);
    }

    public static final <D extends DataBean, VAL> SchemaField<D, List<VAL>>
    plainListField(String name$, Function<D, List<VAL>> getter$, BiConsumer<D, List<VAL>> setter$, Class<VAL> typeClz$, boolean required$) {
        TypeResolver resolverIF = TypeResolverFactory.plainDataList(typeClz$);
        return new SchemaField<>(name$, getter$, setter$, resolverIF, required$);
    }

    public static final <D extends DataBean, VAL extends DataBean> SchemaField<D, VAL>
    beanField(String name, Function<D, VAL> getter, BiConsumer<D, VAL> setter, Class<VAL> typeClz, boolean required) {
        TypeResolver resolverIF = TypeResolverFactory.beanType(typeClz);
        return new SchemaField<>(name, getter, setter, resolverIF, required);
    }

    public static final <D extends DataBean, VAL extends DataBean> SchemaField<D, List<VAL>>
    beanListField(String name$, Function<D, List<VAL>> getter$, BiConsumer<D, List<VAL>> setter$, Class<VAL> typeClz$, boolean required$) {
        TypeResolver resolverIF = TypeResolverFactory.beanList(typeClz$);
        return new SchemaField<>(name$, getter$, setter$, resolverIF, required$);
    }

    public static final <D extends DataBean, VAL extends Enum<VAL>> SchemaField<D, VAL>
    enumField(String name$, Function<D, VAL> getter, BiConsumer<D, VAL> setter, Class<VAL> enumType$, boolean required$) {
        TypeResolver resolverIF = TypeResolverFactory.enumType(enumType$);
        return new SchemaField<>(name$, getter, setter, resolverIF, required$);
    }

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

