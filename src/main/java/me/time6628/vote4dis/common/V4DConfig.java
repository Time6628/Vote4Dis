package me.time6628.vote4dis.common;

import me.time6628.vote4dis.sponge.Texts;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.Collections;
import java.util.List;

@ConfigSerializable
public class V4DConfig {

    @Setting("Voting")
    public Voting voting = new Voting();

    @Setting("Messages")
    public Messages messages = new Messages();

    @Setting("Redis")
    public Redis redis = new Redis();

    @ConfigSerializable
    public static class Voting {

        @Setting("Links")
        public List<String> links = Collections.singletonList("https://link1.io");

        @Setting("Rewards")
        public List<String> rewards = Collections.singletonList("eco give @p 250");
    }

    @ConfigSerializable
    public static class Messages {

        @Setting("Prefix")
        public String prefix = "&5[Voting]";

        @Setting("Broadcast")
        public String broadcast = "%s &dhas just voted at &f%s, you can too with &f/vote&d!";

        @Setting("Broadcast")
        public String login = "&dYou have &f%d &dvotes.";
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
