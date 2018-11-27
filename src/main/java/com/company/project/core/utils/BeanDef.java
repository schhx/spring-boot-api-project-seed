package com.company.project.core.utils;

import java.util.Map;

public class BeanDef {
    String name;
    Class<?> clazz;
    Map<String, FieldDef> fieldDefs;
    
    public String name() {
        return name;
    }
    public Class<?> clazz() {
        return clazz;
    }
    public Map<String, FieldDef> fieldDefs() {
        return fieldDefs;
    }
}
