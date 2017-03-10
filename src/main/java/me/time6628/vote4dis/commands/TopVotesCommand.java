package me.time6628.vote4dis.commands;

import me.time6628.vote4dis.Texts;
import me.time6628.vote4dis.Vote4Dis;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by TimeTheCat on 3/1/2017.
 */
public class TopVotesCommand implements CommandExecutor {
    private final Vote4Dis pl = Vote4Dis.instance;

    @Override
    public CommandResult execute(CommandSource src, CommandContext commandContext) throws CommandException {
        pages(src);
        return CommandResult.success();
    }

    private void pages(CommandSource src) {
        pl.getPaginationService().builder()
                .contents(getTexts())
                .title(Texts.topVotersTitle)
                .build().sendTo(src);
    }

    private List<Text> getTexts() {
        Map<String, Integer> voters = pl.getTopVotes();
        Map<String, Integer> v = voters.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
        List<Text> texts = new ArrayList<>();
        v.forEach((s, s2) -> texts.add(Text.builder().append(Text.of(s2 + " - " + s)).build()));
        return texts;
    }
}
