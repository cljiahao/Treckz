package com.example.foodscanner;

public class InfoList {

    private int id;
    private String brand;
    private String name;
    private String date;
    private String totalquantity;
    private String quantity;
    private String barcode;
    private boolean isExpanded;

    public InfoList(int id, String brand, String name, String date, String totalquantity, String quantity, String barcode) {
        this.id = id;
        this.brand = brand;
        this.name = name;
        this.date = date;
        this.totalquantity = totalquantity;
        this.quantity = quantity;
        this.barcode = barcode;
        this.isExpanded = false;
    }

    @Override
    public String toString() {
        return "InfoList{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", totalquantity='" + totalquantity + '\'' +
                ", quantity='" + quantity + '\'' +
                ", barcode='" + barcode + '\'' +
                ", isExpanded=" + isExpanded +
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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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

    public String getTotalquantity() {
        return totalquantity;
    }

    public void setTotalquantity(String totalquantity) {
        this.totalquantity = totalquantity;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

}
