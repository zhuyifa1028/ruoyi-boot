package com.ruoyi.framework.jpa.config;

import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@EnableJpaRepositories(basePackages = "com.ruoyi.**.repository")
@EntityScan(basePackages = "com.ruoyi.**.entity")
@EnableJpaAuditing
@Configuration
public class JpaConfig implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        try {
            return Optional.of(SecurityUtils.getUserId());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
