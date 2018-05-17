package com.company.project.core.utils;


import org.springframework.beans.BeanUtils;
import org.springframework.cglib.core.ReflectUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author shanchao
 * @date 2018-04-29
 */
public class EntityUtil {
    public static <R, T> R newInstance(T source, Class<R> clazz) {
        if (null == source) {
            return null;
        }
        Object target = ReflectUtils.newInstance(clazz);
        BeanUtils.copyProperties(source, target);
        return (R) target;
    }

    public static <R, T> List<R> transform(List<T> source, Class<R> clazz) {
        if (null == source || source.isEmpty()) {
            return new ArrayList<>();
        }
        return source.stream()
                .parallel()
                .map(param -> newInstance(param, clazz))
                .collect(toList());
    }

    public static Boolean isEmpty(Object o) {
        try {
            for (Field field : o.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.get(o) != null) {
                    return false;
                }
            }
        } catch (Exception e) {

        }
        return true;
    }

    public static Boolean isNotEmpty(Object o) {
        return !isEmpty(o);
    }
}
