package io.devnindo.datatype.schema;

public enum TypeEnum {
    plain(),
    plain_list
    ;

    private Class<?> typeClz;
    TypeEnum(Class typeClz){
        this.typeClz = typeClz;
    }

}
