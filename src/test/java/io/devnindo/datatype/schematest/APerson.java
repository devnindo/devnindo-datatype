package io.devnindo.datatype.schematest;

import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.schema.Required;

import java.util.List;

public class APerson implements DataBean {
    @Required
    Long id;

    @Required
    Integer age;
    Gender gender;
    Address primaryAddress;
    List<Address> addressList;
    APerson employer;


    public Long getId() {
        return id;
    }


    public Integer getAge() {
        return age;
    }

    public APerson setAge(Integer age$) {
        age = age$;
        return this;
    }

    public APerson getEmployer() {
        return employer;
    }

    public Gender getGender() {
        return gender;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Address getPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(Address primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

    public void setEmployer(APerson employer) {
        this.employer = employer;
    }
}
