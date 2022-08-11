package pers.zhangyang.easyback.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import pers.zhangyang.easyback.domain.BackPoint;
import pers.zhangyang.easyback.manager.BackPointManager;
import pers.zhangyang.easylibrary.annotation.EventListener;

@EventListener
public class PlayerTeleport implements Listener {
    @EventHandler
    public void on(PlayerTeleportEvent event){
        Player player=event.getPlayer();
        BackPoint backPoint=new BackPoint(player,event.getFrom(), System.currentTimeMillis());
        BackPointManager.INSTANCE.addBackPoint(backPoint);
    }
}
