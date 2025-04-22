package com.ruoyi.framework.querydsl;

import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.OracleTemplates;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.mysql.MySQLQueryFactory;
import com.querydsl.sql.oracle.OracleQueryFactory;
import com.querydsl.sql.spring.SpringConnectionProvider;
import com.querydsl.sql.types.JSR310LocalDateTimeType;
import com.querydsl.sql.types.JSR310LocalDateType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class QuerydslConfiguration {

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public MySQLQueryFactory mysqlQueryFactory(DataSource dataSource) {
        SQLTemplates templates = MySQLTemplates.builder()
                .printSchema()
                .build();
        return new MySQLQueryFactory(buildConfiguration(templates), new SpringConnectionProvider(dataSource));
    }

    @Bean
    public OracleQueryFactory oracleQueryFactory(DataSource dataSource) {
        SQLTemplates templates = OracleTemplates.builder()
                .printSchema()
                .build();
        return new OracleQueryFactory(buildConfiguration(templates), new SpringConnectionProvider(dataSource));
    }

    private static com.querydsl.sql.Configuration buildConfiguration(SQLTemplates templates) {
        com.querydsl.sql.Configuration configuration = new com.querydsl.sql.Configuration(templates);
        configuration.register(new JSR310LocalDateTimeType());
        configuration.register(new JSR310LocalDateType());
        return configuration;
    }

}
