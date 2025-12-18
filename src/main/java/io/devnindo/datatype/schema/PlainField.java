package io.devnindo.datatype.schema;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;
import io.devnindo.datatype.validation.violations.LogicalViolations;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class PlainField<D extends DataBean, VAL> extends SchemaField<D, VAL>{

    public PlainField(String name, boolean required, Class type, Function<D, VAL> accessor, BiConsumer<D, VAL> setter) {
        super(name, required, type, false, accessor, setter);
    }

    @Override
    public Either<Violation, VAL> evalJsonVal(Object val) {
       return resolver.evalJsonVal(val);
    }

    @Override
    Object toJsonVal(VAL val) {
        return resolver.toJsonVal(val);
    }
}
