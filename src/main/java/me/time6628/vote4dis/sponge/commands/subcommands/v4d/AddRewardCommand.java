package me.time6628.vote4dis.sponge.commands.subcommands.v4d;

import me.time6628.vote4dis.sponge.Vote4DisSponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import java.util.Optional;

/**
 * Created by TimeTheCat on 3/9/2017.
 */
public class AddRewardCommand implements CommandExecutor {
    private Vote4DisSponge pl = Vote4DisSponge.instance;

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<String> ore = args.getOne(Text.of("reward"));
        ore.ifPresent(s -> pl.addReward(s));

        return CommandResult.success();
    }
}
