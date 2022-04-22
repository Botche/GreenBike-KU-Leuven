package com.example.greenbike.database.models.user;

import com.example.greenbike.database.models.BaseModel;

public class User extends BaseModel {
    private String email;
    private String password;
    private UserRole userRole;

    public User(String id, String email, String password, UserRole userRole) {
        super(id);

        this.setEmail(email);
        this.setPassword(password);
        this.setUserRole(userRole);
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getUserRole() {
        return this.userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
