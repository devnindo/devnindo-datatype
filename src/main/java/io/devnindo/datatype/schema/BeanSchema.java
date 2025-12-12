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
import io.devnindo.datatype.schema.typeresolver.TypeResolverFactory;
import io.devnindo.datatype.schema.typeresolver.TypeResolver;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.ObjViolation;
import io.devnindo.datatype.validation.Violation;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class BeanSchema<T extends DataBean> {

    private static final Map<String, BeanSchema> SCHEMA_MAP;
    private static final Map<String, SchemaField> FIELD_MAP;

    static {
        SCHEMA_MAP = new HashMap<>();
        FIELD_MAP = new HashMap<>();
    }

    protected static final void regSchema(BeanSchema beanSchema, SchemaField... fieldArr){
        String clzName = beanSchema.getClass().getName();
        SCHEMA_MAP.put(clzName, beanSchema);
        for(SchemaField f : fieldArr){
            FIELD_MAP.put(clzName+"."+f.name, f);
        }
    }

    protected static final BeanSchema getSchema(String clzName){
        return SCHEMA_MAP.get(clzName);
    }

    protected static final void regField(String dataClzName,  SchemaField field){
        FIELD_MAP.put(dataClzName+"."+field.name, field);
    }

    protected static final SchemaField getField(String dataClzName, String name){
        return FIELD_MAP.get(dataClzName+"."+name);
    }

    @Deprecated
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




    protected static final <T extends DataBean> ObjViolation newViolation(Class<T> beanClz) {

        return new ObjViolation("SCHEMA::" + beanClz.getSimpleName());
    }

    protected static final <D extends DataBean, VAL> SchemaField<D, VAL>
    plainField(String name$, Function<D, VAL> getter$, Class<VAL> typeClz$, boolean required$) {
        TypeResolver resolverIF = TypeResolverFactory.plain(typeClz$);
        return new SchemaField<>(name$, getter$, resolverIF, required$);
    }

    protected static final <D extends DataBean, VAL> SchemaField<D, List<VAL>>
    plainListField(String name$, Function<D, List<VAL>> getter$, Class<VAL> typeClz$, boolean required$) {
        TypeResolver resolverIF = TypeResolverFactory.plainDataList(typeClz$);
        return new SchemaField<>(name$, getter$, resolverIF, required$);
    }

    protected static final <D extends DataBean, VAL extends DataBean> SchemaField<D, VAL>
    beanField(String name$, Function<D, VAL> getter$, Class<VAL> typeClz$, boolean required$) {
        TypeResolver resolverIF = TypeResolverFactory.beanType(typeClz$);
        return new SchemaField<>(name$, getter$, resolverIF, required$);
    }

    protected static final <D extends DataBean, VAL extends DataBean> SchemaField<D, List<VAL>>
    beanListField(String name$, Function<D, List<VAL>> getter$, Class<VAL> typeClz$, boolean required$) {
        TypeResolver resolverIF = TypeResolverFactory.beanList(typeClz$);
        return new SchemaField<>(name$, getter$, resolverIF, required$);
    }

    protected static final <D extends DataBean, VAL extends Enum<VAL>> SchemaField<D, VAL>
    enumField(String name$, Function<D, VAL> getter$, Class<VAL> enumType$, boolean required$) {
        TypeResolver resolverIF = TypeResolverFactory.enumType(enumType$);
        return new SchemaField<>(name$, getter$, resolverIF, required$);
    }



    public abstract JsonObject toJsonObj(T dataBean$);

    public abstract String toJsonStr(T dataBean$);


    // kept for backward compatibility
    public abstract Either<Violation, T> fromJsonObj(JsonObject js);

    //json to object
    public abstract Either<Violation, T> fromJsonStr(String reqObj);



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

