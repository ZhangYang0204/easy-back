package pers.zhangyang.easyback.listener.managebackpointpage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import pers.zhangyang.easyback.domain.ManageBackPointPage;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;

@EventListener
public class PlayerClickManageBackPointPageSearchByWorldName implements Listener {

    @GuiDiscreteButtonHandler(guiPage = ManageBackPointPage.class,closeGui = true,slot = {47},refreshGui = false)
    public void on(InventoryClickEvent event){

        ManageBackPointPage allGoodPage = (ManageBackPointPage) event.getInventory().getHolder();
        Player player = (Player) event.getWhoClicked();
        assert allGoodPage != null;
        new PlayerInputAfterClickManageBackPointPageSearchByWorldName(player, allGoodPage.getOwner(), allGoodPage);
    }
}
