package com.example.coinplan;

public class Expenses {
    public Expenses(String kshAmount, String category, String descripiton) {
        this.kshAmount = kshAmount;
        this.category = category;
        this.descripiton = descripiton;

    }

    public Expenses(){

    }

    public String getKshAmount() {
        return kshAmount;
    }

    public void setKshAmount(String kshAmount) {
        this.kshAmount = kshAmount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescripiton() {
        return descripiton;
    }

    public void setDescripiton(String descripiton) {
        this.descripiton = descripiton;
    }

    public String kshAmount;
    public String category;
    public String descripiton;
}
