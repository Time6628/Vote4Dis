package me.time6628.vote4dis.bukkit;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import me.time6628.vote4dis.bukkit.commands.TopVotesCommand;
import me.time6628.vote4dis.bukkit.commands.VoteCommandBukkit;
import me.time6628.vote4dis.bukkit.commands.VotesCommandBukkit;
import me.time6628.vote4dis.common.V4DConfig;
import me.time6628.vote4dis.common.V4DConfigLoader;
import me.time6628.vote4dis.common.Vote4Dis;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Vote4DisBukkit extends Vote4Dis implements Listener {

    public static Vote4DisBukkit instance;

    private V4DConfig cfg;

    @Override
    public void onEnable() {
        instance = this;

        V4DConfigLoader cfgLoader = new V4DConfigLoader(getDataFolder());
        cfgLoader.loadConfig();
        cfg = cfgLoader.getV4DConfig();

        registerCommands();

        registerListeners();

        int redisPort = cfg.redis.port;
        String redisHost = cfg.redis.host;
        String redisPass = cfg.redis.password;

        super.setup(redisPort, redisHost, redisPass);
    }

    private void registerCommands() {
        getCommand("vote").setExecutor(new VoteCommandBukkit());
        getCommand("votes").setExecutor(new VotesCommandBukkit());
        getCommand("topvotes").setExecutor(new TopVotesCommand());
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    private void handleVote(Player player, Vote vote) {
        getServer().getScheduler().runTaskAsynchronously(this, () -> {
            getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', String.format(cfg.messages.broadcast, player.getName(), vote.getServiceName())));
            incrVote(player.getUniqueId());
            if (isDoubleVotes()) {
                rewardPlayer(player.getName());
                player.sendMessage(ChatColor.LIGHT_PURPLE + "Double Rewards are currently active for voting, double all rewards!");
            }
            rewardPlayer(player.getName());
            votedRecently(player.getUniqueId());
        });
    }

    private void rewardPlayer(String name) {
        getServer().getScheduler().runTaskAsynchronously(this, () -> cfg.voting.rewards.forEach(s -> getServer().dispatchCommand(getServer().getConsoleSender(), s.replace("@p", name))));
    }

    public V4DConfig getCfg() {
        return cfg;
    }

    @EventHandler
    public void voteListener(VotifierEvent event) {
        if (getServer().getPlayer(event.getVote().getUsername()).isOnline()) {
            handleVote(getServer().getPlayer(event.getVote().getUsername()), event.getVote());
        }
    }
}
