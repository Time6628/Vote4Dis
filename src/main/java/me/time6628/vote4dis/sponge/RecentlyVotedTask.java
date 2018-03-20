package me.time6628.vote4dis.sponge;

public class RecentlyVotedTask implements Runnable {

    private Vote4DisSponge pl = Vote4DisSponge.instance;

    @Override
    public void run() {
        pl.getGame().getServer().getOnlinePlayers().forEach(player -> {
            if (pl.hasVotedRecently(player.getUniqueId())) {
                Texts.hasVotedRecently().sendTo(player);
            }
        });
    }
}
