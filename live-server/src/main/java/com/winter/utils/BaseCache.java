package com.winter.utils;

import java.util.HashMap;

public class BaseCache {
    private static final HashMap<String, Object> caches = new HashMap<String, Object>();

    private BaseCache(){
        super();
    }

    public static synchronized void set(String key, Object value){
        caches.put(key, value);
    }

    public static Object get(String key){
        return caches.get(key);
    }

    public static void delete(String key){
        caches.remove(key);
    }
}
