package com.oceanebelle.sb.controller;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.actuate.health.CompositeHealthContributor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.actuate.health.StatusAggregator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Expose health components to custom controller
 */
@Log4j2
@RequiredArgsConstructor
@RestController
public class StatusController {

    private final CompositeHealthContributor contributor;

    private final StatusAggregator aggregator;

    @RequestMapping("/status")
    public ResponseEntity<AppStatus> status() {

        var health = contributor.stream()
                .map(c -> new SimpleImmutableEntry<>(c.getName(), ((HealthIndicator) c.getContributor()).getHealth(true)))
                .collect(Collectors.toMap(SimpleImmutableEntry::getKey, SimpleImmutableEntry::getValue));

        var status = StatusAggregator.getDefault().getAggregateStatus(
                health.values().stream().map(Health::getStatus).collect(Collectors.toSet()));

        var appStatus = AppStatus.builder().status(status).components(health).build();
        return (status.equals(Status.UP)) ?
                ResponseEntity.ok(appStatus) :
                ResponseEntity.internalServerError().body(appStatus);
    }

    @RequestMapping("/status/{component}")
    public ResponseEntity<Health> componentStatus(@PathVariable("component") String component) {

        return contributor.stream().filter(c -> c.getName().equalsIgnoreCase(component))
                .findFirst()
                .map(c -> ((HealthIndicator) c.getContributor()).getHealth(true))
                .map(h -> (h.getStatus().equals(Status.UP) ? ResponseEntity.ok(h) : ResponseEntity.internalServerError().body(h)))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No health component for " + component));
    }


    @Builder
    @Getter
    static class AppStatus {
        @JsonUnwrapped
        private Status status;
        private Map<String, Health> components;
    }

}
