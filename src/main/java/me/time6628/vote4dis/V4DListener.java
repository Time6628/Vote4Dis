package me.time6628.vote4dis;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.sponge.event.VotifierEvent;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.Getter;

import java.util.Optional;

/**
 * Created by TimeTheCat on 2/22/2017.
 */
public class V4DListener {
    Vote4Dis pl = Vote4Dis.instance;

    @Listener
    public void onVote(VotifierEvent event, @Getter("getVote") Vote vote) {
        Optional<Player> p = pl.getGame().getServer().getPlayer(vote.getUsername());
        if (p.isPresent()) {
            Player player = p.get();
            pl.getGame().getServer().getBroadcastChannel().send(Texts.getBroadcastMessage(vote.getUsername(), vote.getServiceName()));
            pl.incrVote(player);
            pl.rewardPlayer(player);
        }
    }
}
