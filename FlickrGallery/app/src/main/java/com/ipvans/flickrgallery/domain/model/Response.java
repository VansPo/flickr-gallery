package com.ipvans.flickrgallery.domain.model;

public class Response<T> {

    private boolean loading;
    private Throwable e;
    private T data;

    private Response(boolean loading, Throwable e, T data) {
        this.loading = loading;
        this.e = e;
        this.data = data;
    }

    public static <T> Response<T> ok(T data) {
        return new Response<>(false, null, data);
    }

    public static <T> Response<T> error(Throwable e) {
        return new Response<>(false, e, null);
    }

    public static <T> Response<T> loading() {
        return new Response<>(true, null, null);
    }

    public boolean isLoading() {
        return loading;
    }

    public Throwable getError() {
        return e;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Response{" +
                "loading=" + loading +
                ", e=" + e +
                ", data=" + data +
                '}';
    }
}
