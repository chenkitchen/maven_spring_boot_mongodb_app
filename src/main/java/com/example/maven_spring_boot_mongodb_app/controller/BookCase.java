package com.example.maven_spring_boot_mongodb_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BookCase {
    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "hello world how are you ！！！";
    }
}
