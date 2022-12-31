package com.example.eventer;

import android.app.Application;

public class GlobalClass extends Application {
    private String token = "";
    private String category_id = "";
    private String subcategory_id = "";

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setCategory(String category_id){
        this.category_id = category_id;
    }

    public void setSubcategory(String subcategory_id){
        this.subcategory_id = subcategory_id;
    }

    public String getCategory(){
        return category_id;
    }

    public String getSubcategory(){
        return subcategory_id;
    }
}
