package com.models;

public class Product {

    private int clothes_id;
    private String clothes_name;
    private String clothes_brand;
    private double clothes_price;
    private String clothes_size;
    private String clothes_condition;
    private String clothes_type;
    private String description_content;
    private byte[] clothes_image;

    public Product(int id, String name, String brand, double price, String size, String condition, String type, String description, byte[] image) {
        this.clothes_id = id;
        this.clothes_name = name;
        this.clothes_brand = brand;
        this.clothes_price = price;
        this.clothes_size = size;
        this.clothes_condition = condition;
        this.clothes_type = type;
        this.description_content = description;
        this.clothes_image = image;
    }

    public int getId() {
        return clothes_id;
    }

    public void setId(int id) {
        this.clothes_id = id;
    }

    public String getName() {
        return clothes_name;
    }

    public void setName(String name) {
        this.clothes_name = name;
    }

    public String getBrand() {
        return clothes_brand;
    }

    public void setBrand(String brand) {
        this.clothes_brand = brand;
    }

    public double getPrice() {
        return clothes_price;
    }

    public void setPrice(double price) {
        this.clothes_price = price;
    }

    public String getSize() {
        return clothes_size;
    }

    public void setSize(String size) {
        clothes_size = size;
    }

    public String getCondition() {
        return clothes_condition;
    }

    public void setCondition(String condition) {
        clothes_condition = condition;
    }

    public String getType() {
        return clothes_type;
    }

    public void setType(String type) {
        this.clothes_type = type;
    }

    public String getDescription() {
        return description_content;
    }

    public void setDescription(String description) {
        this.description_content = description;
    }

    public byte[] getImage() {
        return clothes_image;
    }

    public void setImage(byte[] image) {
        this.clothes_image = image;
    }
}