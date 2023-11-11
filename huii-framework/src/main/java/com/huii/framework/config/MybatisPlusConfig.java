package com.huii.framework.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.extension.incrementer.H2KeyGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.huii.framework.handler.MybatisMetaObjectHandler;
import com.huii.framework.interceptor.MybatisDataPermissionInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * mbp 配置
 *
 * @author huii
 */
@Configuration
@MapperScan("com.huii.**.mapper")
@EnableTransactionManagement
public class MybatisPlusConfig {

    /**
     * postgresSQL 自增ID
     */
    @Bean
    public IKeyGenerator keyGenerator() {
        return new H2KeyGenerator();
    }

    /**
     * 元数据填充
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MybatisMetaObjectHandler();
    }

    /**
     * 拦截器配置
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(dataPermissionInterceptor());
        interceptor.addInnerInterceptor(paginationInnerInterceptor());
        interceptor.addInnerInterceptor(optimisticLockerInnerInterceptor());
        return interceptor;
    }

    /**
     * 数据权限拦截器
     */
    public MybatisDataPermissionInterceptor dataPermissionInterceptor() {
        return new MybatisDataPermissionInterceptor();
    }

    /**
     * 分页拦截器
     * 设置最大单页限制数量，默认 500 条，-1 不受限制
     */
    public PaginationInnerInterceptor paginationInnerInterceptor() {
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setMaxLimit(100L);
        paginationInnerInterceptor.setOverflow(true);
        return paginationInnerInterceptor;
    }

    /**
     * 乐观锁插件
     */
    public OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor() {
        return new OptimisticLockerInnerInterceptor();
    }
}