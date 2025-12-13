package io.devnindo.datatype.schematest;


import io.devnindo.datatype.schema.AField;

public class AnEmployee extends APerson {
    Integer salary;


    APerson manager;

    public Integer getSalary() {
        return salary;
    }


    public APerson getManager() {
        return manager;
    }

    @AField
    public Boolean shouldRetire() {
        return age > 50;
    }


    public AnEmployee setSalary(Integer salary) {
        this.salary = salary;
        return this;
    }
    public void setManager(APerson manager) {
        this.manager = manager;
    }
}
