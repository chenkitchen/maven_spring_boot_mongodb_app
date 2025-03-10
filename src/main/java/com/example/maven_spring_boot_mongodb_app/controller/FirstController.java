package com.example.maven_spring_boot_mongodb_app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("app/controller")
public class FirstController {
    @RequestMapping("getJson")
    public String getJson() {
        Map<String ,String> map = new HashMap<>();
        map.put("id", "pro-0001");
        map.put("name","zhangsan");
        return map.toString();
    }
}
