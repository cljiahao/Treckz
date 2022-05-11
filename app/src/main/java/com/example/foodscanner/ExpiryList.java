package com.example.foodscanner;

public class ExpiryList {

    private int id;
    private String brand;
    private String name;
    private String date;
    private String quantity;
    private String barcode;

    public ExpiryList(int id, String brand, String name, String date, String quantity, String barcode) {
        this.id = id;
        this.brand = brand;
        this.name = name;
        this.date = date;
        this.quantity = quantity;
        this.barcode = barcode;
    }

    @Override
    public String toString() {
        return "ExpiryList{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", quantity='" + quantity + '\'' +
                ", barcode='" + barcode + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
