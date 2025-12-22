/*
 * Copyright 2023 devnindo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.devnindo.datatype.validation;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.json.Jsonable;

public class Violation<CTX>  {
    public final String constraint;

    protected Violation(String constraint$, CTX ctxData$) {
        constraint = constraint$;
    }


    public static final Violation of(String constraint$) {
        return new Violation(constraint$, null);
    }

    public static final <T> Violation<T> withCtx(String constraint$, T ctxData$) {
        return new Violation(constraint$, ctxData$);
    }

}

