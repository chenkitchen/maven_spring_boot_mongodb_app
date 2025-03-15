package com.example.maven_spring_boot_mongodb_app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("app/controller")
public class HandlerResponse {
    @GetMapping("getData")
    public Map<String,String> getData() {
        Map<String ,String> map = new HashMap<>();
        map.put("id", "pro-0001");
        map.put("name","zhangsan");
        return map;
    }
}
