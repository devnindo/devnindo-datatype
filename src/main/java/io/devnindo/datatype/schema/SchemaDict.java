package io.devnindo.datatype.schema;

import java.util.HashMap;
import java.util.Map;

public final class SchemaDict {
    private static final Map<String, SchemaField> fieldMap = new HashMap<>();
    private static final Map<String, BeanSchema> schemaMap = new HashMap<>();

    private SchemaDict(){
    }

    public static final void regSchema(String clzName, BeanSchema beanSchema){
        schemaMap.put(clzName, beanSchema);
    }

    public static final BeanSchema getSchema(String clzName){
        return schemaMap.get(clzName);
    }

    public static final void regField(String dataClzName, String name, SchemaField field){
        fieldMap.put(dataClzName+"."+name, field);
    }

    public static final SchemaField getField(String dataClzName, String name){
        return fieldMap.get(dataClzName+"."+name);
    }

}
