package com.example.greenbike.database.models.bike;

import com.example.greenbike.database.models.BaseModel;

public class Bike extends BaseModel {
    private String model;
    private BikeBrand bikeBrand;
    private BikeMaterial bikeMaterial;
    private BikeCategory bikeCategory;
    private String imageURL;
    private Boolean isForRent;
    private double price;

    public Bike(String id, BikeBrand bikeBrand, BikeMaterial bikeMaterial, BikeCategory bikeCategory,
                String imageURL, Boolean isForRent, double price) {
        super(id);

        this.setBikeBrand(bikeBrand);
        this.setBikeMaterial(bikeMaterial);
        this.setBikeCategory(bikeCategory);
        this.setImageURL(imageURL);
        this.setIsForRent(isForRent);
        this.setPrice(price);
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public BikeBrand getBikeBrand() {
        return this.bikeBrand;
    }

    public void setBikeBrand(BikeBrand bikeBrand) {
        this.bikeBrand = bikeBrand;
    }

    public BikeMaterial getBikeMaterial() {
        return this.bikeMaterial;
    }

    public void setBikeMaterial(BikeMaterial bikeMaterial) {
        this.bikeMaterial = bikeMaterial;
    }

    public BikeCategory getBikeCategory() {
        return this.bikeCategory;
    }

    public void setBikeCategory(BikeCategory bikeCategory) {
        this.bikeCategory = bikeCategory;
    }

    public String getImageURL() {
        return this.imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Boolean getIsForRent() {
        return this.isForRent;
    }

    public void setIsForRent(Boolean forRent) {
        this.isForRent = forRent;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
