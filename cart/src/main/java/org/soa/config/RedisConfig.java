package org.soa.config;

import org.redisson.api.RedissonClient;
import org.redisson.Redisson;
import org.redisson.config.Config;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class RedisConfig {

    @Produces
    @ApplicationScoped
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379"); // Redis server address
        return Redisson.create(config);
    }
}
