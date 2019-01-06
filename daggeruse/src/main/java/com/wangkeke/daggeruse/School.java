package com.wangkeke.daggeruse;


import javax.inject.Inject;

public class School {

    private String name;

    private String address;

    @Inject
    public School(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
