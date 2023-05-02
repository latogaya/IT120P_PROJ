package com.models;

import java.util.List;

public class Product {
    private String clothes_name;
    private String clothes_brand;
    private String clothes_price;
    private String clothes_size;
    private String clothes_condition;
    private String clothes_type;
    private String description_content;
    private String clothes_image;

    public Product(String clothes_name, String clothes_brand, String clothes_price, String clothes_size, String clothes_condition, String clothes_type, String description_content, String clothes_image) {
        this.clothes_name = clothes_name;
        this.clothes_brand = clothes_brand;
        this.clothes_price = clothes_price;
        this.clothes_size = clothes_size;
        this.clothes_condition = clothes_condition;
        this.clothes_type = clothes_type;
        this.description_content = description_content;
        this.clothes_image = clothes_image;
    }

    public String getClothes_name() {
        return clothes_name;
    }

    public void setClothes_name(String clothes_name) {
        this.clothes_name = clothes_name;
    }

    public String getClothes_brand() {
        return clothes_brand;
    }

    public void setClothes_brand(String clothes_brand) {
        this.clothes_brand = clothes_brand;
    }

    public String getClothes_price() {
        return clothes_price;
    }

    public void setClothes_price(String clothes_price) {
        this.clothes_price = clothes_price;
    }

    public String getClothes_size() {
        return clothes_size;
    }

    public void setClothes_size(String clothes_size) {
        this.clothes_size = clothes_size;
    }

    public String getClothes_condition() {
        return clothes_condition;
    }

    public void setClothes_condition(String clothes_condition) {this.clothes_condition = clothes_condition;}

    public String getClothes_type() {
        return clothes_type;
    }

    public void setClothes_type(String clothes_type) {
        this.clothes_type = clothes_type;
    }

    public String getDescription_content() {
        return description_content;
    }

    public void setDescription_content(String description_content) {this.description_content = description_content;}

    public String getClothes_image() {
        return clothes_image;
    }

    public void setClothes_image(String clothes_image) {
        this.clothes_image = clothes_image;
    }
}