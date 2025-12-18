package io.devnindo.datatype.schema;

import io.devnindo.datatype.json.JsonArray;
import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;
import io.devnindo.datatype.validation.violations.TypeViolations;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ListField<D extends DataBean, VAL> extends SchemaField<D, List<VAL>> {

    public ListField(String name, boolean required, Class type, Function<D, List<VAL>> accessor, BiConsumer<D, List<VAL>> setter) {
        super(name, required, type, true, accessor, setter);
    }

    @Override
    public Either<Violation, List<VAL>> evalJsonVal(Object val) {

        boolean jsonList = val instanceof JsonArray || val instanceof List;
        if (!jsonList)
            return Either.left(TypeViolations.plainDataList(dataType));

        JsonArray array = (JsonArray) val;
        List<VAL> dataList = new ArrayList<>();
        for (int idx = 0; idx < array.size(); idx++) {
            Object obj = array.getValue(idx);
            if (obj == null) continue;
            Either<Violation, Object> valEither = resolver.evalJsonVal(obj);
            if(valEither.isLeft())
                return valEither;

            dataList.add(valEither.right())
        }
        return Either.right(dataList);
    }
}
