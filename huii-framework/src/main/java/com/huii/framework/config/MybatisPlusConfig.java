package com.huii.framework.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@MapperScan("com.huii.**.mapper")
@EnableTransactionManagement
public class MybatisPlusConfig {

}