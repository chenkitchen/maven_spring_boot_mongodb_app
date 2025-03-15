package com.example.maven_spring_boot_mongodb_app.logConsole;

// 监听服务的 接口时间过长的情况
import com.mongodb.event.CommandFailedEvent;
import com.mongodb.event.CommandListener;
import com.mongodb.event.CommandStartedEvent;
import com.mongodb.event.CommandSucceededEvent;
import org.bson.BsonDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit; //时间单位

public class SlowLogLis implements CommandListener {
    private static final Logger logger = LoggerFactory.getLogger(SlowLogLis.class);
//    命令表的最大大小
    public static final Long DEFAULT_LIMIT = 100000L;
// 命令记录表
    private Map<Integer , BsonDocument> commands = new ConcurrentHashMap<Integer, BsonDocument>();

//    时延 阀值
    private long maxMs;
    private long overLimit; //避免出现命令拥塞时在内存中积压大量的命令

    public SlowLogLis(long maxMs) {
        this(maxMs, DEFAULT_LIMIT);
    }

    public SlowLogLis(long maxMs, Long overLimit) {
        this.maxMs = maxMs;
        this.overLimit = overLimit;
    }

//    监听命令启动事件
    @Override
    public void commandStarted(CommandStartedEvent event){
        if(commands.size() >= overLimit){
            return;
        }
        commands.put(event.getRequestId(),event.getCommand());
    }

//    监听命令完成事件
    @Override
    public void commandSucceeded(CommandSucceededEvent event){
        long elapsaTime = event.getElapsedTime(TimeUnit.MILLISECONDS);
        if(elapsaTime < maxMs){
            return;
        }
        BsonDocument command = commands.get(event.getRequestId());
        if(command == null){ return; }
        logger.info("command finished - {} , speed {} ms, detail: {} ", event.getRequestId(), elapsaTime, toJson(command) ); // toJson 没有处理 null的情况
        commands.remove(event.getRequestId());
    }

//    监听命令失败事件
    public void commandFailed(CommandFailedEvent event){
        long elapsaTime = event.getElapsedTime(TimeUnit.MILLISECONDS);
        if(elapsaTime < maxMs){
            return;
        }
        BsonDocument command = commands.get(event.getRequestId());
        if(command == null){ return; }
        logger.info("command failed - {} , speed {} ms, detail: {} ", event.getRequestId(), elapsaTime, toJson(command));
        commands.remove(event.getRequestId());
    }

    private String toJson(BsonDocument doc) {
        if(doc == null){ return "EMPTY {}"; }
        try {
            return doc.toJson();
        } catch(Exception e) {
            return "faild by" + e.getMessage();
        }
    }
}
