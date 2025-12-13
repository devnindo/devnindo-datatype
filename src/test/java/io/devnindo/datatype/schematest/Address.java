package io.devnindo.datatype.schematest;

import io.devnindo.datatype.schema.DataBean;

import java.util.List;

public class Address implements DataBean {
    String city;

    List<String> roadList;

    public String getCity() {
        return city;
    }

    public List<String> getRoadList() {
        return roadList;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setRoadList(List<String> roadList) {
        this.roadList = roadList;
    }
}
