package io.devnindo.datatype.schematest;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.schema.*;
import io.devnindo.datatype.schema.field.SchemaField;
import io.devnindo.datatype.struct.StringIdMap;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.ObjViolation;
import io.devnindo.datatype.validation.Violation;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class $APerson extends BeanSchema<APerson> {

    public static final SchemaField<APerson, Gender> gender = enumField("gender", APerson::getGender, APerson::setGender, Gender.class, false);
    public static final SchemaField<APerson, Address> primary_address  = beanField("primary_address", APerson::getPrimaryAddress, APerson::setPrimaryAddress, Address.class, true);
    public static final SchemaField<APerson, List<Address>> address_list = beanListField("address_list", APerson::getAddressList, null, Address.class, false);
    public static final SchemaField<APerson, APerson> employer = beanField("employer", APerson::getEmployer, APerson::setEmployer, APerson.class, false);
    public static final SchemaField<APerson, Long> id = plainField("id", APerson::getId, APerson::setId, Long.class, true);
    public static final SchemaField<APerson, Integer> age = plainField("age", APerson::getAge, APerson::setAge, Integer.class, true);

    private static final Map<String, SchemaField> fieldMap = new StringIdMap<>(6){{
            put(gender.name, gender);
            put(primary_address.name, primary_address);
            put(address_list.name, address_list);
            put(employer.name, employer);
            put(id.name, id);
            put(age.name, age);
        }
    };

    private static final Supplier<APerson> supplier = APerson::new;

    @Override
    public Supplier<APerson> newBean() {
        return null;
    }

    @Override
    public String toJsonStr(APerson dataBean$) {
        return "";
    }

    @Override
    public Map<String, SchemaField> fieldMap(){
        return fieldMap;
    }

    @Override
    public Either<Violation, APerson> fromJsonObj(JsonObject data) {

        Either<Violation, Gender> genderEither = gender.fromJson(data);
        Either<Violation, List<Address>> addressListEither = address_list.fromJson(data);
        Either<Violation, APerson> employerEither = employer.fromJson(data);
        Either<Violation, Long> idEither = id.fromJson(data);
        Either<Violation, Integer> ageEither = age.fromJson(data);

        ObjViolation violation = new ObjViolation("SCHEMA::"+APerson.class.getSimpleName());
        violation.check(gender, genderEither);
        violation.check(address_list, addressListEither);
        violation.check(employer, employerEither);
        violation.check(id, idEither);
        violation.check(age, ageEither);

        if (violation.hasRequirement()) {
            return Either.left(violation);
        }
        APerson bean = new APerson();
        bean.gender = genderEither.right();
        bean.addressList = addressListEither.right();
        bean.employer = employerEither.right();
        bean.id = idEither.right();
        bean.setAge(ageEither.right());
        return Either.right(bean);
    }



    @Override
    public JsonObject toJsonObj(APerson bean) {
        JsonObject js = new JsonObject();
        js.put(gender, gender.toJson(bean));
        js.put(address_list, address_list.toJson(bean));
        js.put(employer, employer.toJson(bean));
        js.put(id, id.toJson(bean));
        js.put(age.name, age.toJson(bean));
        return js;
    }


    @Override
    public DataDiff<APerson> diff(APerson left, APerson right) {
        APerson merged = new APerson();
        JsonObject delta = new JsonObject();

        merged.gender = gender.diff(left, right, delta::put);
        merged.addressList = address_list.diff(left, right, delta::put);
        merged.employer = employer.diff(left, right, delta::put);
        merged.id = id.diff(left, right, delta::put);
        merged.age = age.diff(left, right, delta::put);

        return new DataDiff<>(delta, merged);
    }



}
