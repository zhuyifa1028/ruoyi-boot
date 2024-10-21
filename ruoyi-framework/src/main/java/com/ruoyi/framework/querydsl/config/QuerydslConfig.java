package com.ruoyi.framework.querydsl.config;

import com.querydsl.sql.mysql.MySQLQueryFactory;
import com.querydsl.sql.spring.SpringConnectionProvider;
import com.querydsl.sql.spring.SpringExceptionTranslator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.function.Supplier;

@Configuration
public class QuerydslConfig {

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public MySQLQueryFactory queryFactory(DataSource dataSource) {
        com.querydsl.sql.Configuration configuration = new com.querydsl.sql.Configuration(new MySQLTemplates());
        configuration.setExceptionTranslator(new SpringExceptionTranslator());

        Supplier<Connection> provider = new SpringConnectionProvider(dataSource);
        return new MySQLQueryFactory(configuration, provider);
    }
}
