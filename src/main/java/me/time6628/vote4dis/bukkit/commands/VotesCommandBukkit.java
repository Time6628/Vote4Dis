package me.time6628.vote4dis.bukkit.commands;

import me.time6628.vote4dis.bukkit.Vote4DisBukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VotesCommandBukkit implements CommandExecutor {

    private Vote4DisBukkit vote4Dis = Vote4DisBukkit.instance;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            sender.sendMessage(String.format(ChatColor.translateAlternateColorCodes('&', vote4Dis.getCfg().messages.login), vote4Dis.getVotes(((Player) sender).getUniqueId())));
        }
        return true;
    }
}
