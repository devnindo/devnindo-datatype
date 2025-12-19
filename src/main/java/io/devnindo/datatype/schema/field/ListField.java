package io.devnindo.datatype.schema.field;

import io.devnindo.datatype.json.JsonArray;
import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.schema.typeresolver.ResolverFactory;
import io.devnindo.datatype.schema.typeresolver.TypeResolverIF;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;
import io.devnindo.datatype.validation.violations.TypeViolations;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ListField<D extends DataBean, VAL> extends SchemaField<D, List<VAL>> {

    public final TypeResolverIF resolver;
    public ListField(String name, boolean required, Class type, Function<D, List<VAL>> accessor, BiConsumer<D, List<VAL>> setter) {
        super(name, required, type, true, accessor, setter);
        resolver = ResolverFactory.get(dataType);
    }

    @Override
    public Either<Violation, List<VAL>> resolveVal(Object val) {

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
                return Either.left(valEither.left());

            dataList.add((VAL) valEither.right());
        }
        return Either.right(dataList);
    }

    @Override
    Object toJsonVal(List<VAL> vals) {

        JsonArray jsArr = new JsonArray();
        for (VAL val: vals){
            jsArr.add(resolver.toJsonVal(val));
        }
        return jsArr;
    }
}
