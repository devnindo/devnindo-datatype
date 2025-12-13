package io.devnindo.datatype.schematest;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.schema.BeanSchema;
import io.devnindo.datatype.schema.DataDiff;
import io.devnindo.datatype.schema.SchemaField;
import io.devnindo.datatype.schema.SchemaGen;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.ObjViolation;
import io.devnindo.datatype.validation.Violation;

import java.util.List;

@SchemaGen
public class $Address extends BeanSchema<Address> {
    public static final SchemaField<Address, String> city;
    public static final SchemaField<Address, List<String>> road_list;

    static {
         city = plainField("city", Address::getCity, Address::setCity, String.class, false);
         road_list = plainListField("road_list", Address::getRoadList, Address::setRoadList, String.class, false);
         regSchema(new $Address(), city, road_list);
    }
    
    @Override
    public Either<Violation, Address> fromJsonObj(JsonObject data) {
        Either<Violation, String> cityEither = city.fromJson(data);
        Either<Violation, List<String>> roadListEither = road_list.fromJson(data);

        ObjViolation violation = newViolation(Address.class);
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
        js.put(city.name, city.toJsonVal(bean));
        js.put(road_list.name, road_list.toJsonVal(bean));
        return js;
    }

    @Override
    public String toJsonStr(Address dataBean$) {
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
