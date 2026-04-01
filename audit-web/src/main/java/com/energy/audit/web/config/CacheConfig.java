package com.energy.audit.web.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Cache configuration - Ehcache 3 via JCache (JSR-107)
 * 
 * Cache definitions are configured in ehcache.xml:
 * - dictCache: 500 entries, TTL 1 hour
 * - energyCache: 200 entries, TTL 30 min
 * - templateCache: 100 entries, TTL 1 hour
 */
@Configuration
@EnableCaching
public class CacheConfig {
    // Ehcache 3 is configured via ehcache.xml and application.yml
    // Spring Boot auto-configures JCache provider
}
