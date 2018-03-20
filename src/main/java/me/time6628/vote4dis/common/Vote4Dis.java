package me.time6628.vote4dis.common;

import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Vote4Dis extends JavaPlugin {

    private static JedisPool jedisPool;

    private int redisPort;
    private String redisHost;
    private String redisPass;

    public void setup(int redisPort, String redisHost, String redisPass) {
        this.redisPort = redisPort;
        this.redisHost = redisHost;
        this.redisPass = redisPass;

        if (this.redisPass.equals("")) {
            jedisPool = setupRedis(this.redisHost, this.redisPort, this.redisPass);
        } else {
            jedisPool = setupRedis(this.redisHost, this.redisPort);
        }
    }

    public Integer getVotes(UUID uuid) {
        return getVotes(uuid.toString());
    }

    private Integer getVotes(String uuid) {
        try {
            return CompletableFuture.supplyAsync(() -> {
                try (Jedis jedis = getJedis().getResource()) {
                    if (jedis.hexists(RedisKeys.VOTE_COUNT_KEY, uuid))
                        return Integer.parseInt(jedis.hget(RedisKeys.VOTE_COUNT_KEY, uuid));
                    else {
                        jedis.hset(RedisKeys.VOTE_COUNT_KEY, uuid, String.valueOf(0));
                        return 0;
                    }
                }
            }).get();
        } catch (Exception e) {
            return 0;
        }
    }

    public void incrVote(UUID id) {
        incrVote(id.toString());
    }

    private void incrVote(String id) {
        try (Jedis jedis = getJedis().getResource()) {
            jedis.hincrBy(RedisKeys.VOTE_COUNT_KEY, id, 1);
        }
    }

    public void resetVoteTotals() {
        try (Jedis jedis = getJedis().getResource()) {
            jedis.del(RedisKeys.VOTE_COUNT_KEY);
        }
    }

    public void voteDouble(Integer integer) {
        try (Jedis jedis = getJedis().getResource()) {
            jedis.set(RedisKeys.DOUBLE_REWARDS, "7");
            jedis.expire(RedisKeys.DOUBLE_REWARDS, (int) TimeUnit.DAYS.toSeconds(integer));
        }
    }

    public void updateUUIDCache(String uuid, String name) {
        try (Jedis jedis = getJedis().getResource()) {
            jedis.hset(RedisKeys.UUID_CACHE, uuid, name);
        }
    }

    public boolean hasVotedRecently(String uuid) {
        try {
            return CompletableFuture.supplyAsync(() -> {
                try (Jedis jedis = getJedis().getResource()) {
                    return jedis.exists(RedisKeys.HAS_VOTED + uuid);
                }
            }).get();
        } catch (Exception e) {
            return false;
        }
    }

    private String getNameFromCache(String uuid) {
        try {
            return CompletableFuture.supplyAsync(() -> {
                try (Jedis jedis = getJedis().getResource()) {
                    return jedis.hget(RedisKeys.UUID_CACHE, uuid);
                }
            }).get();
        } catch (InterruptedException | ExecutionException ignored) {
            return null;
        }
    }

    public Map<String, Integer> getTopVotes() {
        try {
            return CompletableFuture.supplyAsync(() -> {
                try (Jedis jedis = getJedis().getResource()) {
                    //get all of the voters
                    Map<String, String> a = jedis.hgetAll(RedisKeys.VOTE_COUNT_KEY);
                    //get a new map to put them in
                    Map<String, Integer> b = new HashMap<>();
                    a.forEach((s, s2) -> {
                        if (Integer.valueOf(s2) > 0) b.put(getNameFromCache(s), Integer.valueOf(s2));
                    });
                    return b;
                }
            }).get();
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    public void votedRecently(String uuid) {
        try (Jedis jedis = getJedis().getResource()) {
            jedis.set(RedisKeys.HAS_VOTED + uuid, "yes");
            jedis.expire(RedisKeys.HAS_VOTED + uuid, (int) TimeUnit.DAYS.toSeconds(1));
        }
    }

    public boolean isDoubleVotes() {
        try {
            return CompletableFuture.supplyAsync(() -> {
                try (Jedis jedis = getJedis().getResource()) {
                    return jedis.exists(RedisKeys.DOUBLE_REWARDS);
                }
            }).get();
        } catch (Exception ignored) {
            return false;
        }
    }

    private JedisPool setupRedis(String host, int port) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(128);
        return new JedisPool(config, host, port, 0);
    }

    private JedisPool setupRedis(String host, int port, String password) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(128);
        return new JedisPool(config, host, port, 0, password);
    }

    public JedisPool getJedis() {
        return jedisPool;
    }
}
