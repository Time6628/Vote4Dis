package me.time6628.vote4dis.sponge.commands;

import com.vexsoftware.votifier.model.Vote;
import me.time6628.vote4dis.sponge.Vote4DisSponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

/**
 * Created by TimeTheCat on 2/26/2017.
 */
public class TestVote implements CommandExecutor {
    private final Vote4DisSponge pl = Vote4DisSponge.instance;
    @Override
    public CommandResult execute(CommandSource src, CommandContext commandContext) throws CommandException {
        pl.handleVote((Player) src, new Vote("SERVERLIST", src.getName(), "127.0.0.1", "TIMESTAMP"));
        return CommandResult.success();
    }
}
