package io.devnindo.datatype.schematest;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.schema.*;
import io.devnindo.datatype.schema.field.SchemaField;
import io.devnindo.datatype.schema.typeresolver.ResolverFactory;
import io.devnindo.datatype.struct.StringIdMap;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.SchemaViolation;
import io.devnindo.datatype.validation.Violation;

import java.util.List;
import java.util.Map;

public class $Address extends BeanSchema<Address> {
    public static final SchemaField<Address, String> city;
    public static final SchemaField<Address, List<String>> road_list;

    static {
         city = plainField("city", String.class, Address::getCity, Address::setCity);
         road_list = plainListField("road_list", String.class, Address::getRoadList, Address::setRoadList);
         ResolverFactory.regResolver(Address.class, new $Address());
    }

    private static final Map<String, SchemaField> fieldMap = new StringIdMap<>(6){{
        put(city.name, city);
        put(road_list.name, road_list);

    }};

    @Override
    public Map<String, SchemaField> fieldMap() {
        return fieldMap();
    }

    @Override
    public Either<Violation, Address> resolve(Iterable<Map.Entry<String, Object>> dataMap) {
        Either<Violation, String> cityEither = city.fromJson(data);
        Either<Violation, List<String>> roadListEither = road_list.fromJson(data);

        SchemaViolation violation = newViolation(Address.class);
        violation.check(city, cityEither);
        violation.check(road_list, roadListEither);

        if (violation.hasRequirement()) {
            return Either.left(violation);
        }
        Address bean = new Address();
        bean.city = cityEither.right();
        bean.roadList = roadListEither.right();
        return Either.right(bean);
    }

    @Override
    public Either<Violation, Address> fromJsonStr(String reqObj) {
        return null;
    }

    @Override
    public JsonObject toJsonObj(Address bean) {
        JsonObject js = new JsonObject();
        js.put(city.name, city.toJson(bean));
        js.put(road_list.name, road_list.toJson(bean));
        return js;
    }

    @Override
    public String encodeStr(Address dataBean$) {
        return "";
    }

    @Override
    public DataDiff<Address> diff(Address left, Address right) {
        Address merged = new Address();
        JsonObject delta = new JsonObject();

        merged.city = city.diff(left, right, delta::put);
        merged.roadList = road_list.diff(left, right, delta::put);

        return new DataDiff<>(delta, merged);
    }
}
