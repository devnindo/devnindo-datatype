package io.devnindo.datatype.schema;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;
import io.devnindo.datatype.validation.violations.TypeViolations;

import javax.xml.validation.Schema;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class BeanField<D extends DataBean, VAL extends DataBean> extends SchemaField<D, VAL> {
    private final Violation beanViolation;
    public BeanField(String name, boolean required, Class type,  Function<D, VAL> accessor, BiConsumer<D, VAL> setter) {
        super(name, required, type, false, accessor, setter);
        beanViolation = TypeViolations.beanType(this.dataType);
    }

    @Override
    Either<Violation, VAL> evalJsonVal(Object val) {
        if (val instanceof JsonObject == false)
            return Either.left(beanViolation);
        JsonObject obj = (JsonObject) val;
        return obj.toBeanEither(dataType);
    }

    @Override
    Object toJsonVal(VAL val) {
        return null;
    }

}
