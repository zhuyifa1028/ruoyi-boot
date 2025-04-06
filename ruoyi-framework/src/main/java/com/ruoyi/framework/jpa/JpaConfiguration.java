package com.ruoyi.framework.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ruoyi.common.utils.SecurityUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.lang.NonNull;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
@EntityScan("com.ruoyi.**.entity")
@EnableJpaRepositories("com.ruoyi.**.repository")
public class JpaConfiguration implements AuditorAware<String> {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory queryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityUtils.getUsername());
    }

}
