package com.example.greenbike.database.models;

import java.io.Serializable;

public abstract class BaseModel implements Serializable {
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
