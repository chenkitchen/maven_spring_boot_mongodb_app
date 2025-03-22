package com.example.maven_spring_boot_mongodb_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
public class NotBelieveApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotBelieveApplication.class, args);
    }

//    public static void main(String[] args) {
//        SpringApplication app = new SpringApplication(NotBelieveApplication.class);
//        app.addListeners(new ApplicationPidFileWriter());
//        app.run(args);
//    }

}
