package me.time6628.vote4dis.commands.subcommands.v4d;

import me.time6628.vote4dis.Texts;
import me.time6628.vote4dis.Vote4Dis;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

/**
 * Created by TimeTheCat on 3/2/2017.
 */
public class ResetTotalsCommand implements CommandExecutor {
    private Vote4Dis pl = Vote4Dis.instance;
    @Override
    public CommandResult execute(CommandSource src, CommandContext commandContext) throws CommandException {
        pl.resetVoteTotals();
        src.sendMessage(Texts.resetVoteTotals);
        return CommandResult.success();
    }
}
