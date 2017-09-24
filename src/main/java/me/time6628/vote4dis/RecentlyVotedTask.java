package me.time6628.vote4dis;

public class RecentlyVotedTask implements Runnable {

    private Vote4Dis pl = Vote4Dis.instance;

    @Override
    public void run() {
        pl.getGame().getServer().getOnlinePlayers().forEach(player -> {
            if (!pl.hasVotedRecently(player.getUniqueId())) {
                Texts.hasVotedRecently.sendTo(player);
            }
        });
    }
}
