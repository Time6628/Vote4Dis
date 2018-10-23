package me.time6628.vote4dis.bukkit.commands;

import me.time6628.vote4dis.bukkit.Vote4DisBukkit;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VoteCommandBukkit implements CommandExecutor {

    private Vote4DisBukkit vote4Dis = Vote4DisBukkit.instance;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        List<String> messages = new ArrayList<>();
        messages.add(ChatColor.DARK_PURPLE + "-------[Vote Links]-------");
        for (String link : vote4Dis.getCfg().voting.links) {
            messages.add(ChatColor.WHITE + link);
        }
        sender.sendMessage(messages.toArray(new String[0]));
        return true;
    }
}
