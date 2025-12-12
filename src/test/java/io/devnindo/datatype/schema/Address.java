package io.devnindo.datatype.schema;

import java.util.List;

public class Address implements DataBean  {
    String city;

    List<String> roadList;

    public String getCity() {
        return city;
    }

    public List<String> getRoadList() {
        return roadList;
    }
}
