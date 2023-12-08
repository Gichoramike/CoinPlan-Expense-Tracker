package com.example.coinplan;

public class expense {
    String kshAmount;
    String category;
    String description;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    String key;

    public expense(){

    }



    public expense(String kshAmount, String category, String description) {
        this.kshAmount = kshAmount;
        this.category = category;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
