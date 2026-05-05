package com.example.masroofy.Model.Entity;

public class Category {
    private String categoryName;

    public Category(){

    }

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setCategoryName(String categoryName){
        this.categoryName = categoryName;
    }

    public String getCategoryName(){
        return this.categoryName;
    }
}