package me.time6628.vote4dis.sponge.commands;

import me.time6628.vote4dis.sponge.Vote4DisSponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TimeTheCat on 3/2/2017.
 */
public class V4DCommand implements CommandExecutor {
    private final Vote4DisSponge pl = Vote4DisSponge.instance;

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        pl.getPaginationService().builder().contents(getCommands()).title(Text.builder().color(TextColors.LIGHT_PURPLE).append(Text.of("Commands")).build()).sendTo(src);
        return CommandResult.success();
    }

    private List<Text> getCommands() {
        List<Text> texts = new ArrayList<>();
        texts.add(Text.builder().onClick(TextActions.suggestCommand("/v4d resettotals")).onHover(TextActions.showText(Text.of("Reset all vote totals."))).append(Text.of("/v4d resettotals")).build());
        return texts;
    }
}
