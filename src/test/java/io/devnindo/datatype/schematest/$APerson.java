package io.devnindo.datatype.schematest;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.schema.BeanSchema;
import io.devnindo.datatype.schema.DataDiff;
import io.devnindo.datatype.schema.SchemaField;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.ObjViolation;
import io.devnindo.datatype.validation.Violation;

import java.util.List;

public class $APerson extends BeanSchema<APerson> {

    public static final SchemaField<APerson, Gender> gender;
    public static final SchemaField<APerson, Address> primary_address;
    public static final SchemaField<APerson, List<Address>> address_list  ;
    public static final SchemaField<APerson, APerson> employer;
    public static final SchemaField<APerson, Long> id;
    public static final SchemaField<APerson, Integer> age;

    private static final SchemaField[] fieldArr;

    static {
        gender = enumField("gender", APerson::getGender, APerson::setGender, Gender.class, false);
        primary_address = beanField("primary_address", APerson::getPrimaryAddress, APerson::setPrimaryAddress, Address.class, true);
        address_list = beanListField("address_list", APerson::getAddressList, null, Address.class, false);
        employer = beanField("employer", APerson::getEmployer, APerson::setEmployer, APerson.class, false);
        id = plainField("id", APerson::getId, APerson::setId, Long.class, true);
        age = plainField("age", APerson::getAge, APerson::setAge, Integer.class, true);
        fieldArr = new SchemaField[]{gender, address_list, employer, id, age};

        BeanSchema.regSchema(new $APerson(), fieldArr);
    }


    @Override
    public Either<Violation, APerson> fromJsonObj(JsonObject data) {
        APerson bean = new APerson();
        ObjViolation violation = new ObjViolation("SCHEMA_VIOLATION");

        // need to change implementation: required check & type-check with fast-formatting
        for (SchemaField f : fieldArr){
            Either<Violation, ?> valEither = f.fromJson(data);
            if (valEither.isLeft())
                return Either.left(violation.check(f, valEither));

            f.setter.accept(bean, valEither.right());
        }

         return Either.right(bean);
    }

    @Override
    public Either<Violation, APerson> fromJsonStr(String reqObj) {
        return null;
    }

    @Override
    public JsonObject toJsonObj(APerson bean) {
        JsonObject js = new JsonObject();
        js.put(gender, gender.toJsonVal(bean));
        js.put(address_list, address_list.toJsonVal(bean));
        js.put(employer, employer.toJsonVal(bean));
        js.put(id, id.toJsonVal(bean));
        js.put(age.name, age.toJsonVal(bean));
        return js;
    }


    @Override
    public String toJsonStr(APerson dataBean$) {
        return "";
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

     /* @Override
    public Either<Violation, APerson> fromJsonObj(JsonObject data) {

        Either<Violation, Gender> genderEither = gender.fromJson(data);
        Either<Violation, List<Address>> addressListEither = address_list.fromJson(data);
        Either<Violation, APerson> employerEither = employer.fromJson(data);
        Either<Violation, Long> idEither = id.fromJson(data);
        Either<Violation, Integer> ageEither = age.fromJson(data);

        ObjViolation violation = newViolation(APerson.class);
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
    }*/

}
