package com.seminarhub.config;

import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.time.Duration;
import java.util.ArrayList;

@Configuration
@EnableCaching
public class ehCacheConfig {

    @Bean
    public CacheManager getCacheManager(){
        CachingProvider provider = Caching.getCachingProvider();
        CacheManager cacheManager =  provider.getCacheManager();

        CacheConfiguration<String, ArrayList> configuration = CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, ArrayList.class
                        , ResourcePoolsBuilder.heap(100).offheap(10, MemoryUnit.MB))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofHours(3)))
                .build();

        javax.cache.configuration.Configuration<String, ArrayList> cacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(configuration);
        cacheManager.createCache("mainPageSeminarList", cacheConfiguration);

        return cacheManager;
    }
}
