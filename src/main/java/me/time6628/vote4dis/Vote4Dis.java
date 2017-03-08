package me.time6628.vote4dis;


import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import com.vexsoftware.votifier.model.Vote;
import me.time6628.vote4dis.commands.*;
import me.time6628.vote4dis.commands.subcommands.v4d.ResetTotalsCommand;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;

import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.serializer.TextSerializers;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.File;
import java.util.*;

@Plugin(id = "vote4dis", name = "Vote4Dis", description = "A Redis powered vote listener.", url = "http://time6628.me", authors = {"Time6628"})
public class Vote4Dis {

    public static Vote4Dis instance;

    @Inject
    private Logger logger;

    //config stuff
    @Inject
    @DefaultConfig(sharedRoot = false)
    private File defaultCfg;
    @Inject
    @DefaultConfig(sharedRoot = false)
    private ConfigurationLoader<CommentedConfigurationNode> cfgMgr;
    private ConfigurationNode cfg;

    //public
    @Inject
    private Game game;

    private static JedisPool jedisPool;
    private List<String> voteLinks;
    private List<String> voteRewards;

    private int redisPort;
    private String redisHost;
    private String redisPass;

    @Listener
    public void onPreinit(GamePreInitializationEvent event) {
        setupConfig();
    }

    @Listener
    public void onInit(GameInitializationEvent event) {
        instance = this;
        registerCommands();
        registerEvents();
    }

    private void registerEvents() {
        game.getEventManager().registerListeners(this, new V4DListener());
    }

    private void registerCommands() {
        game.getCommandManager().register(this, CommandSpec.builder()
        .executor(new me.time6628.vote4dis.commands.VotesCommand())
        .permission("vote4dis.command.votes")
        .description(Text.of("See how many votes you have."))
        .build(), "votes");
        game.getCommandManager().register(this, CommandSpec.builder()
                .executor(new VoteCommand())
                .permission("vote4dis.command.vote")
                .description(Text.of("List where you can vote."))
                .build(), "vote");
        game.getCommandManager().register(this, CommandSpec.builder()
                .executor(new TestVote())
                .permission("vote4dis.command.testvote")
                .description(Text.of("List where you can vote."))
                .build(), "testvote");
        game.getCommandManager().register(this, CommandSpec.builder()
                .executor(new TopVotesCommand())
                .permission("vote4dis.command.topvotes")
                .description(Text.of("List of top voters."))
                .build(), "topvotes");
        CommandSpec rt = CommandSpec.builder()
                .executor(new ResetTotalsCommand())
                .permission("vote4dis.command.admin.resettotals")
                .description(Text.of("Reset top voters."))
                .build();
        game.getCommandManager().register(this, CommandSpec.builder()
                .executor(new V4DCommand())
                .permission("vote4dis.command.admin.base")
                .description(Text.of("Use /v4d."))
                .child(rt, "resettotals")
                .build(), "v4d");
    }

    private void setupConfig() {
        logger.info("Setting up config...");
        try {
            if (!defaultCfg.exists()) {
                defaultCfg.createNewFile();

                this.cfg = cfgMgr.load();
                this.cfg.getNode("redis", "host").setValue("localhost");
                this.cfg.getNode("redis", "port").setValue(6379);
                this.cfg.getNode("redis", "use-password").setValue(false);
                this.cfg.getNode("redis", "password").setValue("password");

                this.cfg.getNode("voting", "links").setValue(new ArrayList(){{add("https://link1.io");}});
                this.cfg.getNode("voting", "rewards").setValue(new ArrayList(){{add("give minecraft:potato 1");}});


                this.cfg.getNode("messages", "prefix").setValue("&5[Voting]");
                this.cfg.getNode("messages", "broadcast").setValue("%s has just voted at %s, you can vote too with /vote!");

                this.cfgMgr.save(cfg);
            }

            this.cfg = cfgMgr.load();

            this.voteLinks = cfg.getNode("voting", "links").getList(TypeToken.of(String.class));
            this.voteRewards = cfg.getNode("voting", "rewards").getList(TypeToken.of(String.class));

            this.redisPort = cfg.getNode("redis", "port").getInt();
            this.redisHost = cfg.getNode("redis", "host").getString();
            this.redisPass = cfg.getNode("redis", "password").getString();

            Texts.setPrefix(TextSerializers.FORMATTING_CODE.deserialize(cfg.getNode("messages", "prefix").getString()));
            Texts.setBroadcastMessage(cfg.getNode("messages", "broadcast").getString());

            if (this.cfg.getNode("redis", "use-password").getBoolean()) {
                jedisPool = setupRedis(this.redisHost, this.redisPort, this.redisPass);
                //logger.info("Connected to Redis!");
            } else {
                jedisPool = setupRedis(this.redisHost, this.redisPort);
                //logger.info("Connected to Redis!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event, @Getter("getTargetEntity") Player player) {
        player.sendMessage(ChatTypes.ACTION_BAR, Texts.getVotesMessage(getVotes(player)));
        player.sendMessage(Texts.getVotesMessage(getVotes(player)));
        updateUUIDCache(player.getUniqueId().toString(), player.getName());
    }



    /* PLAYER MANAGEMENT */
    public int getVotes(Player player) {
        return getVotes(player.getUniqueId().toString());
    }

    public Integer getVotes(UUID uuid) {
        return getVotes(uuid.toString());
    }

    private Integer getVotes(String uuid) {
        try (Jedis jedis = getJedis().getResource()) {
            if (jedis.hexists(RedisKeys.VOTE_COUNT_KEY, uuid))
                return Integer.parseInt(jedis.hget(RedisKeys.VOTE_COUNT_KEY, uuid));
            else {
                jedis.hset(RedisKeys.VOTE_COUNT_KEY, uuid, String.valueOf(0));
                return 0;
            }
        }
    }

    public void incrVote(Player player) {
        incrVote(player.getUniqueId().toString());
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
            Map<String, String> totals = jedis.hgetAll(RedisKeys.VOTE_COUNT_KEY);
            String[] keys = (String[]) totals.keySet().toArray();
            jedis.hdel(RedisKeys.VOTE_COUNT_KEY, keys);
        }
    }

    public void updateUUIDCache(String uuid, String name) {
        try (Jedis jedis = getJedis().getResource()) {
            jedis.hset(RedisKeys.UUID_CACHE, uuid, name);
        }
    }

    private String getNameFromCache(String uuid) {
        try (Jedis jedis = getJedis().getResource()) {
            return jedis.hget(RedisKeys.UUID_CACHE, uuid);
        }
    }

    public Map<String, Integer> getTopVotes() {
        try (Jedis jedis = getJedis().getResource()) {
            Map<String, String> a = jedis.hgetAll(RedisKeys.VOTE_COUNT_KEY);
            Map<String, Integer> b = new HashMap<>();
            a.forEach((s, s2) -> b.put(getNameFromCache(s), Integer.valueOf(s2)));
            return b;
        }
    }


    /* REDIS STUFF */
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

    private JedisPool getJedis() {
        if (jedisPool == null) {
            if (this.cfg.getNode("redis", "use-password").getBoolean()) {
                return setupRedis(this.redisHost, this.redisPort, this.redisPass);
            } else {
                return setupRedis(this.redisHost, this.redisPort);
            }
        } else {
            return jedisPool;
        }
    }

    public Game getGame() {
        return game;
    }

    // rewards
    public void rewardPlayer(Player player) {
        rewardPlayer(player.getName());
    }

    @SuppressWarnings("ConstantConditions")
    private void rewardPlayer(String player) {
        for (String voteReward : voteRewards) {
            game.getCommandManager().process(game.getServer().getConsole().getCommandSource().get(), voteReward.replace("@p", player));
        }
    }

    @SuppressWarnings("ConstantConditions")
    public PaginationService getPaginationService() {
        return game.getServiceManager().provide(PaginationService.class).get();
    }

    public List<String> getVoteLinks() {
        return voteLinks;
    }

    public void handleVote(Player player, Vote vote) {
        getGame().getServer().getBroadcastChannel().send(Texts.getBroadcastMessage(vote.getUsername(), vote.getServiceName()));
        incrVote(player);
        rewardPlayer(player);
        logger.info("Username: " + vote.getUsername(), "IP Address: " + vote.getAddress(), "Site: " + vote.getServiceName());
    }
}
