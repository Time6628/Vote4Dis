package me.time6628.vote4dis.common;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;

public class V4DConfigLoader {

    private Vote4Dis vote4Dis;
    private V4DConfig v4DConfig;
    private File configFile;

    public V4DConfigLoader(File file) {
        this.vote4Dis = vote4Dis;
        if (!file.exists())
            vote4Dis.getDataFolder().mkdirs();
    }

    public boolean loadConfig() {
        try {
            File file = new File(configFile, "config.conf");
            if (!file.exists()) {
                file.createNewFile();
            }
            ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setFile(file).build();
            CommentedConfigurationNode config = loader.load(ConfigurationOptions.defaults().setShouldCopyDefaults(true));
            v4DConfig = config.getValue(TypeToken.of(V4DConfig.class), new V4DConfig());
            loader.save(config);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public V4DConfig getV4DConfig() {
        return v4DConfig;
    }
}
