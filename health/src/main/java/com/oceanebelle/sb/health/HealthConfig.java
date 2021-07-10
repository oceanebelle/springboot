package com.oceanebelle.sb.health;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.CompositeHealthContributor;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.actuate.health.HealthContributorRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Log4j2
@Configuration
public class HealthConfig {
    private static final List<String> custom = Arrays.asList("myrandom");

    @Bean
    @Qualifier("appHealth")
    CompositeHealthContributor custom(HealthContributorRegistry registry) {
        var appHealthContributor = new HashMap<String, HealthContributor>();

        // Custom health components are unregistered from actuator/health
        registry.stream().filter(c -> custom.contains(c.getName()))
                .peek(c -> log.info("Registering {}", c.getName()))
                .peek(c -> appHealthContributor.put(c.getName(), c.getContributor()))
                .peek(c -> log.info("Unregistering: {}", c.getName()))
                .forEach(c ->  registry.unregisterContributor(c.getName()));

        registry.stream().forEach(c -> appHealthContributor.put(c.getName(), c.getContributor()));

        log.info("Registered: {} default health indicators", registry.stream().count());
        log.info("Registered: {} custom health indicators", appHealthContributor.size());

        return CompositeHealthContributor.fromMap(appHealthContributor);
    }
}
