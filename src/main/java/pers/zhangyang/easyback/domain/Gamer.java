package pers.zhangyang.easyback.domain;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class Gamer {
    private Player player;

    private Long lastBackTime;

    @Nullable
    public Long getLastBackTime() {
        return lastBackTime;
    }

    public Gamer(Player player){
        this.player=player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setLastBackTime(Long lastBackTime) {
        this.lastBackTime = lastBackTime;
    }
}
