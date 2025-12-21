package io.devnindo.datatype.schema.field;

import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.schema.typeresolver.ResolverFactory;
import io.devnindo.datatype.schema.typeresolver.TypeResolverIF;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class PlainField<D extends DataBean, VAL> extends SchemaField<D, VAL>{

    public final TypeResolverIF resolver;
    public PlainField(String name, boolean required, Class type, Function<D, VAL> accessor, BiConsumer<D, VAL> setter) {
        super(name,  type, false, accessor, setter);
        resolver = ResolverFactory.get(dataType);
    }

    @Override
    public Either<Violation, VAL> resolveVal(Object val) {
       return resolver.resolve(val);
    }

    @Override
    Object toJsonVal(VAL val) {
        return resolver.toJsonVal(val);
    }
}
