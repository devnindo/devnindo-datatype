package io.devnindo.datatype.schema.field;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;
import io.devnindo.datatype.validation.violations.TypeViolations;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class EnumField <D extends DataBean, VAL extends Enum<VAL>> extends SchemaField<D, VAL> {
    private final Violation enumViolation ;
    EnumField(String name, boolean required, Class type, Function<D, VAL> accessor, BiConsumer<D, VAL> setter) {
        super(name, required, type, false, accessor, setter);
        enumViolation = TypeViolations.enumVal(this.dataType);
    }

    @Override
    public Either<Violation, VAL> resolveVal(Object val) {
        if (val instanceof String == false) // null safe operation
            return Either.left(enumViolation);
        try {
            VAL anEnum = (VAL) Enum.valueOf(dataType, (String) val);
            return Either.right(anEnum);
        } catch (IllegalArgumentException exception) {
            return Either.left(enumViolation);
        }
    }

    @Override
    Object toJsonVal(VAL val) {
        return val;
    }

}
