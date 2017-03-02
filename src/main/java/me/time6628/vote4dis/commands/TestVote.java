package me.time6628.vote4dis.commands;

import me.time6628.vote4dis.Texts;
import me.time6628.vote4dis.Vote4Dis;
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
    private Vote4Dis pl = Vote4Dis.instance;
    @Override
    public CommandResult execute(CommandSource src, CommandContext commandContext) throws CommandException {
        pl.getGame().getServer().getBroadcastChannel().send(Texts.getBroadcastMessage(src.getName(), "FTBSERVERS"));
        pl.incrVote((Player) src);
        pl.rewardPlayer((Player) src);
        return null;
    }
}
