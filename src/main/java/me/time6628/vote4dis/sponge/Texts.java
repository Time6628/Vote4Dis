package me.time6628.vote4dis.sponge;

import static org.spongepowered.api.text.TextTemplate.arg;

import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.net.MalformedURLException;
import java.net.URL;
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
    public static Text DOUBLE_VOTES = Text.builder().color(TextColors.LIGHT_PURPLE).append(Text.of("Double Rewards are currently active for voting, double all rewards!")).build();
    private static String broadcastMessage;
    private static String votesMessage;

    public static void setPrefix(Text prefix) {
        Texts.prefix = prefix;
    }

    public static void setBroadcastMessage(String broadcastMessage) {
        Texts.broadcastMessage = broadcastMessage;
    }

    public static void setVotesMessage(String votesMessage) {
        Texts.votesMessage = votesMessage;
    }

    /*
    public static TextTemplate votesMessage = TextTemplate.of(
            TextColors.LIGHT_PURPLE, "You have ",
            arg("votes").color(TextColors.WHITE),
            TextColors.LIGHT_PURPLE, " votes."
    );
    */


    public static Text formatVM (Integer votes) {
        return TextSerializers.FORMATTING_CODE.deserialize(String.format(votesMessage, votes));
    }

    public static PaginationList hasVotedRecently() {
        return Vote4DisSponge.instance.getPaginationService().builder()
                .title(Text.builder().color(TextColors.DARK_PURPLE).append(Text.of("Have you recently voted?")).build())
                .contents(Text.builder().onClick(TextActions.suggestCommand("vote")).append(Text.of("Have you voted recently? Do /vote to get your daily voting rewards!")).build())
                .build();
    }


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
        for (String s : Vote4DisSponge.instance.getVoteLinks()) {
            try {
                texts.add(Text.of(TextColors.LIGHT_PURPLE, TextActions.openUrl(new URL(s)), s));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return texts;
    }

    /*
    public static TextTemplate broadcastMessage = TextTemplate.of(
            arg("player").color(TextColors.WHITE),
            TextColors.LIGHT_PURPLE, " has just voted at ",
            arg("service").color(TextColors.WHITE),
            TextColors.LIGHT_PURPLE, ", you can too with /vote!"
    );
    */


    public static Text formatBM(String name, String service) {
        return TextSerializers.FORMATTING_CODE.deserialize(String.format(broadcastMessage, name, service));
    }

    public static TextTemplate rewardCmdMessage = TextTemplate.of(
            TextColors.LIGHT_PURPLE, "Added ",
            arg("link").color(TextColors.WHITE),
            TextColors.LIGHT_PURPLE, " to the list of vote rewards."
    );
}
