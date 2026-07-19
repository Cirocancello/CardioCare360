package com.cardiocare360.model.response;

public class ResponseWrapper<T> {

    private T data;
    private Object alert;

    public ResponseWrapper(T data, Object alert) {
        this.data = data;
        this.alert = alert;
    }

    public T getData() {
        return data;
    }

    public Object getAlert() {
        return alert;
    }
}
