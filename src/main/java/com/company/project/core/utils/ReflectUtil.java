package com.company.project.core.utils;

import java.lang.reflect.Field;

/**
 * @author shanchao
 * @date 2018-04-29
 */
public class ReflectUtil {

    public static Integer getId(Object model) {
        try {
            Field field = model.getClass().getDeclaredField("id");
            field.setAccessible(true);
            return (Integer) field.get(model);
        } catch (Exception e){
            throw new RuntimeException();
        }
    }
}
