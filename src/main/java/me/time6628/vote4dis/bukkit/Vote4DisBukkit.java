package me.time6628.vote4dis.bukkit;

import me.time6628.vote4dis.common.V4DConfig;
import me.time6628.vote4dis.common.V4DConfigLoader;
import me.time6628.vote4dis.common.Vote4Dis;

public class Vote4DisBukkit extends Vote4Dis {

    public static Vote4DisBukkit instance;

    V4DConfig cfg;

    @Override
    public void onEnable() {
        instance = this;

        V4DConfigLoader cfgLoader = new V4DConfigLoader(getDataFolder());
        cfgLoader.loadConfig();
        cfg = cfgLoader.getV4DConfig();

        int redisPort = cfg.redis.port;
        String redisHost = cfg.redis.host;
        String redisPass = cfg.redis.password;

        super.setup(redisPort, redisHost, redisPass);
    }
}
