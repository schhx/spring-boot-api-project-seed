package com.company.project.core.utils;

import java.lang.reflect.Type;

public class FieldDef {
    String name;
    Class<?> clazz;
    Type genericType;
    boolean readable;
    boolean writable;

    public String name() {
        return name;
    }

    public Class<?> clazz() {
        return clazz;
    }

    public Type genericType() {
        return genericType;
    }

    public boolean readable() {
        return readable;
    }

    public boolean writable() {
        return writable;
    }

}
