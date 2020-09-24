package com.iproov.androidapiclient.javaretrofit;

public enum PhotoSource {

    ELECTRONIC_ID("eid"),
    OPTICAL_ID("oid");

    private String name;

    PhotoSource(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
