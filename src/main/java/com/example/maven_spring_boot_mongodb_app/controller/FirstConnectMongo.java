package com.example.maven_spring_boot_mongodb_app.controller;

import com.example.maven_spring_boot_mongodb_app.logConsole.PoolLogLis;
import com.example.maven_spring_boot_mongodb_app.logConsole.SlowLogLis;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable; //高效查询
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.management.JMXConnectionPoolListener;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;

import org.bson.Document;

import java.util.*;

@RestController
@RequestMapping("app/controller")
public class FirstConnectMongo {

    @PostMapping("/find")
    public String find(@RequestBody Params json) {
        createMongoConfig("test_connect");
//        System.out.println(json);
//        Map<String, String> map = new HashMap<>();
//        map.put("name", json);
        Document filter = new Document("name", new Document("$regex", json.getName())); //模糊匹配 正则匹配
        List<Document> doc = mongoCollection.find(filter).into(new ArrayList<>());
        mongoClient.close();
//        System.out.println(doc);
//        System.out.println(filter);
        return doc.toString();
//        return map.toString();
    }

    @PostMapping("/insert")
    public String insert() {
        createMongoConfig("articles_test");
        Date currentDate = new Date();
        Document doc = new Document("title", "tdfdfransfdddorm是否值233得去看看").append("author", new Document("name", "wuwua").append("gender", "female"))
                .append("summary", "次选定要设置断选定要设置断点的代码行，在行号的区域后面单击鼠标左键即可。点的代码行，在行号的区域后面单击鼠标左键即可。序更新选定要设置断点的代码行，在行号的区域后面单击鼠标左键即可。")
                .append("type", "小dddd说")
                .append("tags", Arrays.asList("SSS", "巅峰赛", "??"))
                .append("views", 0)
                .append("updateAt", currentDate)
                .append("createAt", currentDate);
        mongoCollection.insertOne(doc);
        String id = doc.getObjectId("_id").toString(); // _id 是比较特殊的字段
//        其他字段可以直接用 get("字段名")
//        mongoClient.close();
//        Map<String, String> map = new HashMap<>();
//        map.put("states","success");
//        map.put("message","ok"); //使用函数
//        return map.toString();
        return createSuccessBody(id);
    }

    @PostMapping("/findTitle")
    public ResponseEntity<Map<String, Object>> findTitle(@RequestBody Params id) {
        createMongoConfig("articles_test");
//        System.out.println(id.getId());
        FindIterable<Document> doci = mongoCollection.find(
                Filters.eq("_id", new ObjectId(id.getId())),
                Document.class
        );
//        mongoClient.close(); //doci 依然是在执行 MongoDB 客户端，因此不能 关闭
//        System.out.println(doci);
//        doci.forEach((Block<Document>)item -> {
//            System.out.println(item.toJson());
//        });
//        for (Document doc : doci) {
//            System.out.println(doc);
//        }
        Document doc = doci.first();
//        System.out.println(doc);
//        mongoClient.close();
        return createResponse(doc);
    }

    @PostMapping("/simpleFind")
    @ResponseBody
    public ResponseData simpleFind(@RequestBody Params type) {
        createMongoConfig("articles_test");
        FindIterable<Document> doci = mongoCollection.find(
                        Filters.eq("type", type.getType()),
                        Document.class
                ).projection(new Document("title", 1).append("createAt", 1).append("author.name", 1))
                .sort(Sorts.ascending("createAt"))
                .limit(2);

        List<Document> doc = doci.into(new ArrayList<>());
        mongoClient.close();
        ResponseData res = new ResponseData("success", "ok", doc);
//        return createSuccessBody(doc.toString());
        return res;
    }

    public static MongoClient mongoClient;
    public static MongoDatabase mongoDatabase;
    public static MongoCollection<Document> mongoCollection;

    public static String createSuccessBody(String str) {
        Map<String, String> map = new HashMap<>();
        map.put("states", "success");
        map.put("message", str);
        return map.toString();
    }

    public static void createMongoConfig(String collecName) {
        MongoClientSettings.Builder settings = MongoClientSettings.builder();
        PoolLogLis logLis = new PoolLogLis();


        settings.addCommandListener(new SlowLogLis(0))
                .applyConnectionString(new ConnectionString("mongodb://localhost:27017")); //将请求url 和 setter 通过管道进行调用

        mongoClient = MongoClients.create(settings.build());
        mongoDatabase = mongoClient.getDatabase("my_first_json_data");
        mongoCollection = mongoDatabase.getCollection(collecName);

        logLis.start();
//        添加 jmx 监听
        settings.applyToConnectionPoolSettings( builder -> {
            builder.addConnectionPoolListener(new JMXConnectionPoolListener());
        });


    }

    //    public class Params {
//        private String name;
//        public void setName(String name) {
//            this.name = name;
//        }
//        public String getName() {
//            return name;
//        }
//    }
    public static ResponseEntity<Map<String, Object>> createResponse(Document doc) {
        ResponseEntity<Map<String, Object>> response = ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .header("X-Custom-Header", "customValue")
                .body(createBody(doc));
        return response;
    }

    public static Map<String, Object> createBody(Document doc) {
        Map<String, Object> map = new HashMap<>();
        map.put("states", "success");
//        map.put("data",doc.toMap()); // org.mongodb 4.8.1版本支持
        map.put("data",localToMap(doc));
        return map;
    }

    // Document 类型变 Map类型的方法
    public static Map<String, Object> localToMap(Document doc) {
        Map<String, Object> map = new HashMap<>();
        for (String key : doc.keySet()) {
            if (doc.get(key) instanceof Document) {
                map.put(key, localToMap((Document) doc.get(key)));
            } else if (doc.get(key) instanceof List) {
                map.put(key,doc.get(key));
            } else if(doc.get(key) != null) {
                map.put(key, doc.get(key).toString());
            } else {
                map.put(key, null);
            }
        }
        return map;
    }

    //修改操作
    @PostMapping("/updataType")
    public ResponseEntity<String> updata(@RequestBody Params type) {
        createMongoConfig("articles_test");
        UpdateResult updateResult = mongoCollection.updateOne(
            Filters.eq("_id", new ObjectId(type.getId())),
                Updates.set("type", type.getType())
        );
        mongoClient.close();
        return ResponseEntity.ok(updateResult.toString());
    }
//    修改多个字段
//    只会关注注册的字段
    @PostMapping("/updataManyKey")
    public ResponseEntity<String> updataMany(@RequestBody Params params) {
        createMongoConfig("articles_test");
        UpdateResult updateResult = mongoCollection.updateOne(
                Filters.eq("_id", new ObjectId(params.getId())),
                Updates.combine(
                        Updates.set("title", params.getTitle()),
//                        updates.push("tags",params.getTags()),// 添加单个元素给一个list
                        Updates.addEachToSet("tags", params.getTags()), //添加list类型的
                        Updates.currentDate("updateAt") // 更新修改时间
                )
        );
        mongoClient.close();
        return ResponseEntity.ok("upDate result:" + updateResult.getMatchedCount());
    }
}
