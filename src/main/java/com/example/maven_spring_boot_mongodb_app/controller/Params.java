package com.example.maven_spring_boot_mongodb_app.controller;
//import lombok.Data //可能是 一个自动生成 getter 方法的 库

import java.util.List;

//@Data // lombok.Data库已 注释的方式进行使用
public class Params {
    private String name;
    private String id;
    private String type;
    private String title;
    private List<String> tags;
    private int currentPage;
    private int pageSize;
    public String getName() {
        return name;
    }
    public String getId() {
        return id;
    }
    public String getType() {
        return type;
    }
    public String getTitle() {
        return title;
    }
    public List<String> getTags() {
        return tags;
    }
    public int getCurrentPage() {
        return currentPage;
    }
    public int getPageSize() {
        return pageSize;
    }
}
