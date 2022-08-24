package pers.zhangyang.easyback.domain;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class Gamer {
    private final Player player;

    private Long lastBackTime;

    public Gamer(Player player) {
        this.player = player;
    }

    @Nullable
    public Long getLastBackTime() {
        return lastBackTime;
    }

    public void setLastBackTime(Long lastBackTime) {
        this.lastBackTime = lastBackTime;
    }

    public Player getPlayer() {
        return player;
    }
}
