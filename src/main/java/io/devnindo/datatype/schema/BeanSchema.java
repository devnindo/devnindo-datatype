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
import io.devnindo.datatype.schema.field.*;
import io.devnindo.datatype.schema.typeresolver.ResolverFactory;
import io.devnindo.datatype.schema.typeresolver.TypeResolverIF;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class BeanSchema<D extends DataBean> implements TypeResolverIF<Iterable<Map.Entry<String, Object>>, D> {

    public static <D extends DataBean> BeanSchema<D> of(Class<D> modelClz$) {
        TypeResolverIF schemaIF = ResolverFactory.get(modelClz$);
        // both null & type check
        if (schemaIF instanceof BeanSchema == false)
            throw new IllegalStateException("No schema found for DataBean: " + modelClz$.getName());

        return (BeanSchema<D>) schemaIF;
    }





    public abstract Map<String, SchemaField> fieldMap();
    public abstract Supplier<D> newBean();

    public abstract JsonObject toJsonObj(D dataBean$);

    public abstract String encodeStr(D dataBean$);

    // kept for backward compatibility
    public abstract Either<Violation, D> fromJsonObj(JsonObject js);


    public abstract DataDiff<D> diff(D from$, D to$);


    public static final <D extends DataBean, VAL> SchemaField<D, VAL>
    plainField(String name$, Class<VAL> typeClz$, Function<D, VAL> getter$, BiConsumer<D, VAL> setter$) {
        return new PlainField<>(name$, typeClz$, getter$, setter$);
    }

    public static final <D extends DataBean, VAL> SchemaField<D, List<VAL>>
    plainListField(String name$, Class<VAL> typeClz$, Function<D, List<VAL>> getter$, BiConsumer<D, List<VAL>> setter$) {
         return new ListField<>(name$, typeClz$, getter$, setter$);
    }

    public static final <D extends DataBean, VAL extends DataBean> SchemaField<D, VAL>
    beanField(String name$, Class<D> typeClz$, Function<D, VAL> getter$, BiConsumer<D, VAL> setter$) {
         return new BeanField<>(name$, typeClz$, getter$, setter$);
    }


    public static final <D extends DataBean, VAL extends Enum<VAL>> SchemaField<D, VAL>
    enumField(String name$, Class<VAL> enumType$, Function<D, VAL> getter, BiConsumer<D, VAL> setter) {
        return new EnumField<>(name$, enumType$, getter, setter);
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

