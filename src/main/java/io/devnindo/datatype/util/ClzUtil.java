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
package io.devnindo.datatype.util;


import org.atteo.classindex.ClassIndex;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ClzUtil {


    public static Iterable<Class> scanClzList(Class superClz$) {
        return ClassIndex.getSubclasses(superClz$);
    }


    public static String throwableString(Throwable throwable) {
        StringWriter strWrt = new StringWriter();
        PrintWriter printWrt = new PrintWriter(strWrt);
        throwable.printStackTrace(printWrt);
        String excp = strWrt.toString();

        return excp;
    }

    public static boolean methodHasAnnotation(Method method, Class<? extends Annotation> annClz) {
        return method.getAnnotation(annClz) != null;

    }

}

