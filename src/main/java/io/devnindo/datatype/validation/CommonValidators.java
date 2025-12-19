package io.devnindo.datatype.validation;

import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.violations.LogicalViolations;

import java.util.regex.Pattern;

public class CommonValidators
{
    private static final Pattern emailPattern = Pattern.compile("^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
    public static final Validator<String> asEmail = (t) -> {
            if (t instanceof String) {
                String email = (String) t;

                if (emailPattern.matcher(email).matches())
                    return Either.right(null);

                else return Either.left(Violation.of("EMAIL_FORMAT"));
            } else return Either.left(Violation.of("EMAIL_FORMAT"));

    };

    public static final Validator notNull = t -> {
        if (t == null)
            return Either.left(LogicalViolations.notNull());
        else return Either.right(null);
    };

    public static final <T> Validator<T> equal(T t$) {
        return (t) -> {
            if (t$.equals(t))
                return Either.right(null);
            else return Either.left(LogicalViolations.equalBound(t$));

        };
    }

    public static final <T extends Comparable> Validator<T> inSet(T... tVar$) {
        return (t) -> {
            for (T t0 : tVar$) {
                if (t0.equals(t))
                    return Either.right(null);
            }
            return Either.left(LogicalViolations.setBound(tVar$));
        };
    }

    public static final <T extends Comparable<T>> Validator<T> max(T t$) {
        return (t) -> {

            int comp = t.compareTo(t$);
            if (comp > 0)
                return Either.left(LogicalViolations.maxBound(t$));

            return Either.right(null);

        };
    }

    public static final <T extends Comparable<T>> Validator<T> min(T t$) {
        return (t) -> {

            int comp = t.compareTo(t$);
            if (comp < 0)
                return Either.left(LogicalViolations.minBound(t$));

            return Either.right(null);
        };
    }

    public static final <T extends Comparable<T>> Validator<T> range(T max$, T min$) {
        return (t) -> {
            if (t.compareTo(max$) > 0 || t.compareTo(min$) < 0)
                return Either.left(LogicalViolations.rangeBound(min$, max$));

            return Either.right(null);
        };
    }
}
