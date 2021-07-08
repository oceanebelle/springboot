package com.oceanebelle.sb.health;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.actuate.health.DefaultHealthContributorRegistry;
import org.springframework.boot.actuate.health.HealthContributorRegistry;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Component
public class MyCustomRegistry implements InitializingBean {

    private static final List<String> custom = Arrays.asList("myrandom");

    private final HealthContributorRegistry defaultRegistry;

    @Getter
    private DefaultHealthContributorRegistry customRegistry;


    @Override
    public void afterPropertiesSet() throws Exception {
        customRegistry = new DefaultHealthContributorRegistry();

        defaultRegistry.stream().filter(c -> custom.contains(c.getName()))
                .peek(c -> log.info("registering {}", c.getName()))
                .peek(c -> customRegistry.registerContributor(c.getName(), c.getContributor()))
                .peek(c -> log.info("Unregistering: {}", c.getName()))
                .forEach(c ->  defaultRegistry.unregisterContributor(c.getName()));

        defaultRegistry.stream()
                .peek(c -> log.info("registering {}", c.getName()))
                .forEach(c -> customRegistry.registerContributor(c.getName(), c.getContributor()));

        log.info("Registered: {} default health indicators", defaultRegistry.stream().count());
        log.info("Registered: {} custom health indicators", customRegistry.stream().count());

    }
}
