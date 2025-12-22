package io.devnindo.datatype.schema;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import io.devnindo.datatype.json.JsonBag;
import io.devnindo.datatype.schema.field.SchemaField;
import io.devnindo.datatype.schema.typeresolver.ResolverFactory;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.SchemaViolation;
import io.devnindo.datatype.validation.Violation;
import io.devnindo.datatype.validation.violations.TypeViolations;

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
            SchemaViolation violation = new SchemaViolation("SCHEMA::"+beanClz.getSimpleName());
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                // The current token should be the field name (key)
                if (parser.currentToken() == JsonToken.FIELD_NAME) {
                    String fieldName = parser.getCurrentName();
                    SchemaField field = fmap.get(fieldName);
                    Either<Violation, Object> valEither = evalField(parser, field);
                    if (valEither.isLeft())
                    { // first-fail on violation
                        violation.check(field, valEither);
                    }
                    field.setter.accept(owner, valEither.right());
                }
            }

            return Either.right(owner);

        } catch(IOException excp){
            return Either.left(Violation.of("VALID_JSON").withCtx("PARSE_ERROR", excp.getMessage()));
        }
    }

    private static final Either<Violation, Object> evalField(JsonParser parser, SchemaField field){
        // a Json field value domain: {plain data | {} | []}
        Either<Violation, Object>  valEither;
        if (field.isList) {
           return parseList(parser, field.dataType);
        } else if (field.isBean()){
            return parseBean(parser, field.dataType);
        } else {
            return field.evalVal(parser.getCurrentValue());
        }

    }

    // parse linear 1-D list with type: {DataBean, Integer, Long, String, Boolean, Instant, Double}
    // todo: handle nested list
    public static Either<Violation, Object>  parseList(JsonParser parser, Class type)   {
        // Ensure the current token is the start of an array
        try {
            if (parser.nextToken() != JsonToken.START_ARRAY) {
                return Either.left(TypeViolations.plainDataList(type));
            }

            List dataList = new ArrayList();
            // Iterate over the tokens within the array until the end of the array is reached
            while (parser.nextToken() != JsonToken.END_ARRAY) {
                Either<Violation, Object> valEither;
                if (type.isAssignableFrom(DataBean.class)) { // handle list of bean
                     valEither = parseBean(parser, type);
                } else { // handle other plain type
                    valEither = ResolverFactory.get(type).resolve(parser.currentValue());
                }
                if (valEither.isLeft()){
                    return valEither;
                } else {
                    dataList.add(valEither.right());
                }
            }

            return Either.right(dataList);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
