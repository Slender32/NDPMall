package com.slender.utils;

public final class CacheToolkit {
    private CacheToolkit(){}

    public static String getKey(Class<?> clazz, Long id){
        String[] split = clazz.getName().split("\\.");
        String name = split[split.length - 1];
        return name + ":" + id;
    }

    public static String getRemoveKey(Class<?> clazz, Long id){
        String[] split = clazz.getName().split("\\.");
        String name = split[split.length - 1];
        return name.substring(0, name.length() - 11) + ":" + id;
    }
}
