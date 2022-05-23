package com.example.greenbike.database.models.bike;

import com.example.greenbike.database.models.BaseModel;
import com.example.greenbike.database.models.interfaces.INameable;

import java.io.Serializable;

public abstract class BaseBike extends BaseModel implements INameable {
    private String name;

    protected BaseBike(String id, String name) {
        super(id);

        this.setName(name);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
