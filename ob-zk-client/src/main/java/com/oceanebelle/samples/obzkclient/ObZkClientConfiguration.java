package com.oceanebelle.samples.obzkclient;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObZkClientConfiguration {

    @Bean
    ZkManager getZkManager() throws IOException, InterruptedException {
        return new ZkManagerImpl();
    }    
}