package me.time6628.vote4dis.commands;

import me.time6628.vote4dis.Vote4Dis;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import java.util.Optional;

/**
 * Created by TimeTheCat on 7/4/2017.
 */
public class DoubleVotesCommand implements CommandExecutor {
    Vote4Dis instance = Vote4Dis.instance;
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<Integer> oi = args.getOne(Text.of("Days"));
        oi.ifPresent(integer -> {
            instance.voteDouble(integer);
            src.sendMessage(Text.of("Double votes enabled for " + oi.get() + " day(s)."));
        });
        return null;
    }
}
