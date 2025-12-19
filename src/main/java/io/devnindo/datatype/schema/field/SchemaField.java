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
package io.devnindo.datatype.schema.field;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.schema.typeresolver.ResolverFactory;
import io.devnindo.datatype.schema.typeresolver.TypeResolverIF;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Validator;
import io.devnindo.datatype.validation.Violation;
import io.devnindo.datatype.validation.violations.LogicalViolations;
import jdk.jfr.Description;

import java.util.function.BiConsumer;
import java.util.function.Function;

public  abstract class SchemaField<D extends DataBean, VAL> {
    public final String name;
    public final Function<D, VAL> accessor;
    public final BiConsumer<D, VAL> setter;
    public final boolean required;
    public final Class dataType;
    public final boolean isList;

    public final Validator<VAL, VAL> validator;

    protected SchemaField(String name, boolean required,  Class type, boolean isList, Function<D, VAL> accessor, BiConsumer<D, VAL> setter) {
        this.name = name;
        this.required = required;
        this.dataType = type;
        this.isList = isList;
        this.accessor = accessor;
        this.setter = setter;

    }

    public boolean isBean(){
        return (DataBean.class.isAssignableFrom(dataType)) ;
    }

    @Deprecated
    public   Either<Violation, VAL> fromJson(JsonObject jsObj){

        Object val = jsObj.getValue(name);
        if (val == null){
            if (required) return Either.left(LogicalViolations.notNull());
            return Either.right(null);
        }
        return resolveVal(val);
    }

    public  Either<Violation, VAL> evalVal(Object val){
        // type resolve
        Either<Violation, VAL> valEither = resolveVal(val);
        if(valEither.isRight())
        {
            //todo: validation check implementaiton
           throw new UnsupportedOperationException("Validation Check");
        }

        return  valEither;

    }


     abstract Either<Violation, VAL> resolveVal(Object val);



    public Object toJson(D dataBean) {
        VAL val = accessor.apply(dataBean);
        if (val == null)
            return null;
        return toJsonVal(val);
    }

    abstract Object toJsonVal(VAL val);

    @Description("To change a data bean, we should make a copy -> change it and then compare (old, new) to calculate delta")
    public   VAL diff(D from, D to, BiConsumer<String, Object> changeBiConsumer){
        throw new UnsupportedOperationException();
    }
    /*public Either<Violation, VAL> fromJson(JsonObject jsObj) {
        Object val = jsObj.getValue(name);
        if (val != null)
            return typeResolver.evalJsonVal(val);
        else if (required)
            return Either.left(LogicalViolations.notNull());
        else return Either.right(null);
    }

    public Object toJson(D dataBean) {
        VAL val = accessor.apply(dataBean);
        if (val == null)
            return null;
        return typeResolver.toJsonVal(val);
    }

    public VAL diff(D from, D to, BiConsumer<String, Object> changeBiConsumer) {
        VAL fromVal = accessor.apply(from);
        VAL toVal = accessor.apply(to);

        Consumer<VAL> changeConsumer = (val) -> changeBiConsumer.accept(name, val);
        return typeResolver.diff(fromVal, toVal, changeConsumer);
    }

    public static final <D extends DataBean, VAL> SchemaField<D, VAL>
    plainField(String name$, Function<D, VAL> getter$, BiConsumer<D, VAL> setter$, Class<VAL> typeClz$, boolean required$) {
        TypeResolver resolverIF = TypeResolverFactory.plain(typeClz$);
        return new SchemaField<>(name$, getter$, setter$, resolverIF, required$);
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
    }*/


    /*public static final <D extends DataBean, VAL> SchemaField<D, VAL>
    plainField(String name$, Function<D, VAL> getter$, BiConsumer<D, VAL> setter$, Class<VAL> typeClz$, boolean required$) {
        TypeResolver resolverIF = TypeResolverFactory.plain(typeClz$);
        return new SchemaField<>(name$, getter$, setter$, resolverIF, required$);
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
    }*/
}

