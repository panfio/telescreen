package ru.panfio.telescreen.service;

public enum Bucket {

    MEDIA ("media"),
    APP ("app"),
    AUTOTIMER("autotimer");

    String name;
    Bucket(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
