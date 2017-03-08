package me.time6628.vote4dis;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TimeTheCat on 2/22/2017.
 */
public class Texts {

    private static Text prefix;
    private static String broadcastMessage;
    public static final Text voteCommandTitle = Text.builder().color(TextColors.DARK_PURPLE).append(Text.of("Vote Links")).build();
    public static final Text topVotersTitle = Text.builder().color(TextColors.DARK_PURPLE).append(Text.of("Top Voters")).build();
    public static final Text resetVoteTotals = Text.builder().color(TextColors.LIGHT_PURPLE).append(Text.of("Vote totals have been reset.")).build();

    public static void setPrefix(Text prefix) {
        Texts.prefix = prefix;
    }

    public static void setBroadcastMessage(String broadcastMessage) {
        Texts.broadcastMessage = broadcastMessage;
    }

    public static Text getVotesMessage(Integer votes) {
        return Text.builder()
                .color(TextColors.LIGHT_PURPLE)
                .append(Text.of("You have "))
                .append(Text.builder()
                        .color(TextColors.WHITE)
                        .append(Text.of(votes))
                        .build())
                .append(Text.of(" votes."))
                .build();
    }

    public static Text getBroadcastMessage(String name, String site) {
        return Text.builder()
                .append(prefix)
                .color(TextColors.LIGHT_PURPLE)
                .append(Text.of(String.format(broadcastMessage, name, site)))
                .build();
    }

    public static List<Text> getVoteLinksAsText() {
        List<Text> texts = new ArrayList<>();
        for (String s : Vote4Dis.instance.getVoteLinks()) {
            texts.add(Text.builder().color(TextColors.LIGHT_PURPLE).append(Text.of(s)).build());
        }
        return texts;
    }
}
