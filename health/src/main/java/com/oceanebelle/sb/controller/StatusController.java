package com.oceanebelle.sb.controller;

import com.oceanebelle.sb.health.MyCustomRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@RestController
public class StatusController {

    private final MyCustomRegistry registry;

    @RequestMapping("/status")
    public List<Health> status() {

        log.info("endpoints {}", registry.getCustomRegistry().stream().count());

        var status = registry.getCustomRegistry().stream()
                .map(c -> (HealthIndicator) c.getContributor())
                .map(c -> c.health())
                .collect(Collectors.toList());

        return status;
    }

}
