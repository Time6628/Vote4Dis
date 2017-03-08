package me.time6628.vote4dis.commands;

import me.time6628.vote4dis.Texts;
import me.time6628.vote4dis.Vote4Dis;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

/**
 * Created by TimeTheCat on 2/22/2017.
 */
public class VotesCommand implements CommandExecutor {
    private final Vote4Dis pl = Vote4Dis.instance;

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (src instanceof ConsoleSource) return CommandResult.empty();

        src.sendMessage(Texts.getVotesMessage(pl.getVotes((Player) src)));
        pl.updateUUIDCache(((Player) src).getUniqueId().toString(), src.getName());

        return CommandResult.success();
    }
}
