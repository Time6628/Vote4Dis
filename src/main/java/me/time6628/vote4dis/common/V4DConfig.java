package me.time6628.vote4dis.common;

import me.time6628.vote4dis.sponge.Texts;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.text.TextTemplate;

import java.util.List;

@ConfigSerializable
public class V4DConfig {

    @Setting("Voting")
    public Voting voting;

    @Setting("Messages")
    public Messages messages;

    @Setting("Redis")
    public Redis redis;

    @ConfigSerializable
    public static class Voting {

        @Setting("Links")
        public List<String> links;

        @Setting("Rewards")
        public List<String> rewards;
    }

    @ConfigSerializable
    public static class Messages {

        @Setting("Prefix")
        public String prefix = "&5[Voting]";

        @Setting("Broadcast")
        public TextTemplate broadcast = Texts.broadcastMessage;

        @Setting("Broadcast")
        public TextTemplate login = Texts.votesMessage;
    }

    @ConfigSerializable
    public static class Redis {

        @Setting("Host")
        public String host = "localhost";

        @Setting("Port")
        public int port = 6379;

        @Setting("Password")
        public String password = "";

        @Setting("Keys")
        public Keys keys = new Keys();

        @ConfigSerializable
        public static class Keys {

            @Setting(value = "UUID-Cache")
            public String uuidCache = "market:uuidcache";
        }
    }
}
