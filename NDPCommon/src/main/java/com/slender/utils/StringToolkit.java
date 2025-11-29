package com.slender.utils;

public final class StringToolkit {
    private StringToolkit(){}

    public static boolean isBlank(final String str){
        return str==null || str.trim().isEmpty();
    }

    public static String getBlankString(){
        return "";
    }
}
