package me.time6628.vote4dis.sponge;


import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import com.vexsoftware.votifier.model.Vote;
import me.time6628.vote4dis.common.V4DConfig;
import me.time6628.vote4dis.common.V4DConfigLoader;
import me.time6628.vote4dis.sponge.commands.*;
import me.time6628.vote4dis.sponge.commands.subcommands.v4d.AddRewardCommand;
import me.time6628.vote4dis.sponge.commands.subcommands.v4d.AddVoteLinkCommand;
import me.time6628.vote4dis.sponge.commands.subcommands.v4d.ResetTotalsCommand;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextElement;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Plugin(id = "vote4dis", name = "Vote4Dis", description = "A Redis powered vote listener.", url = "http://time6628.me", authors = {"Time6628"})
public class Vote4DisSponge extends me.time6628.vote4dis.common.Vote4Dis {

    public static Vote4DisSponge instance;

    @Inject
    private Logger logger;

    //config stuff
    @Inject
    @ConfigDir(sharedRoot = false)
    private File defaultFolder;

    private V4DConfig cfg;

    //public
    @Inject
    private Game game;

    @Listener
    public void onPreinit(GamePreInitializationEvent event) {
        setupConfig();
    }

    @Listener
    public void onInit(GameInitializationEvent event) {
        instance = this;
        registerCommands();
        registerEvents();

        game.getScheduler().createTaskBuilder()
                .async()
                .delay(5, TimeUnit.MINUTES)
                .interval(5, TimeUnit.MINUTES)
                .execute(new RecentlyVotedTask())
                .submit(this);
    }

    private void registerEvents() {
        game.getEventManager().registerListeners(this, new V4DListener());
    }

    private void registerCommands() {
        game.getCommandManager().register(this, CommandSpec.builder()
                .executor(new me.time6628.vote4dis.sponge.commands.VotesCommand())
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
        CommandSpec avl = CommandSpec.builder()
                .executor(new AddVoteLinkCommand())
                .permission("vote4dis.command.admin.addvotelink")
                .arguments(GenericArguments.string(Text.of("link")))
                .description(Text.of("Add a vote link."))
                .build();
        CommandSpec ar = CommandSpec.builder()
                .executor(new AddRewardCommand())
                .permission("vote4dis.command.admin.addreward")
                .arguments(GenericArguments.remainingJoinedStrings(Text.of("reward")))
                .description(Text.of("Add a vote reward."))
                .build();
        CommandSpec dv = CommandSpec.builder()
                .executor(new DoubleVotesCommand())
                .permission("vote4dis.command.admin.doublevotes")
                .arguments(GenericArguments.integer(Text.of("Days")))
                .description(Text.of("Double vote rewards for X amount of days.."))
                .build();
        game.getCommandManager().register(this, CommandSpec.builder()
                .executor(new V4DCommand())
                .permission("vote4dis.command.admin.base")
                .description(Text.of("Use /v4d."))
                .child(rt, "resettotals")
                .child(avl, "addvotelink")
                .child(ar, "addvotereward")
                .child(dv, "doublevotes", "dv")
                .build(), "v4d");
    }

    private void setupConfig() {
        logger.info("Setting up config...");
        try {
            V4DConfigLoader v4DConfigLoader = new V4DConfigLoader(defaultFolder);
            v4DConfigLoader.loadConfig();
            cfg = v4DConfigLoader.getV4DConfig();

            int redisPort = cfg.redis.port;
            String redisHost = cfg.redis.host;
            String redisPass = cfg.redis.password;

            Texts.setPrefix(TextSerializers.FORMATTING_CODE.deserialize(cfg.messages.prefix));
            Texts.setBroadcastMessage(cfg.messages.broadcast);
            Texts.setVotesMessage(cfg.messages.login);

            super.setup(redisPort, redisHost, redisPass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event, @Getter("getTargetEntity") Player player) {
        Task.builder().async().execute(() -> {
            if (hasVotedRecently(player.getUniqueId())) {
                Texts.hasVotedRecently().sendTo(player);
            }
            Map<String, TextElement> args = new HashMap<>();
            args.put("votes", Text.of(getVotes(player)));
            player.sendMessage(ChatTypes.ACTION_BAR, Texts.votesMessage.apply(args).build());
            player.sendMessage(Texts.votesMessage.apply(args).build());
            updateUUIDCache(player.getUniqueId().toString(), player.getName());
        }).submit(this);
    }

    /* PLAYER MANAGEMENT */
    public int getVotes(Player player) {
        return super.getVotes(player.getUniqueId());
    }

    private void incrVote(Player player) {
        super.incrVote(player.getUniqueId());
    }

    public boolean hasVotedRecently(UUID uuid) {
        return hasVotedRecently(uuid.toString());
    }

    public Game getGame() {
        return game;
    }

    // rewards
    private void rewardPlayer(Player player) {
        rewardPlayer(player.getName());
    }

    @SuppressWarnings("ConstantConditions")
    private void rewardPlayer(String player) {
        Task.builder().async().execute(() -> {
            for (String voteReward : cfg.voting.rewards) {
                game.getCommandManager().process(game.getServer().getConsole().getCommandSource().get(), voteReward.replace("@p", player));
            }
        }).submit(this);
    }

    @SuppressWarnings("ConstantConditions")
    public PaginationService getPaginationService() {
        return game.getServiceManager().provide(PaginationService.class).get();
    }

    public List<String> getVoteLinks() {
        return cfg.voting.links;
    }

    public void handleVote(Player player, Vote vote) {
        Task.builder().async().execute(() -> {
            Map<String, TextElement> args = new HashMap<>();
            args.put("player", Text.of(player.getName()));
            args.put("service", Text.of(vote.getServiceName()));
            getGame().getServer().getBroadcastChannel().send(Texts.broadcastMessage.apply(args).build());
            incrVote(player);
            if (isDoubleVotes()) {
                rewardPlayer(player);
                player.sendMessage(Texts.DOUBLE_VOTES);
            }
            rewardPlayer(player);
            votedRecently(player.getUniqueId());
        }).submit(this);
    }

    private void votedRecently(UUID uniqueId) {
        super.votedRecently(uniqueId.toString());
    }

    public void addVoteLink(String link) {
        cfg.voting.links.add(link);
    }

    public List<String> getVoteRewards() {
        return cfg.voting.rewards;
    }

    public void addReward(String reward) {
        cfg.voting.rewards.add(reward);
    }

    public Map<String, TextElement> getTextTemplateMap(String key, String value) {
        Map<String, TextElement> a = new HashMap<>();
        a.put(key, Text.of(value));
        return a;
    }
}
