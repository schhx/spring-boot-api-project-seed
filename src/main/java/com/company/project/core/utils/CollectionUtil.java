package com.company.project.core.utils;

import java.util.*;

public class CollectionUtil {
    public static Map hashMap(Object... objects) {
        Map result = new LinkedHashMap();
        for (int i = 0; i < objects.length / 2; ++i) {
            result.put(objects[2 * i], objects[2 * i + 1]);
        }
        return result;
    }
    
    public static <T> Set<T> hashSet(T... elements) {
        Set<T> set = new HashSet<T>();
        for(T elem: elements) {
            set.add(elem);
        }
        return set;
    }

    public static Map extendMap(Map original, Map extension) {
        for (Map.Entry<String, Object> e : ((Map<String, Object>) extension).entrySet()) {
            original.put(e.getKey(), e.getValue());
        }
        return original;
    }

    public static <T> T[] toArray(Collection<T> collection) {
        T[] target = (T[]) new Object[collection.size()];
        int i = 0;
        for (T e : collection) {
            target[i++] = e;
        }
        return target;
    }

    public static <T> List<T> iterator2list(Iterator<T> iterator) {
        ArrayList<T> result = new ArrayList<T>();
        while (iterator != null && iterator.hasNext()) {
            result.add(iterator.next());
        }
        return result;
    }

    public static <T> List<T> listOf(Collection<T> coll) {
        return new ArrayList<T>(coll);
    }
    
    public static <T> List<T> page(List<T> data, int offset, int limit) {
        if (offset > 0 || limit > 0){
            int from = offset;
            int to = Math.min(data.size(), offset+limit);
            if (from>=to){
                return Collections.emptyList();
            }else{
                return data.subList(from, to);
            }
        }else{
            return data;
        }
    }
}
