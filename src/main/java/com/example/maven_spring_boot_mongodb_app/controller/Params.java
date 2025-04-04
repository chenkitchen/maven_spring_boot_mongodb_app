package com.example.maven_spring_boot_mongodb_app.controller;
//import lombok.Data //可能是 一个自动生成 getter 方法的 库

import com.example.maven_spring_boot_mongodb_app.actorManagerServer.ActorDoc;

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

    //  actor add
    private String videoCode;
    private String types;
    private String filesNameSuffix;
    private String remark;
    private String fileUrl;

    private List<ActorDoc> list;

    public String getVideoCode(){return videoCode;}
    public String getTypes(){return types;}
    public String getFilesNameSuffix(){return filesNameSuffix;}
    public String getRemark(){return remark;}
    public String getFileUrl(){return fileUrl;}

    public List<ActorDoc> getList() {
        return list;
    }

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
