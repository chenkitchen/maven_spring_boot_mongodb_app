package com.example.maven_spring_boot_mongodb_app.components;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CommonUtils {
    public void addId(List<Document> docs){
        for(Document doc : docs){
            ObjectId id = doc.getObjectId("_id");
            doc.remove("_id");
            doc.append("id",id.toString());
        }
    }

    // 统一的response
    public Map<String, Object> createResponse(String msg){
        Map<String, Object> mapI = new HashMap<>();
        mapI.put("msg",msg);
        return this.createResponse(mapI);
    }

    public Map<String, Object> createResponse(List<Document> docs,long total){
        Map<String, Object> mapI = new HashMap<>();
        mapI.put("dataList",docs);
        mapI.put("total", total);
        return this.createResponse(mapI);
    }

    public ResponseEntity<Map<String, Object>> createResponse(List<Document> docs,long total,String header){
        ResponseEntity<Map<String, Object>> response = ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .header("X-Custom-Header", header)
                .body(this.createResponse(docs,total));
        return response;
    }

    public Map<String, Object> createResponse(Map<String,Object> mapI){
        Map<String, Object> map = new HashMap<>();
        map.put("states", "success");
        map.put("data", mapI);
        map.put("err",0);
        return map;
    }
}
