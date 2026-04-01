package com.energy.audit.web.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis configuration - mapper scanning
 */
@Configuration
@MapperScan("com.energy.audit.dao.mapper")
public class MyBatisConfig {
    // MyBatis configuration is handled via application.yml
    // Mapper XML files are located in classpath:mapper/**/*.xml
}
