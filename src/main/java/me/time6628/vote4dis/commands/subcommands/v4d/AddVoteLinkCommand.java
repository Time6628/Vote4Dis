package me.time6628.vote4dis.commands.subcommands.v4d;

import me.time6628.vote4dis.Texts;
import me.time6628.vote4dis.Vote4Dis;
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
public class AddVoteLinkCommand implements CommandExecutor {
    private Vote4Dis pl = Vote4Dis.instance;

    @Override public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<String> ol = args.getOne(Text.of("link"));
        ol.ifPresent(s -> {
            pl.addVoteLink(s);
            src.sendMessage(Texts.rewardCmdMessage.apply(pl.getTextTemplateMap("link", ol.get())).build());
        });
        return CommandResult.success();
    }
}
