package com.example.maven_spring_boot_mongodb_app.actorManagerServer;

public class ActorDoc {
    private String name;
    private String videoCode;
    private String types;
    private String filesNameSuffix;
    private String remark;
    private String fileUrl;

    public String getName() {
        return name;
    }
    public String getVideoCode(){return videoCode;}
    public String getTypes(){return types;}
    public String getFilesNameSuffix(){return filesNameSuffix;}
    public String getRemark(){return remark;}
    public String getFileUrl(){return fileUrl;}
}
