package com.energy.audit.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Energy Audit Platform Application
 */
@SpringBootApplication(scanBasePackages = "com.energy.audit")
public class EnergyAuditApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnergyAuditApplication.class, args);
    }
}
