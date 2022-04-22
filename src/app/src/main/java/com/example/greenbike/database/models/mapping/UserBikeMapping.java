package com.example.greenbike.database.models.mapping;

import com.example.greenbike.database.models.BaseModel;
import com.example.greenbike.database.models.bike.Bike;
import com.example.greenbike.database.models.user.User;

import java.time.LocalDateTime;

public class UserBikeMapping extends BaseModel {
    private User user;
    private Bike bike;
    private LocalDateTime rentStartDate;
    private LocalDateTime rentEndDate;
    private LocalDateTime buyDate;

    public UserBikeMapping(String id, User user, Bike bike, LocalDateTime rentStartDate,
                           LocalDateTime rentEndDate, LocalDateTime buyDate) {
        super(id);

        this.setUser(user);
        this.setBike(bike);
        this.setRentStartDate(rentStartDate);
        this.setRentEndDate(rentEndDate);
        this.setBuyDate(buyDate);
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Bike getBike() {
        return this.bike;
    }

    public void setBike(Bike bike) {
        this.bike = bike;
    }

    public LocalDateTime getRentStartDate() {
        return this.rentStartDate;
    }

    public void setRentStartDate(LocalDateTime rentStartDate) {
        this.rentStartDate = rentStartDate;
    }

    public LocalDateTime getRentEndDate() {
        return this.rentEndDate;
    }

    public void setRentEndDate(LocalDateTime rentEndDate) {
        this.rentEndDate = rentEndDate;
    }

    public LocalDateTime getBuyDate() {
        return this.buyDate;
    }

    public void setBuyDate(LocalDateTime buyDate) {
        this.buyDate = buyDate;
    }
}
