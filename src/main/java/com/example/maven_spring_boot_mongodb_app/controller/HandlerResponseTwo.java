package com.example.maven_spring_boot_mongodb_app.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("app/controller")
public class HandlerResponseTwo {
    @GetMapping("getDataTwo")
    @ResponseBody
    public Map<String, Object> getDataTwo() {
        return createData();
    }

//    ResponseEntity 专门第一个 空类
    @GetMapping("getDataThree")
    public ResponseEntity<Map<String, Object>> getDataThree() {
//        return new ResponseEntity<>(createData(), HttpStatus.OK);
        return ResponseEntity.ok(createData());
    }
    public static Map<String, Object> createData() {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("title","this is book");
        data.put("view",0);
        map.put("name","zhangsan");
        map.put("age",18);
        map.put("createDate",new Date().getTime());
        map.put("data", data);
        return map;
    }

    @GetMapping("getDataFour")
    public ResponseEntity<Map<String, Object>> getDataFour() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Custom-Header", "customValue");
        headers.add("Location", "http://example.com");
        return new ResponseEntity<>(createData(), headers, HttpStatus.CREATED);
    }

    @GetMapping("getDataFive")
    public ResponseEntity<Map<String, Object>> getDataFive() {
        ResponseEntity<Map<String, Object>> response = ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .header("X-Custom-Header", "customValue")
                .body(createData());
        return response;
    }
}
