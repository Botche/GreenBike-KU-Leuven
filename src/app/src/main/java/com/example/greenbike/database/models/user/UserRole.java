package com.example.greenbike.database.models.user;

import com.example.greenbike.database.models.BaseModel;
import com.example.greenbike.database.models.interfaces.INameable;

public class UserRole extends BaseModel implements INameable {
    private String name;

    public UserRole(String id, String name) {
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
