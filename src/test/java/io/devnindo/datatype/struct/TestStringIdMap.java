package io.devnindo.datatype.struct;

import java.util.Random;

public class TestStringIdMap {

    public static final String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    public static final Random rnd = new Random();

    protected static String randomString(int length) {
        StringBuilder salt = new StringBuilder();

        while (salt.length() < length) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    public static void main(){

        String strArr[] = new String[20];
        StringIdMap<Object> map = new StringIdMap(strArr.length);
        StringIdMap<Object> map2 = new StringIdMap(strArr.length);
        Random random = new Random();

        for(int idx=0; idx<10; idx++){
            strArr[idx] = randomString(10);
            map.put(strArr[idx], "value: "+idx);
        }

        for (String key : map.keySet()){
            map2.put(key, map.get(key)+"-2");
        }

        for (String str : strArr){
            System.out.printf("Key: %s map1-val:%s map2-val%s\n", str, map.get(str), map2.get(str));
        }

    }
}
