package me.time6628.vote4dis.sponge.commands;

import me.time6628.vote4dis.sponge.Vote4DisSponge;
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
    private Vote4DisSponge instance = Vote4DisSponge.instance;
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<Integer> oi = args.getOne(Text.of("Days"));
        oi.ifPresent(integer -> {
            instance.voteDouble(integer);
            src.sendMessage(Text.of("Double votes enabled for " + oi.get() + " day(s)."));
        });
        return CommandResult.success();
    }
}
