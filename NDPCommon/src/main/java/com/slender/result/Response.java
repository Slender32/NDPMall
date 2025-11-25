package com.slender.result;

public record Response<T>(
    Integer code,
    String message,
    Long timestamp,
    T data
){
    public static <T> Response<T> success(final String msg){
        return new Response<>(0, msg, System.currentTimeMillis(), null);
    }

    public static <T> Response<T> fail(final Integer code,final String msg){
        return new Response<>(code, msg, System.currentTimeMillis(), null);
    }

    public static <T> Response<T> success(final String msg,final T data){
        return new Response<>(0, msg, System.currentTimeMillis(), data);
    }

    public static <T> Response<T> success(final T data){
        return new Response<>(0, null, System.currentTimeMillis(), data);
    }

    public static Response<Void> exception(final Integer code,final String msg){
        return new Response<>(code, msg, System.currentTimeMillis(), null);
    }

    public static Response<Void> exception(final String msg){
        return new Response<>(500, msg, System.currentTimeMillis(), null);
    }
}