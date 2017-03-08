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
class V4DListener {
    private final Vote4Dis pl = Vote4Dis.instance;

    @Listener
    public void onVote(VotifierEvent event, @Getter("getVote") Vote vote) {
        Optional<Player> p = pl.getGame().getServer().getPlayer(vote.getUsername());
        p.ifPresent(player -> pl.handleVote(player, vote));
    }
}
