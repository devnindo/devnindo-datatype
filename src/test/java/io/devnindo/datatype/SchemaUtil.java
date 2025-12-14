package io.devnindo.datatype;

import io.devnindo.datatype.schema.BeanSchema;
import io.devnindo.datatype.schema.DataBean;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import org.joor.Reflect;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SchemaUtil {

    public static void main(String... args){
        // this test util simply help generating schema index file
        // the index file will be generated generally by a gradle plugin

        List<String> clzNameList = findSubClzList(DataBean.class)
                .stream()
                .map(clz -> clz.getName())
                .toList();

        writeSchemaIndex(clzNameList);
    }

    public static void writeSchemaIndex(List<String> beanList){
        StringBuilder builder = new StringBuilder();
        for (Class clz:findSubClzList(BeanSchema.class)){
            builder.append(clz.getName()).append("\n");
        }

        String indexData = builder.substring(0, builder.length()-1);

        // Use the relative path to the src/test/resources directory
        Path resourceDirectory = Paths.get("src", "test", "resources");
        Path filePath = resourceDirectory.resolve(BeanSchema.SCHEMA_IDX); // Combines the directory path with the filename

        try {
            // Ensure the directory exists
            if (!Files.exists(resourceDirectory)) {
                Files.createDirectories(resourceDirectory);
            }
            // Create the new, empty file
            File newFile = filePath.toFile();
            Files.write(filePath, indexData.getBytes());
            System.out.println("Index written to the file.");

        } catch (IOException e) {
            System.err.println("An error occurred while creating the file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<Class<?>> findSubClzList(Class<?> superClz$) {
        List<Class<?>> clzList;
        try (ScanResult scanResult = new ClassGraph().enableClassInfo().scan()) {
            ClassInfoList clzInfoList;
            if (superClz$.isInterface())
                clzInfoList = scanResult.getClassesImplementing(superClz$);
            else
                clzInfoList = scanResult.getSubclasses(superClz$);
            clzList = new ArrayList<>(clzInfoList.size());
            clzInfoList.forEach(clzInfo -> clzList.add(clzInfo.loadClass()));
//
        }
        return clzList;
    }

    public static List<Class<?>> findSubClzList(Class<?> superClz$, String tPackage$)
    //    throws IllegalAccessException
    {
        List<Class<?>> clzList;
        try (ScanResult scanResult = new ClassGraph().acceptPackages(tPackage$)
                .enableClassInfo().scan()) {

            ClassInfoList clzInfoList = scanResult.getSubclasses(superClz$);
            clzList = new ArrayList<>(clzInfoList.size());
            clzInfoList.forEach(clzInfo -> clzList.add(clzInfo.loadClass()));

        }
        return clzList;

    }

    public static <T> T findPackageClzAndReflect(Class<?> superClz$, String tPackge$, Object... initArgs$)
            throws IllegalAccessException {
        List<Class<?>> clzList = findSubClzList(superClz$, tPackge$);

        if (clzList.isEmpty())
            throw new IllegalAccessException("No Subclass found of class: " + superClz$ + " in package: " + tPackge$);

        if (clzList.size() > 1)
            throw new IllegalAccessException("More Than One Subclass found of class: " + superClz$ + " in package: " + tPackge$);

        T t = Reflect.onClass(clzList.get(0)).create(initArgs$).get();

        return t;
    }

    public static <T> T findClzAndReflect(Class<?> superClz$, Object... initArgs$)
            throws IllegalAccessException {
        List<Class<?>> clzList = findSubClzList(superClz$);

        if (clzList.isEmpty())
            throw new IllegalAccessException("No Subclass found of class: " + superClz$);

        if (clzList.size() > 1) {
            StringBuilder builder = new StringBuilder();
            builder.append("More Than One implementation found of class: ")
                    .append(superClz$).append("\n");
            clzList.forEach(clz -> builder.append("\t").append(clz.getName()).append("\n"));
            throw new IllegalAccessException(builder.toString());

        }

        T t = Reflect.onClass(clzList.get(0)).create(initArgs$).get();

        return t;
    }

}
