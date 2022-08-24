package pers.zhangyang.easyback.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import pers.zhangyang.easyback.domain.BackPoint;
import pers.zhangyang.easyback.manager.BackPointManager;
import pers.zhangyang.easylibrary.annotation.EventListener;

@EventListener
public class PlayerDeath implements Listener {

    @EventHandler
    public void on(PlayerDeathEvent event) {
        Player player = event.getEntity();
        BackPoint backPoint = new BackPoint(player, event.getEntity().getLocation(), System.currentTimeMillis());
        BackPointManager.INSTANCE.addBackPoint(backPoint);
    }

}
