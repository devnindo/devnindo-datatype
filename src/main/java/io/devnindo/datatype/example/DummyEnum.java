package io.devnindo.datatype.example;

public enum DummyEnum {
    enum_val;

    public static void main(String... args){
        Enum.valueOf(DummyEnum.class, "enum_val");
    }
}
