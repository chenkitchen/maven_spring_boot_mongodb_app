package com.example.maven_spring_boot_mongodb_app.logConsole;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//Runable 用于定义线程任务
public class PoolLogLis implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(PoolLogLis.class);

    private MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private ExecutorService scheduledThreadPool;

    //    开启监听
//    public void start() {
//        logger.info("start monitoring for connections pool");
//        scheduledExecutorService.scheduleAtFixedRate(() -> {
//            try {
//                // 模拟耗时任务
//                Thread.sleep(5000); // 任务执行时间 5 秒
//                System.out.println("任务执行时间: " + System.currentTimeMillis());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        },5,5, TimeUnit.SECONDS); //每 5s 打印一次
//    }

    public void start() {
        scheduledExecutorService.scheduleAtFixedRate(this,0,5, TimeUnit.SECONDS);
    }

//    停止监听
    public void stop() {
        this.scheduledThreadPool.shutdownNow();
        logger.info("stop monitoring for connections pool");
    }

//    重写接口 必须实现的 run 方法
    @Override
    public void run() {
        try{
            Set<ObjectInstance> instances = mBeanServer.queryMBeans(new ObjectName("org.mongodb.driver:type=ConnectionPool,*"), null);
            for (ObjectInstance instance : instances) {
                String className = instance.getClassName();
                if(!className.contains("mongo")){
                    continue;
                }
                ObjectName objectName = instance.getObjectName();
                String[] attrs = new String[]{"CheckedOutCount","Host","Port","MinSize","MaxSize","Size","WaitQueueSize"};
                StringBuilder sb = new StringBuilder();
                for(String attr : attrs){
                    sb.append(attr).append("=").append(mBeanServer.getAttribute(objectName,attr)).append("|");
                }
                logger.info("ConnPoolStatistic - {}: \n\t - {}",objectName,sb.toString());
            }
        }catch(Throwable e) {
                logger.error(e.getMessage());
        }
    }
}
