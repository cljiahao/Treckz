package com.example.foodscanner;

public class AddList {

    private String brand;
    private String name;
    private String date;
    private String quantity;
    private String barcode;

    public AddList(String brand, String name, String date, String quantity, String barcode) {
        this.brand = brand;
        this.name = name;
        this.date = date;
        this.quantity = quantity;
        this.barcode = barcode;
    }

    @Override
    public String toString() {
        return "AddList{" +
                "brand='" + brand + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", quantity='" + quantity + '\'' +
                ", barcode='" + barcode + '\'' +
                '}';
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
