package io.devnindo.datatype.schema;

import io.devnindo.datatype.schema.field.SchemaField;
import io.devnindo.datatype.schema.typeresolver.TypeResolver;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class FieldPlain<D extends DataBean, VAL> extends SchemaField<D, VAL> {

    public final TypeResolver<VAL> typeResolver;

    FieldPlain(String name, Function<D, VAL> accessor, BiConsumer<D, VAL> setter, TypeResolver<VAL> typeResolver, boolean required) {
        super(name, accessor, setter, required);
        this.typeResolver = typeResolver;
    }

    public static <D extends DataBean, VAL> SchemaField
    fInt(String name, Function<D, VAL> accessor, BiConsumer<D, VAL> setter, boolean required){
        return new FieldPlain(name, accessor, setter, TypeResolver.Int, required);
    }
}
