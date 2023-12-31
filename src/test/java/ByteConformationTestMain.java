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

import io.devnindo.datatype.json.JsonArray;
import io.devnindo.datatype.json.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

public class ByteConformationTestMain {

    @Test
    public static void check_byte_conversion_utf8_set(String... args) {


        JsonObject js = new JsonObject();
        JsonArray arrJS = new JsonArray("[100, 200, \"aa\"]");
        js.put("string", "A Trash")
                .put("long", 125123123L)
                .put("integer", 123123)
                .put("decimal", 123.456)
                .put("bangla", "আমার সোনার বাংলা। অক্ষি খুঁচে যেচেছি তোমায়")
                .put("array", arrJS)
        ;

        byte[] byteData = js.toByteData();
       // System.out.println(new String(byteData, StandardCharsets.UTF_8));
       // System.out.println("byteArrJs and stringArrJs equal: " + (arrJS.equals(new JsonArray("[100, 200, \"aa\"]".getBytes(StandardCharsets.UTF_8)))));

        JsonObject byteJS = new JsonObject(byteData);

        Assertions.assertEquals(true, js.equals(byteJS), "byteJs and stringJs equal: " );


    }
}

