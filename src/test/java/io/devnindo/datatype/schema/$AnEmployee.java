package io.devnindo.datatype.schema;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.ObjViolation;
import io.devnindo.datatype.validation.Violation;

import java.util.List;

public class $AnEmployee extends BeanSchema<AnEmployee> {
    public static final SchemaField<AnEmployee, Boolean> retire;
    public static final SchemaField<AnEmployee, Gender> gender;
    public static final SchemaField<AnEmployee, List<Address>> address_list;
    public static final SchemaField<AnEmployee, APerson> manager;
    public static final SchemaField<AnEmployee, APerson> employer;
    public static final SchemaField<AnEmployee, Long> id;
    public static final SchemaField<AnEmployee, Integer> age;
    public static final SchemaField<AnEmployee, Integer> salary;

    static {
        retire = plainField("retire", AnEmployee::shouldRetire, Boolean.class, false);
        gender = enumField("gender", AnEmployee::getGender, Gender.class, false);
        address_list = beanListField("address_list", AnEmployee::getAddressList, Address.class, false);
        manager = beanField("manager", AnEmployee::getManager, APerson.class, false);
        employer = beanField("employer", AnEmployee::getEmployer, APerson.class, false);
        id = plainField("id", AnEmployee::getId, Long.class, true);
        age = plainField("age", AnEmployee::getAge, Integer.class, true);
        salary = plainField("salary", AnEmployee::getSalary, Integer.class, false);

        regSchema(new $AnEmployee(), retire, gender, address_list, manager, employer, id, age, salary);

    }

    @Override
    public Either<Violation, AnEmployee> fromJsonObj(JsonObject data) {
        Either<Violation, Gender> genderEither = gender.fromJson(data);
        Either<Violation, List<Address>> addressListEither = address_list.fromJson(data);
        Either<Violation, APerson> managerEither = manager.fromJson(data);
        Either<Violation, APerson> employerEither = employer.fromJson(data);
        Either<Violation, Long> idEither = id.fromJson(data);
        Either<Violation, Integer> ageEither = age.fromJson(data);
        Either<Violation, Integer> salaryEither = salary.fromJson(data);

        ObjViolation violation = newViolation(AnEmployee.class);
        violation.check(gender, genderEither);
        violation.check(address_list, addressListEither);
        violation.check(manager, managerEither);
        violation.check(employer, employerEither);
        violation.check(id, idEither);
        violation.check(age, ageEither);
        violation.check(salary, salaryEither);

        if (violation.hasRequirement()) {
            return Either.left(violation);
        }
        AnEmployee bean = new AnEmployee();
        bean.gender = genderEither.right();
        bean.addressList = addressListEither.right();
        bean.manager = managerEither.right();
        bean.employer = employerEither.right();
        bean.id = idEither.right();
        bean.setAge(ageEither.right());
        bean.setSalary(salaryEither.right());
        return Either.right(bean);
    }

    @Override
    public Either<Violation, AnEmployee> fromJsonStr(String reqObj) {
        return null;
    }

    @Override
    public JsonObject toJsonObj(AnEmployee bean) {
        JsonObject js = new JsonObject();
        js.put(retire.name, retire.toJsonVal(bean));
        js.put(gender.name, gender.toJsonVal(bean));
        js.put(address_list.name, address_list.toJsonVal(bean));
        js.put(manager.name, manager.toJsonVal(bean));
        js.put(employer.name, employer.toJsonVal(bean));
        js.put(id.name, id.toJsonVal(bean));
        js.put(age.name, age.toJsonVal(bean));
        js.put(salary.name, salary.toJsonVal(bean));
        return js;
    }

    @Override
    public String toJsonStr(AnEmployee dataBean$) {
        return "";
    }

    @Override
    public DataDiff<AnEmployee> diff(AnEmployee left, AnEmployee right) {
        AnEmployee merged = new AnEmployee();
        JsonObject delta = new JsonObject();

        merged.gender = gender.diff(left, right, delta::put);
        merged.addressList = address_list.diff(left, right, delta::put);
        merged.manager = manager.diff(left, right, delta::put);
        merged.employer = employer.diff(left, right, delta::put);
        merged.id = id.diff(left, right, delta::put);
        merged.age = age.diff(left, right, delta::put);
        merged.salary = salary.diff(left, right, delta::put);

        return new DataDiff<>(delta, merged);
    }
}
