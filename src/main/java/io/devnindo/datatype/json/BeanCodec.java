package io.devnindo.datatype.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import io.devnindo.datatype.schema.BeanSchema;
import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.schema.field.SchemaField;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.ObjViolation;
import io.devnindo.datatype.validation.Violation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BeanCodec {

    public static final <D extends DataBean> Either<Violation, D> parseBean(String jsStr, Class<D> beanClz){
        JsonParser parser = JsonBag.createParser(jsStr);
        return parseBean(parser, beanClz);
    }

    public static final <D extends DataBean> Either  parseBean(JsonParser parser, Class<D> beanClz){
        BeanSchema<D> schema = BeanSchema.of(beanClz);
        Map<String, SchemaField> fmap = schema.fieldMap();

        try{
            if (parser.nextToken() != JsonToken.START_OBJECT) {
                return Either.left(Violation.withCtx("VALID_JSON", "INVALID_JSON"));
            }

            DataBean owner = schema.newBean().get();
            ObjViolation violation = new ObjViolation("SCHEMA::"+beanClz.getSimpleName());
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                // The current token should be the field name (key)
                if (parser.currentToken() == JsonToken.FIELD_NAME) {
                    String fieldName = parser.getCurrentName();
                    SchemaField field = fmap.get(fieldName);
                    if (field.isBean()) {
                        Either<Violation, Object>  valEither = parseBean(parser, field.dataType);
                        if (valEither.isLeft())
                        { // first-fail on violation
                            violation.check(field, valEither);
                            return Either.left(violation);
                        }
                        field.setter.accept(owner, valEither.right());
                    } else if (field.isBeanList()){

                    }
                }
            }

            return Either.right(owner);

        } catch(IOException excp){
            return Either.left(Violation.of("VALID_JSON").withCtx("PARSE_ERROR", excp.getMessage()));
        }
    }

    public static <T> List<T> parsePlainArr(JsonParser parser, Class<T> type) throws IOException {


        // Ensure the current token is the start of an array
        if (parser.nextToken() != JsonToken.START_ARRAY) {
            throw new IOException("Expected a JSON array start token");
        }

        List<T> dataList = new ArrayList<>();
        // Iterate over the tokens within the array until the end of the array is reached
        while (parser.nextToken() != JsonToken.END_ARRAY) {
            // Check if the current token is an integer value

            if (Long.class.equals(type)) {
               dataList.add((T)evalLong(parser.getCurrentValue()));
            } else if (Double.class.equals(type)){
                evalDouble(parser.getCurrentValue());
            } else if (Boolean.class.equals(type)){
                parser.getBooleanValue();
            }
             else {
                // Handle non-integer values within the array if necessary
                System.out.println("Skipping non-integer token: " + parser.currentToken());
            }
        }

        parser.close();

        // Convert the List<Integer> to an int[]
        return intList.stream().mapToInt(i -> i).toArray();
    }

    private static final Long evalLong(Object val){
        throw new UnsupportedOperationException();
    }

}
