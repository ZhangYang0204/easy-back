package pers.zhangyang.easyback.domain;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BackPoint {
    private final Player player;
    private final Location location;
    private final long time;


    public BackPoint(Player player, Location location, long time) {
        this.player = player;
        this.location = location;
        this.time = time;
    }

    public Player getPlayer() {
        return player;
    }

    public Location getLocation() {
        return location;
    }

    public long getTime() {
        return time;
    }
}
