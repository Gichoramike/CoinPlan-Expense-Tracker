package com.example.coinplan;

public class Incomes {
    String kshAmounts;
    String category;
    String description;


    public Incomes(){

    }
    public Incomes(String kshAmounts, String category, String description) {
        this.kshAmounts = kshAmounts;
        this.category = category;
        this.description = description;
    }

    public String getKshAmounts() {
        return kshAmounts;
    }

    public void setKshAmounts(String kshAmounts) {
        this.kshAmounts = kshAmounts;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}