package com.ruoyi.framework.querydsl.configuration;

import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.mysql.MySQLQueryFactory;
import com.querydsl.sql.spring.SpringConnectionProvider;
import com.querydsl.sql.spring.SpringExceptionTranslator;
import com.querydsl.sql.types.JSR310LocalDateTimeType;
import com.querydsl.sql.types.JSR310LocalDateType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class JdbcConfiguration {

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public MySQLQueryFactory queryFactory(DataSource dataSource) {
        com.querydsl.sql.Configuration configuration = new com.querydsl.sql.Configuration(MySQLTemplates.DEFAULT);
        configuration.setExceptionTranslator(new SpringExceptionTranslator());
        configuration.register(new JSR310LocalDateType());
        configuration.register(new JSR310LocalDateTimeType());

        SpringConnectionProvider provider = new SpringConnectionProvider(dataSource);

        return new MySQLQueryFactory(configuration, provider);
    }

}
