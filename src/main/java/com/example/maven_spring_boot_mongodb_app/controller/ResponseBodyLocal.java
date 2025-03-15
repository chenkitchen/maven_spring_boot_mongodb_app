package com.example.maven_spring_boot_mongodb_app.controller;

import java.util.Map;

//没用上，直接使用 Map<String,Object> 这种广泛的定义
public class ResponseBodyLocal {
    private String states;
    private Map<String,String> data;
    public String getStates() {
        return states;
    }
    public Map<String,String> getData() {
        return data;
    }
    public void setStates(String states) {
        this.states = states;
    }
    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
