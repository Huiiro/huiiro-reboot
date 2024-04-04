package com.huii;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HuiiApplication {

    public static void main(String[] args) {
        System.setProperty("spring.devtools.restart.enabled", "false");
        System.out.println("spring.devtools closed");
        SpringApplication.run(HuiiApplication.class, args);
        System.out.println("HUII-APPLICATION-START-SUCCESS");
        System.out.println("-------------------------------");
        System.out.println("----------　／l、---------------");
        System.out.println("----------（°､ 。 ７------------");
        System.out.println("---------- l、 ~ヽ--------------");
        System.out.println("---------- じしf_, )ノ----------");
        System.out.println("-------------------------------");
    }
}
