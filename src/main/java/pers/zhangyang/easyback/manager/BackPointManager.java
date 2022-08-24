package pers.zhangyang.easyback.manager;

import org.bukkit.entity.Player;
import pers.zhangyang.easyback.domain.BackPoint;

import java.util.ArrayList;
import java.util.List;

public class BackPointManager {
    public static final BackPointManager INSTANCE = new BackPointManager();
    private final List<BackPoint> backPointList = new ArrayList<>();

    public List<BackPoint> getBackPointList() {
        return backPointList;
    }

    public List<BackPoint> getBackPointList(Player player) {
        List<BackPoint> teleportAskList = new ArrayList<>();
        for (BackPoint t : this.backPointList) {
            if (t.getPlayer().equals(player)) {
                teleportAskList.add(t);
            }
        }
        return teleportAskList;
    }

    public void remove(Player player) {
        backPointList.removeIf(teleportAsk -> teleportAsk.getPlayer().equals(player));
    }

    public void addBackPoint(BackPoint backPoint) {
        this.backPointList.add(backPoint);
    }
}
