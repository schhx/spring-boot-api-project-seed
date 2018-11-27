package com.company.project.core.utils;


import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BeanUtil {

    public static String toGetterMethodName(String propName, Class<?> clazz) {
        if (Boolean.TYPE.equals(clazz)) {
            return "is" + StringUtil.capitalize(propName);
        } else {
            return "get" + StringUtil.capitalize(propName);
        }
    }

    public static String toSetterMethodName(String propName) {
        return "set" + StringUtil.capitalize(propName);
    }

    public static String readableProperty(String methodName) {
        if (methodName != null) {
            if (methodName.startsWith("get") && StringUtil.isCapitalized(methodName.substring(3))) {
                return StringUtil.deCap(methodName.substring(3));
            } else if (methodName.startsWith("is") && StringUtil.isCapitalized(methodName.substring(2))) {
                return StringUtil.deCap(methodName.substring(2));
            }
        }
        return null;
    }

    public static String writableProperty(String methodName) {
        if (methodName != null && methodName.startsWith("set") && StringUtil.isCapitalized(methodName.substring(3))) {
            return StringUtil.deCap(methodName.substring(3));
        } else {
            return null;
        }
    }

    private static ParameterizedType getSuperClasGenericMapKeyType(Object obj) {
        Type superType = obj.getClass().getGenericSuperclass();
        while (true) {
            if (superType instanceof ParameterizedType) {
                ParameterizedType pType = (ParameterizedType) superType;
                Type rawType = pType.getRawType();
                if (rawType instanceof Class && Map.class.isAssignableFrom((Class<?>) rawType)) {
                    return pType;
                }
            }
            if (superType instanceof Class) {
                superType = ((Class<?>) superType).getGenericSuperclass();
            } else {
                return null;
            }
        }
    }

    public static Object getProperty(Object obj, String propName) {
        if (obj == null) {
            return null;
        } else if (obj instanceof Map) {
            ParameterizedType pType = getSuperClasGenericMapKeyType(obj);
            if (pType != null) {
                Type kType = pType.getActualTypeArguments()[0];

                if (kType instanceof Class && ((Class<?>) kType).isEnum()) {
                    return ((Map) obj).get(Enum.valueOf((Class) kType, propName));
                }
            }
            return ((Map) obj).get(propName);
        } else if (obj instanceof List) {
            List<?> l = (List<?>) obj;
            int idx = 0;
            try {
                idx = Integer.parseInt(propName);
            } catch (NumberFormatException e) {
                return null;
            }
            if (idx >= 0 && idx < l.size()) {
                return l.get(idx);
            } else {
                return null;
            }
        } else if (obj instanceof Set) {
            Set<?> l = (Set<?>) obj;
            int idx = 0;
            try {
                idx = Integer.parseInt(propName);
            } catch (NumberFormatException e) {
                return null;
            }
            if (idx >= 0 && idx < l.size()) {
                int iteratorTime = 0;
                Iterator<?> it = l.iterator();
                while (it.hasNext()) {
                    if (iteratorTime == idx) {
                        return it.next();
                    } else {
                        it.next();
                    }
                    iteratorTime++;
                }
                return null;
            }
            return null;
        } else {
            return getBeanProperty(obj, propName);
        }
    }

    public static Object getProperty(Object obj, String propName, Type genericType) {
        if (obj == null) {
            return null;
        }
        if (genericType != null && genericType instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) genericType;
            Type rawType = pType.getRawType();
            if (rawType instanceof Class && Map.class.isAssignableFrom((Class<?>) rawType)) {
                Type kType = pType.getActualTypeArguments()[0];
                if (kType instanceof Class && ((Class<?>) kType).isEnum()) {
                    return ((Map) obj).get(Enum.valueOf((Class) kType, propName));
                }
            }
        }
        return getProperty(obj, propName);
    }

    public static Object getBeanProperty(Object bean, String propName) {
        if (bean == null) {
            return null;
        }
        if (StringUtil.empty(propName))
            return null;
        Map<String, FieldDef> fieldDefs = getBeanDef(bean).fieldDefs();
        FieldDef fieldDef = fieldDefs.get(propName);
        try {
            if (fieldDef != null) {
                Method accessor = bean.getClass().getMethod(toGetterMethodName(propName, fieldDef.clazz()));
                return accessor.invoke(bean);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void setProperty(Object obj, String propName, Object value) {
        if (obj instanceof Map) {
            ParameterizedType pType = getSuperClasGenericMapKeyType(obj);
            if (pType != null) {
                Type kType = pType.getActualTypeArguments()[0];

                if (kType instanceof Class && ((Class<?>) kType).isEnum()) {
                    ((Map) obj).put(Enum.valueOf((Class) kType, propName), value);
                    return;
                }
            }
            ((Map) obj).put(propName, value);
            return;
        } else if (obj instanceof List) {
            List l = (List) obj;
            int idx = 0;
            try {
                idx = Integer.parseInt(propName);
            } catch (NumberFormatException e) {
                return;
            }
            if (idx >= 0 && idx < l.size()) {
                l.set(idx, value);
            }
        } else if (obj instanceof Set) {
            throw new RuntimeException("unable to update set elements");
        } else {
            setBeanProperty(obj, propName, value);
        }
    }

    public static void setBeanProperty(Object bean, String propName, Object value) {
        if (StringUtil.empty(propName))
            return;

        Map<String, FieldDef> fieldDefs = getBeanDef(bean).fieldDefs();
        FieldDef fieldDef = fieldDefs.get(propName);
        if (fieldDef != null && fieldDef.writable()) {
            try {
                Method accessor = bean.getClass().getMethod(toSetterMethodName(propName), fieldDef.clazz());
                accessor.invoke(bean, value);
            } catch (Exception ex) {
                throw new RuntimeException("runtime error setting property " + propName + " with value " + value, ex);
            }
        } else {
            throw new IllegalArgumentException("property " + propName + " is not defined or not writable.");
        }
    }

    public static Object getNestedProperty(Object obj, String propName) {
        if (obj == null) {
            return null;
        }
        if (StringUtil.empty(propName))
            return null;
        String[] components = propName.split("\\.");

        Type genericType = null;
        for (String component : components) {
            FieldDef fieldDef = null;
            if (!(obj instanceof Map) && !(obj instanceof List) && !(obj instanceof Set)) {
                BeanDef beanDef = getBeanDef(obj);
                fieldDef = beanDef.fieldDefs().get(component);
            }

            Type listElementGenericType = null;
            if ((obj instanceof List || obj instanceof Set) && genericType != null && genericType instanceof ParameterizedType) {
                listElementGenericType = ((ParameterizedType) genericType).getActualTypeArguments()[0];
            }

            obj = getProperty(obj, component, genericType);

            if (fieldDef != null) {
                genericType = fieldDef.genericType();
            } else {
                if (listElementGenericType != null) {
                    genericType = listElementGenericType;
                } else {
                    genericType = null;
                }
            }

            if (obj == null) {
                return null;
            }
        }
        return obj;
    }

    public static void setNestedProperty(Object obj, String propName, Object value) {
        if (StringUtil.empty(propName))
            return;
        String[] components = propName.split("\\.");

        Type genericType = null;
        for (int i = 0; i < components.length - 1; ++i) {
            String component = components[i];
            FieldDef fieldDef = null;
            if (!(obj instanceof Map) && !(obj instanceof List) && !(obj instanceof Set)) {
                BeanDef beanDef = getBeanDef(obj);
                fieldDef = beanDef.fieldDefs().get(component);
            }

            Type listElementGenericType = null;
            if ((obj instanceof List || obj instanceof Set) && genericType != null && genericType instanceof ParameterizedType) {
                listElementGenericType = ((ParameterizedType) genericType).getActualTypeArguments()[0];
            }

            Object subObj = getProperty(obj, component, genericType);

            if (fieldDef != null) {
                genericType = fieldDef.genericType();
            } else {
                if (listElementGenericType != null) {
                    genericType = listElementGenericType;
                } else {
                    genericType = null;
                }
            }

            if (subObj == null) {
                if (fieldDef == null) {
                    subObj = CollectionUtil.hashMap();
                } else {
                    try {
                        subObj = fieldDef.clazz().newInstance();
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException("failed to create bean instance", e);
                    }
                }
                setProperty(obj, component, subObj);
            }
            obj = subObj;
        }

        setProperty(obj, components[components.length - 1], value);
    }

    private static Map<Class<?>, BeanDef> beanDefs = new ConcurrentHashMap<Class<?>, BeanDef>();


    public static BeanDef getBeanDef(Object bean) {
        return getBeanDef(bean.getClass());
    }

    public static BeanDef getBeanDef(Class<?> clazz) {
        if (beanDefs.get(clazz) != null) {
            return beanDefs.get(clazz);
        }
        synchronized (beanDefs) {
            if (beanDefs.get(clazz) != null) {
                return beanDefs.get(clazz);
            }

            BeanDef beanDef = new BeanDef();

            beanDef.clazz = clazz;
            Map<String, FieldDef> fieldDefs = new LinkedHashMap<String, FieldDef>();
            Method[] methods = clazz.getMethods();
            Arrays.sort(methods, new Comparator<Method>() {
                public int compare(Method o1, Method o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            for (Method method : clazz.getMethods()) {
                String methodName = method.getName();

                String propName = BeanUtil.readableProperty(methodName);
                Class<?> returnType = method.getReturnType();
                if (propName != null && !propName.isEmpty() && !propName.equals("class") && !propName.equals("metaClass")) {
                    FieldDef fieldDef = fieldDefs.get(propName);
                    if (fieldDef == null) {
                        fieldDef = new FieldDef();
                        fieldDef.name = propName;
                        fieldDef.clazz = returnType;
                        fieldDefs.put(propName, fieldDef);
                        fieldDef.readable = true;
                        fieldDef.genericType = method.getGenericReturnType();
                    } else if (returnType.equals(fieldDef.clazz)) {
                        fieldDef.readable = true;
                    }
                }

                propName = BeanUtil.writableProperty(methodName);
                Class<?>[] paramTypes = method.getParameterTypes();
                if (propName != null && !propName.isEmpty() && !propName.equals("metaClass") && paramTypes.length == 1) {
                    FieldDef fieldDef = fieldDefs.get(propName);
                    if (fieldDef == null) {
                        fieldDef = new FieldDef();
                        fieldDef.name = propName;
                        fieldDef.clazz = paramTypes[0];
                        fieldDefs.put(propName, fieldDef);
                        fieldDef.writable = true;
                        fieldDef.genericType = method.getGenericParameterTypes()[0];
                    } else if (paramTypes[0].equals(fieldDef.clazz)) {
                        fieldDef.writable = true;
                    }
                }

            }

            beanDef.fieldDefs = fieldDefs;

            beanDefs.put(clazz, beanDef);
            return beanDef;
        }
    }

    public static Object describeList(Type componentType) {
        List<Object> listDesc = new ArrayList<Object>();
        listDesc.add(describe(componentType));
        return listDesc;
    }

    private static Map<Class<?>, Object> beanDescriptions = new ConcurrentHashMap<Class<?>, Object>();

    public static Collection<Object> getAllBeanDescriptions() {
        return beanDescriptions.values();
    }

    public static Object getBeanDescription(Class<?> clazz) {
        if (beanDescriptions.get(clazz) != null)
            return beanDescriptions.get(clazz);

        synchronized (beanDescriptions) {
            if (beanDescriptions.get(clazz) != null) {
                return beanDescriptions.get(clazz);
            }

            beanDescriptions.put(clazz, CollectionUtil.hashMap("type", clazz.getName(), "fields", "unknown"));
            beanDescriptions.put(clazz, describeBean(clazz));
            return beanDescriptions.get(clazz);
        }

    }

    public static String getBeanTypeName(Class<?> clazz) {
        getBeanDescription(clazz);
        return clazz.getName();
    }

    public static String getParameterizedTypeName(ParameterizedType parameterizedType) {
        Class<?> rawType = (Class<?>) parameterizedType.getRawType();
        Type[] typeArguments = parameterizedType.getActualTypeArguments();
        if (typeArguments.length > 0) {
            String[] argTypeNames = new String[typeArguments.length];
            for (int i = 0; i < typeArguments.length; ++i) {
                argTypeNames[i] = getTypeName(typeArguments[i]);
            }
            return getClassTypeName(rawType, argTypeNames);
        } else {
            return getClassTypeName(rawType);
        }
    }

    public static String getClassTypeName(Class<?> clazz, String... argTypeNames) {
        /*
        if (clazz.isArray()) {
            return "[" + getClassTypeName(clazz.getComponentType()) + "]";
        }else if (Collection.class.isAssignableFrom(clazz)){
            String compTypeName = "object";
            if (argTypeNames.length > 0){
                compTypeName = argTypeNames[0];
            }
            return "[" + compTypeName + "]";
        }else if (Map.class.isAssignableFrom(clazz)){
            if (argTypeNames.length == 2){
                return "map<" + argTypeNames[0] + ", " + argTypeNames[1] + ">";
            }else{
                return "map";
            }
        }else if (String.class.isAssignableFrom(clazz)){
            return "string";
        }else if (Object.class.equals(clazz)){
            return "object";
        }
        */
        StringBuilder result = new StringBuilder();
        result.append(clazz.getName());
        if (argTypeNames.length > 0) {
            result.append("<");
            for (int i = 0; i < argTypeNames.length; ++i) {
                if (i > 0) {
                    result.append(", ");
                }
                result.append(argTypeNames[i]);
            }
            result.append(">");
        }
        return result.toString();
    }

    public static String getTypeName(Type type) {
        if (type instanceof Class) {
            return getClassTypeName((Class<?>) type);
        } else if (type instanceof ParameterizedType) {
            return getParameterizedTypeName((ParameterizedType) type);
        } else {
            return type.toString();
        }
    }


    public static Map<String, Object> describeBean(Class<?> clazz) {
        Map<String, Object> description = new LinkedHashMap<String, Object>();
        description.put("type", clazz.getName());
        Map<String, Object> fields = new LinkedHashMap<String, Object>();
        Map<String, FieldDef> fieldDefs = getBeanDef(clazz).fieldDefs();
        for (String fieldName : fieldDefs.keySet()) {
            FieldDef fieldDef = fieldDefs.get(fieldName);
            if (fieldDef.readable() && fieldDef.writable()) {
                fields.put(fieldName, describe(fieldDef.genericType()));
            }
        }
        description.put("fields", fields);
        return description;
    }

    public static Map<String, Object> describeEnum(Class<?> clazz) {
        Map<String, Object> description = new LinkedHashMap<String, Object>();
        description.put("type", clazz.getName());
        List<String> values = new ArrayList<String>();
        description.put("values", values);
        try {
            Method valuesMethod = clazz.getMethod("values");
            Object[] vals = (Object[]) valuesMethod.invoke(null);
            for (Object v : vals) {
                values.add(v.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return description;
    }

    public static Object describe(Type type) {
        if (type instanceof Class) {
            return describe((Class<?>) type);
        } else if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            Type rawType = pType.getRawType();
            if (rawType instanceof Class && Collection.class.isAssignableFrom((Class<?>) rawType)) {
                return describeList(pType.getActualTypeArguments()[0]);
            } else {
                return getTypeName(pType);
            }
        } else if (type instanceof GenericArrayType) {
            return describeList(((GenericArrayType) type).getGenericComponentType());
        } else if (type instanceof TypeVariable) {
            return ((TypeVariable<?>) type).getName();
        } else {
            return "object";
        }
    }

    public static Object describe(Class<?> clazz) {
        if (List.class.isAssignableFrom(clazz)) {
            return describeList(Object.class);
        } else if (Enum.class.isAssignableFrom(clazz)) {
            return describeEnum(clazz);
        } else {
            return getTypeName(clazz);
        }
    }

    public static Object makeCopy(Object obj) {
        if (obj instanceof Map) {
            return new HashMap((Map) obj);
        } else {
            return copyBean(obj);
        }
    }

    /**
     * Make a shallow copy of the original object.
     *
     * @param bean
     * @return
     */
    public static Object copyBean(Object bean) {
        if (bean == null) {
            return null;
        }
        try {
            BeanDef beanDef = getBeanDef(bean);
            Object copy = beanDef.clazz().newInstance();
            Map<String, FieldDef> fieldDefs = beanDef.fieldDefs();
            for (FieldDef fieldDef : fieldDefs.values()) {
                if (fieldDef.readable() && fieldDef.writable()) {
                    String propName = fieldDef.name();
                    setBeanProperty(copy, propName, getBeanProperty(bean, propName));
                }
            }
            return copy;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object deepCopyBean(Object bean) {
        if (bean == null) {
            return null;
        }
        try {
            BeanDef beanDef = getBeanDef(bean);
            Object copy = beanDef.clazz().newInstance();
            Map<String, FieldDef> fieldDefs = beanDef.fieldDefs();
            for (FieldDef fieldDef : fieldDefs.values()) {
                if (fieldDef.readable() && fieldDef.writable()) {
                    String propName = fieldDef.name();
                    Object value = getBeanProperty(bean, propName);
                    String valueClassName = fieldDef.clazz().getName();
                    if (valueClassName.startsWith("com.wosai")) {
                        setBeanProperty(copy, propName, deepCopyBean(value));
                    } else {
                        setBeanProperty(copy, propName, value);
                    }
                }
            }
            return copy;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    public static void updatePart(Object obj, Map<String, Object> update) {
        for (Map.Entry<String, Object> kv : update.entrySet()) {
            String key = kv.getKey();
            Object value = kv.getValue();
            setNestedProperty(obj, key, value);
        }
    }

    public static Map<String, Object> getPart(Object obj, Collection<String> projection) {
        Map<String, Object> part = new LinkedHashMap<String, Object>();
        for (String field : projection) {
            Object value = getNestedProperty(obj, field);
            if (value != null) {
                setNestedProperty(part, field, value);
            }
        }
        return part;
    }

    public static Object toGenericData(Object bean) {
        if (bean == null) {
            return null;
        } else if (bean instanceof Number || bean instanceof Boolean || bean instanceof String) {
            return bean;
        } else if (bean instanceof List) {
            List<Object> list = new ArrayList<Object>();
            for (Object elem : (List) bean) {
                list.add(toGenericData(elem));
            }
            return list;

        } else if (bean.getClass().isArray()) {
            List<Object> list = new ArrayList<Object>();
            for (int i = 0; i < Array.getLength(bean); ++i) {
                list.add(Array.get(bean, i));
            }
            return list;
        } else if (bean instanceof Map) {
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            for (Map.Entry<String, Object> entry : ((Map<String, Object>) bean).entrySet()) {
                map.put(entry.getKey(), toGenericData(entry.getValue()));
            }
            return map;
        } else {
            Map<String, Object> dbObj = new LinkedHashMap<String, Object>();
            Map<String, FieldDef> fieldDefs = getBeanDef(bean).fieldDefs();
            for (FieldDef fieldDef : fieldDefs.values()) {
                String key = fieldDef.name();
                if (!fieldDef.readable()) {
                    continue;
                }

                try {
                    Method accessor = bean.getClass().getMethod(toGetterMethodName(key, fieldDef.clazz()));
                    Object value = accessor.invoke(bean);
                    dbObj.put(key, toGenericData(value));
                } catch (ReflectiveOperationException e) {

                }
            }
            return dbObj;
        }

    }

    private static Class<?> boxedType(Class<?> type) {
        if (type == Integer.TYPE) {
            return Integer.class;
        } else if (type == Long.TYPE) {
            return Long.class;
        } else if (type == Float.TYPE) {
            return Float.class;
        } else if (type == Double.TYPE) {
            return Double.class;
        } else if (type == Boolean.TYPE) {
            return Boolean.class;
        }
        return type;
    }

    private static boolean isAssignable(Class<?> targetType, Class<?> actualType) {
        return boxedType(targetType).isAssignableFrom(boxedType(actualType));
    }

    public static Object fromGenericData(Object data, Type genericType) {
        if (data == null) {
            return null;
        } else if (data instanceof Number || data instanceof Boolean || data instanceof String) {
            if (genericType instanceof Class && isAssignable((Class) genericType, data.getClass())) {
                return data;
            } else {
                throw new RuntimeException(String.format("incompatible type %s => %s", data.getClass(), genericType));
            }
        } else if (genericType instanceof Class && ((Class) genericType).isArray()) {
            if (data instanceof List) {
                Class componentType = ((Class) genericType).getComponentType();
                int length = ((List) data).size();

                Object arr = Array.newInstance(componentType, length);
                for (int i = 0; i < ((List) data).size(); ++i) {
                    Array.set(arr, i, ((List) data).get(i));
                }
                return arr;
            } else {
                throw new RuntimeException(String.format("incompatible array type %s => %s", data.getClass(), genericType));
            }

        } else {
            Class clazz = (genericType instanceof ParameterizedType) ? (Class) ((ParameterizedType) genericType).getRawType() : (Class) genericType;

            if (clazz == List.class || clazz == ArrayList.class) {
                if (data instanceof List) {
                    Type componentType = (genericType instanceof ParameterizedType) ? ((ParameterizedType) genericType).getActualTypeArguments()[0] : Object.class;

                    List<Object> list = new ArrayList<Object>();
                    for (Object elem : (List<Object>) data) {
                        list.add(fromGenericData(elem, componentType));
                    }
                    return list;
                }
            } else if (clazz == Map.class || clazz == HashMap.class || clazz == LinkedHashMap.class) {
                if (data instanceof Map) {
                    Type componentType = (genericType instanceof ParameterizedType) ? ((ParameterizedType) genericType).getActualTypeArguments()[1] : Object.class;
                    Map<String, Object> map = new LinkedHashMap<String, Object>();
                    for (Map.Entry<String, Object> entry : ((Map<String, Object>) data).entrySet()) {
                        map.put(entry.getKey(), fromGenericData(entry.getValue(), componentType));
                    }
                    return map;
                }
            } else {
                Object bean;
                try {
                    bean = clazz.newInstance();
                } catch (ReflectiveOperationException e) {
                    return null;
                }
                Map<String, FieldDef> fieldDefs = BeanUtil.getBeanDef(clazz).fieldDefs();
                if (data instanceof Map) {
                    for (Map.Entry<String, Object> entry : ((Map<String, Object>) data).entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        FieldDef fieldDef = fieldDefs.get(entry.getKey());
                        if (fieldDef != null && fieldDef.writable() && value != null) {
                            setBeanProperty(bean, key, fromGenericData(value, fieldDef.genericType()));
                        }
                    }
                    return bean;
                }
            }
        }
        throw new RuntimeException(String.format("incompatible array type %s => %s", data.getClass(), genericType));
    }

    public static String getPropString(Object obj, String propName, String defaultVal) {
        Object value = getNestedProperty(obj, propName);
        if (value != null) {
            return value.toString();
        } else {
            return defaultVal;
        }
    }

    public static String getPropString(Object obj, String propName) {
        return getPropString(obj, propName, null);
    }

    public static int getPropInt(Object obj, String propName, int defaultVal) {
        Object value = getNestedProperty(obj, propName);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        } else if (value instanceof String) {
            return Integer.parseInt((String) value);
        } else {
            return defaultVal;
        }
    }

    public static int getPropInt(Object obj, String propName) {
        return getPropInt(obj, propName, 0);
    }

    public static long getPropLong(Object obj, String propName, long defaultVal) {
        Object value = getNestedProperty(obj, propName);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value instanceof String) {
            return Long.parseLong((String) value);
        } else {
            return defaultVal;
        }
    }

    public static long getPropLong(Object obj, String propName) {
        return getPropLong(obj, propName, 0);
    }

    public static boolean getPropBoolean(Object obj, String propName, boolean defaultVal) {
        Object value = getNestedProperty(obj, propName);
        if (value instanceof Number) {
            return ((Number) value).longValue() != 0L;
        } else if (value instanceof Boolean) {
            return (Boolean) value;
        } else {
            return defaultVal;
        }
    }

    public static boolean getPropBoolean(Object obj, String propName) {
        return getPropBoolean(obj, propName, false);
    }
}
