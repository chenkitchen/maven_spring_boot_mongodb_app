package com.example.maven_spring_boot_mongodb_app.controller;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateParams {
    private String name;
    private String types;
    private String filesNameSuffix;
    private String remark;
    private String fileUrl;
    private String id;

    public boolean isName(){
        return name!=null;
    }
    public boolean isTypes(){
        return types!=null;
    }
    public boolean isSuffix(){
        return filesNameSuffix!=null;
    }
    public boolean isRemark(){
        return remark!=null;
    }
    public boolean isFileUrl(){
        return fileUrl!=null;
    }
    public boolean isId(){
        return id!=null;
    }
}
