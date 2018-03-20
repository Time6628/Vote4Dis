package me.time6628.vote4dis.sponge.commands;

import me.time6628.vote4dis.sponge.Texts;
import me.time6628.vote4dis.sponge.Vote4DisSponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.pagination.PaginationList;

/**
 * Created by TimeTheCat on 2/26/2017.
 */
public class VoteCommand implements CommandExecutor {
    private final Vote4DisSponge pl = Vote4DisSponge.instance;


    @Override
    public CommandResult execute(CommandSource commandSource, CommandContext commandContext) throws CommandException {
        getPages().sendTo(commandSource);
        return CommandResult.success();
    }

    private PaginationList getPages() {
        return pl.getPaginationService().builder()
                .contents(Texts.getVoteLinksAsText())
                .title(Texts.voteCommandTitle)
                .build();
    }
}
