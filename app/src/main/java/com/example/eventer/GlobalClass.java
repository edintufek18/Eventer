package com.example.eventer;

import android.app.Application;

public class GlobalClass extends Application {
    private String token = "";
    private String category_id = "";
    private String subcategory_id = "";
    private String opens_at = "";
    private String closes_at = "";
    private String amount = "";

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public String getCategory(){
        return category_id;
    }
    public void setCategory(String category_id){
        this.category_id = category_id;
    }

    public String getSubcategory(){
        return subcategory_id;
    }
    public void setSubcategory(String subcategory_id){
        this.subcategory_id = subcategory_id;
    }

    public String getCloses_at(){
        return closes_at;
    }
    public void setCloses_at(String closes_at){
        this.closes_at = closes_at;
    }

    public void setOpens_at(String opens_at){
        this.opens_at = opens_at;
    }
    public String getOpens_at(){
        return opens_at;
    }

    public String getAmount(){
        return amount;
    }
    public void setAmount(String amount){
        this.amount = amount;
    }
}
