package com.example.greenbike.database.models;

public abstract class BaseModel {
    private String id;

    protected BaseModel(String id) {
        this.setId(id);
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
