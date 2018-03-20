package me.time6628.vote4dis.sponge.commands;

import me.time6628.vote4dis.sponge.Texts;
import me.time6628.vote4dis.sponge.Vote4DisSponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextElement;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TimeTheCat on 2/22/2017.
 */
public class VotesCommand implements CommandExecutor {
    private final Vote4DisSponge pl = Vote4DisSponge.instance;

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (src instanceof ConsoleSource) return CommandResult.empty();

        Map<String, TextElement> a = new HashMap<>();
        a.put("votes", Text.of(pl.getVotes((Player) src)));
        src.sendMessage(Texts.votesMessage.apply(a).build());
        pl.updateUUIDCache(((Player) src).getUniqueId().toString(), src.getName());

        return CommandResult.success();
    }
}
