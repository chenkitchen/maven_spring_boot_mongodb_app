package com.example.maven_spring_boot_mongodb_app.controller;

import org.bson.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseData {
    private String status;
    private String message;
    private List<Document> doc;
    public ResponseData(String status, String message, List<Document> doc) {
        this.status = status;
        this.message = message;
        this.doc = doc;
    }
    public Map getResponse() {
        Map<String, String> map = new HashMap<>();
        map.put("status", status);
        map.put("message", message);
        map.put("doc", doc.toString());
        return map;
    }
}
