package com.example.mylibrary.recycler;

public class BaseViewHolderBean<T> {
    protected int type;

    protected T data;

    public BaseViewHolderBean(int type) {
        this.type = type;
    }

    public BaseViewHolderBean(int type, T t) {
        this.type = type;
        this.data = t;
    }

    public T getData() {
        return data;
    }
}
