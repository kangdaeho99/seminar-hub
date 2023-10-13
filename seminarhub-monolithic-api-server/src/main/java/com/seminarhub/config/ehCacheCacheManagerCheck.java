package com.seminarhub.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.cache.CacheManager;

@Component
@Log4j2
public class ehCacheCacheManagerCheck implements CommandLineRunner {
    private final CacheManager cacheManager;

    public ehCacheCacheManagerCheck(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("ehCacheManager : "+this.cacheManager.getClass().getName());
    }
}
