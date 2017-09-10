package me.time6628.vote4dis.commands;

import com.vexsoftware.votifier.model.Vote;
import me.time6628.vote4dis.Texts;
import me.time6628.vote4dis.Vote4Dis;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

import java.util.HashMap;

/**
 * Created by TimeTheCat on 2/26/2017.
 */
public class TestVote implements CommandExecutor {
    private final Vote4Dis pl = Vote4Dis.instance;
    @Override
    public CommandResult execute(CommandSource src, CommandContext commandContext) throws CommandException {
        pl.handleVote((Player) src, new Vote("SERVERLIST", src.getName(), "127.0.0.1", "TIMESTAMP"));
        return CommandResult.success();
    }
}
