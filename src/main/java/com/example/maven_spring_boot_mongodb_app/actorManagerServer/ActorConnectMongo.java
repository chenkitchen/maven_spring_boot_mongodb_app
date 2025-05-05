package com.example.maven_spring_boot_mongodb_app.actorManagerServer;

import com.example.maven_spring_boot_mongodb_app.components.CommonUtils;
import com.example.maven_spring_boot_mongodb_app.controller.Params;

import com.example.maven_spring_boot_mongodb_app.controller.UpdateParams;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("app/actor")
public class ActorConnectMongo {
    public static MongoClient mongoClient;
    public static MongoDatabase mongoDatabase;
    public static MongoCollection<Document> mongoCollection;


    @Autowired
    private CommonUtils commonUtils;

    @PostMapping("/getAllData")
    public @ResponseBody ResponseEntity getAllData(@RequestBody Params pages) {
        createMongoConfig("actor_manage_system");
        int pageC = pages.getCurrentPage();
        int pageSize = pages.getPageSize();
        FindIterable<Document> doci = mongoCollection.find(
                        Filters.empty(), //查所有数据
                        Document.class
                )
                .sort(Sorts.ascending("updateAt"))
                .skip((pageC - 1) * pageSize)
                .limit(pageSize)
                .projection(
                        new Document("updateAt", 1)
                                .append("createAt", 1)
                                .append("name", 1)
                                .append("videoCode", 1)
                                .append("types", 1)
                                .append("filesNameSuffix", 1)
                                .append("remark", 1)
                                .append("fileUrl",1)
                                .append("_id",1) //默认是 1 因此要去掉
                );
        List<Document> doc = doci.into(new ArrayList<>());
        commonUtils.addId(doc);
        commonUtils.transfromTime(doc,"updateAt");
        commonUtils.transfromTime(doc,"createAt");
        long total = mongoCollection.countDocuments();
        mongoClient.close();
        return commonUtils.createResponse(doc,total,"customValue");
    }

    @PostMapping("/getMagnetData")
    public @ResponseBody ResponseEntity getMagnetData(@RequestBody Params pages) {
        createMongoConfig("av_title_json_collect");
        int pageC = pages.getCurrentPage();
        int pageSize = pages.getPageSize();
        FindIterable<Document> doci = mongoCollection.find(
                        Filters.empty(), //查所有数据
                        Document.class
                )
                .skip((pageC - 1) * pageSize)
                .limit(pageSize)
                .projection(
                        new Document("page_name", 1)
                                .append("video_size", 1)
                                .append("video_title", 1)
                                .append("video_url", 1)
                                .append("_id",1) //默认是 1 因此要去掉
                );
        List<Document> doc = doci.into(new ArrayList<>());
        commonUtils.addId(doc);
        long total = mongoCollection.countDocuments();
        mongoClient.close();
        return commonUtils.createResponse(doc,total,"customValue");
    }
//    @GetMapping(value="/findMagnetCode",params = {"title"})
//    public @ResponseBody ResponseEntity getMagnetCode(String title) {
//        return getMagnetCode(title,1,10); //函数重载 解决默认入参问题，在这里没效果
//    }
    @GetMapping(value="/findMagnetCode")
    public @ResponseBody ResponseEntity getMagnetCode(
            @RequestParam(required = true) String title, //必须写 require为true
            @RequestParam(required = false, defaultValue = "1") String pageNo,
            @RequestParam(required = false, defaultValue = "10") String pageSize
    ){
        createMongoConfig("av_title_json_collect");
        int pageC = Integer.parseInt(pageNo);
        int pageS = Integer.parseInt(pageSize);
        FindIterable<Document> dociSize = mongoCollection.find(
                        Filters.regex("video_title", commonUtils.escapeRegex(title)), //模糊匹配
                        Document.class
                )
                .projection(
                        new Document("page_name", 1)
                                .append("video_size", 1)
                                .append("video_title", 1)
                                .append("video_url", 1)
                                .append("_id",1) //默认是 1 因此要去掉
                        // 这里的 _id 还不能直接被 前端使用
                );
        long total = dociSize.into(new ArrayList<>()).size(); // count已经被弃用
        FindIterable<Document> doci = dociSize.skip((pageC - 1) * pageS)
                .limit(pageS);
        List<Document> doc = doci.into(new ArrayList<>());
        commonUtils.addId(doc);
        mongoClient.close();
        return commonUtils.createResponse(doc,total,"customValue");
    }

    @PostMapping("/insertMany")
    public @ResponseBody ResponseEntity insertMany(@RequestBody Params pages) {
        createMongoConfig("actor_manage_system");
        List<ActorDoc> list = pages.getList();
        List<Document> docs = new ArrayList<>();
        list.forEach(doc -> {
            Date currentDate = new Date();
            Document item = new Document("name", doc.getName())
                    .append("videoCode", doc.getVideoCode())
                    .append("types", doc.getTypes())
                    .append("filesNameSuffix", doc.getFilesNameSuffix())
                    .append("remark", doc.getRemark())
                    .append("fileUrl", doc.getFileUrl())
                    .append("createAt", currentDate)
                    .append("updateAt", currentDate);
            docs.add(item);
        });
        mongoCollection.insertMany(docs);
        return commonUtils.createResponse(new ArrayList<>(),docs.size(),"customValue");
    }
    @PostMapping("/insertOne")
    public Map<String, Object> insertOne(@RequestBody Params pages) {
        createMongoConfig("actor_manage_system");
        Date currentDate = new Date();
        Document doc = new Document("name", pages.getName())
                .append("videoCode", pages.getVideoCode())
                .append("types", pages.getTypes())
                .append("filesNameSuffix", pages.getFilesNameSuffix())
                .append("remark", pages.getRemark())
                .append("fileUrl", pages.getFileUrl())
                .append("createAt", currentDate)
                .append("updateAt", currentDate);
        mongoCollection.insertOne(doc);
        String id = doc.getObjectId("_id").toString();
        mongoClient.close();
//        return createSuccessBody(id);
        Map<String, Object> mapI = new HashMap<>();
        mapI.put("callbackId",id);
        return commonUtils.createResponse(mapI);
    }

//    public ResponseEntity<Map<String, Object>> createResponse(List<Document> docs, long total){
//        ResponseEntity<Map<String, Object>> response = ResponseEntity
//                .status(HttpStatus.ACCEPTED)
//                .header("X-Custom-Header", "customValue")
//                .body(commonUtils.createResponse(docs,total));
//        return response;
//    }
//    public static  Map<String, Object> createBody(List<Document> docs,long total) {
//        Map<String, Object> map = new HashMap<>();
//        Map<String, Object> mapI = new HashMap<>();
//        mapI.put("dataList",docs);
//        mapI.put("total", total);
//        map.put("states", "success");
//        map.put("data",mapI);
//        map.put("err",0);
//        return map;
//    }

    public static void createMongoConfig(String collecName) {
        MongoClientSettings.Builder settings = MongoClientSettings.builder();
        settings.applyConnectionString(new ConnectionString("mongodb://admin:12345678@localhost:27017")); //将请求url 和 setter 通过管道进行调用

        mongoClient = MongoClients.create(settings.build());
        mongoDatabase = mongoClient.getDatabase("av_data_json");
        mongoCollection = mongoDatabase.getCollection(collecName);
    }

//    public static Map<String, Object> createSuccessBody(String str) {
//        Map<String, Object> map = new HashMap<>();
//        Map<String, Object> mapI = new HashMap<>();
//        mapI.put("callbackId",str);
//        map.put("states", "success");
//        map.put("data", mapI);
//        map.put("err",0);
//        return map;
//    }

    @GetMapping(value="/findActor",params = {"name"})
    public @ResponseBody ResponseEntity getActor(String name){
        createMongoConfig("actor_manage_system");
        FindIterable<Document> doci = mongoCollection.find(
                        Filters.regex("name", commonUtils.escapeRegex(name)), //模糊匹配
                        Document.class
                )
                .sort(Sorts.ascending("updateAt"))
                .projection(
                        new Document("updateAt", 1)
                                .append("createAt", 1)
                                .append("name", 1)
                                .append("videoCode", 1)
                                .append("types", 1)
                                .append("filesNameSuffix", 1)
                                .append("remark", 1)
                                .append("fileUrl",1)
                                .append("_id",1) //默认是 1 因此要去掉
                        // 这里的 _id 还不能直接被 前端使用
                );
        List<Document> doc = doci.into(new ArrayList<>());
//        for(Document document : doc){
//            ObjectId id = document.getObjectId("_id");
//            document.remove("_id");
//            document.append("id",id.toString());
//        }
        commonUtils.addId(doc);
        commonUtils.transfromTime(doc,"updateAt");
        commonUtils.transfromTime(doc,"createAt");
        long total =doc.size();
        mongoClient.close();
        return commonUtils.createResponse(doc,total,"customValue");
    }
    @GetMapping("findActor")
    public Map<String, Object> getNoActor(){
//        return createSuccessBody("未入参");
        return commonUtils.createResponse("未入参");
    }

    @GetMapping(value="/findVideoCode",params = {"name"})
    public @ResponseBody ResponseEntity getVideoCode(String name){
        createMongoConfig("actor_manage_system");
        FindIterable<Document> doci = mongoCollection.find(
                        Filters.regex("videoCode", commonUtils.escapeRegex(name) ), //模糊匹配
                        Document.class
                )
                .sort(Sorts.ascending("updateAt"))
                .projection(
                        new Document("updateAt", 1)
                                .append("createAt", 1)
                                .append("name", 1)
                                .append("videoCode", 1)
                                .append("types", 1)
                                .append("filesNameSuffix", 1)
                                .append("remark", 1)
                                .append("fileUrl",1)
                                .append("_id",1) //默认是 1 因此要去掉
                );
        List<Document> doc = doci.into(new ArrayList<>());
        commonUtils.addId(doc);
        commonUtils.transfromTime(doc,"updateAt");
        commonUtils.transfromTime(doc,"createAt");
        long total =doc.size();
        mongoClient.close();
        return commonUtils.createResponse(doc,total,"find_my");
    }
    @GetMapping("findVideoCode")
    public Map<String, Object> getNoVideoCode(){
        return commonUtils.createResponse("未入参");
    }

    @PostMapping(value="/updateActor")
    public Map<String, Object> updateActor(@RequestBody UpdateParams pages) {
        createMongoConfig("actor_manage_system");
//        FindIterable<Document> doci = mongoCollection.find(
//                Filters.eq("_id",new ObjectId(pages.getId())), //查所有数据
//                Document.class
//        );
        if(!pages.isId()) return commonUtils.createResponse("修改失败，无id入参");
        String id = pages.getId();
        Bson doci;
        System.out.println(id.length());
        if(id.length() == 24 && id.matches("[0-9a-fA-F]+")){
            doci = Filters.eq("_id",new ObjectId(pages.getId()));
        }else{
            doci = Filters.eq("_id",id);
            System.err.println("Invalid hex string! 入参id有问题");
        }
//        Bson doci = Filters.eq("_id",new ObjectId(pages.getId()));
//        Bson update = Updates.set("name", pages.getName());
        Date updateT = new Date();
        //创建修改多个字段
//        Bson update = Updates.combine(
//                Updates.set("name", pages.getName()),
//                Updates.set("updateAt", updateT)
//        );
        Map<String, Object> params = new HashMap<>();
        if(pages.isName()) params.put("name",pages.getName());
        if(pages.isTypes()) params.put("types",pages.getTypes());
        if(pages.isFileUrl()) params.put("fileUrl",pages.getFileUrl());
        if(pages.isRemark()) params.put("remark",pages.getRemark());
        if(pages.isSuffix()) params.put("filesNameSuffix",pages.getFilesNameSuffix());
        params.put("updateAt",updateT);
        Bson update =  commonUtils.changeParamsHandler(params);
        UpdateResult result = mongoCollection.updateOne(doci, update);
        return commonUtils.createResponse("修改了 " + result.getMatchedCount() + "个文档");
    }
}
