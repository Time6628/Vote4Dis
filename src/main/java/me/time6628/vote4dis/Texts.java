package me.time6628.vote4dis;

import static org.spongepowered.api.text.TextTemplate.arg;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TimeTheCat on 2/22/2017.
 */
public class Texts {

    private static Text prefix;
    //private static String broadcastMessage;
    public static final Text voteCommandTitle = Text.builder().color(TextColors.DARK_PURPLE).append(Text.of("Vote Links")).build();
    public static final Text topVotersTitle = Text.builder().color(TextColors.DARK_PURPLE).append(Text.of("Top Voters")).build();
    public static final Text resetVoteTotals = Text.builder().color(TextColors.LIGHT_PURPLE).append(Text.of("Vote totals have been reset.")).build();

    public static void setPrefix(Text prefix) {
        Texts.prefix = prefix;
    }

    public static void setBroadcastMessage(TextTemplate broadcastMessage) {
        Texts.broadcastMessage = broadcastMessage;
    }

    public static void setVotesMessage(TextTemplate votesMessage) {
        Texts.votesMessage = votesMessage;
    }

    /*
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
    */

    public static TextTemplate votesMessage = TextTemplate.of(
            TextColors.LIGHT_PURPLE, "You have ",
            arg("votes").color(TextColors.WHITE),
            TextColors.LIGHT_PURPLE, " votes."
    );


    /*
    public static Text getBroadcastMessage(String name, String site) {
        return Text.builder()
                .append(prefix)
                .color(TextColors.LIGHT_PURPLE)
                .append(Text.of(String.format(broadcastMessage, name, site)))
                .build();
    }
    */

    public static List<Text> getVoteLinksAsText() {
        List<Text> texts = new ArrayList<>();
        for (String s : Vote4Dis.instance.getVoteLinks()) {
            texts.add(Text.builder().color(TextColors.LIGHT_PURPLE).append(Text.of(s)).build());
        }
        return texts;
    }

    public static TextTemplate broadcastMessage = TextTemplate.of(
            arg("player").color(TextColors.WHITE),
            TextColors.LIGHT_PURPLE, " has just voted at ",
            arg("service").color(TextColors.WHITE),
            TextColors.LIGHT_PURPLE, ", you can too with /vote!"
    );
}
