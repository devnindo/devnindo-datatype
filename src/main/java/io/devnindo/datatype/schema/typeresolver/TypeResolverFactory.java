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
package io.devnindo.datatype.schema.typeresolver;

import io.devnindo.datatype.schema.typeresolver.lists.PlainListResolver;
import io.devnindo.datatype.schema.typeresolver.literals.*;

import java.time.Instant;


@Deprecated
public final class TypeResolverFactory {

    public static final TypeResolver INT = new IntegerResolver();
    public static final TypeResolver LONG = new LongResolver();
    public static final TypeResolver DOUBLE = new DoubleResolver();
    public static final TypeResolver STRING = new StringResolver();
    public static final TypeResolver BOOLEAN = new BooleanResolver();
    public static final TypeResolver INSTANT = new InstantResolver();

    public static final TypeResolver LIST_INT = new PlainListResolver<>(Integer.class);
    public static final TypeResolver LIST_LONG = new PlainListResolver<>(Long.class);
    public static final TypeResolver LIST_DOUBLE = new PlainListResolver<>(Double.class);
    public static final TypeResolver LIST_STRING = new PlainListResolver<>(String.class);
    public static final TypeResolver LIST_BOOLEAN = new PlainListResolver<>(Boolean.class);
    public static final TypeResolver LIST_INSTANT = new PlainListResolver<>(Instant.class);
    public static final TypeResolver LIST_VAL = new PlainListResolver<>(Object.class);




}

