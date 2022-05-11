package com.example.foodscanner;

public class BarcodeList {

    private int id;
    private String brand;
    private String name;
    private String barcode;

    public BarcodeList(int id, String brand, String name, String barcode) {
        this.id = id;
        this.brand = brand;
        this.name = name;
        this.barcode = barcode;
    }

    @Override
    public String toString() {
        return "BarcodeList{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", name='" + name + '\'' +
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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
