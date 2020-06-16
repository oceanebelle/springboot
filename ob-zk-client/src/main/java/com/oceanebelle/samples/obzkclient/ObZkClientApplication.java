package com.oceanebelle.samples.obzkclient;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class ObZkClientApplication implements CommandLineRunner {

	private final ZkManager manager;

	@Autowired
	public ObZkClientApplication(ZkManager manager) {
		this.manager = manager;
	}

	public static void main(String[] args) {
		SpringApplication.run(ObZkClientApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("STARTED");
		Objects.requireNonNull(manager, "The manager must exist");
		try {

			log.info("Creating TEST");
			manager.create("/TEST2", "test".getBytes("UTF-8"));

		} finally {
			manager.closeConnection();
			log.info("BYE");
		}
	}

}
