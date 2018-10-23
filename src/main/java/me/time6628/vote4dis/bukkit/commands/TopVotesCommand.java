package me.time6628.vote4dis.bukkit.commands;

import me.time6628.vote4dis.bukkit.Vote4DisBukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class TopVotesCommand implements CommandExecutor {

    private Vote4DisBukkit vote4Dis = Vote4DisBukkit.instance;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Map<String, Integer> v = vote4Dis.getTopVotes().entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
        List<String> to = new ArrayList<>();
        to.add(ChatColor.LIGHT_PURPLE + "===== Top Voters =====");
        AtomicInteger i = new AtomicInteger();
        v.forEach((s1, integer) -> {
            if (i.get() == 10) return;
            to.add(integer + " - " + s1);
            i.getAndIncrement();
        });
        for (String s1 : to) {
            commandSender.sendMessage(s1);
        }
        return true;
    }
}