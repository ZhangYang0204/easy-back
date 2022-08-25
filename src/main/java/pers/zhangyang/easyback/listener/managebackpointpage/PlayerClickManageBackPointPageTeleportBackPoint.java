package pers.zhangyang.easyback.listener.managebackpointpage;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import pers.zhangyang.easyback.domain.BackPoint;
import pers.zhangyang.easyback.domain.Gamer;
import pers.zhangyang.easyback.domain.ManageBackPointPage;
import pers.zhangyang.easyback.manager.BackPointManager;
import pers.zhangyang.easyback.manager.GamerManager;
import pers.zhangyang.easyback.yaml.MessageYaml;
import pers.zhangyang.easyback.yaml.SettingYaml;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiSerialButtonHandler;
import pers.zhangyang.easylibrary.other.vault.Vault;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.PermUtil;

import java.util.List;

@EventListener
public class PlayerClickManageBackPointPageTeleportBackPoint implements Listener {
    @GuiSerialButtonHandler(guiPage = ManageBackPointPage.class, from = 0, to = 44,closeGui = true,refreshGui = false)
    public void on(InventoryClickEvent event) {

        ManageBackPointPage manageBackPointPage = (ManageBackPointPage) event.getInventory().getHolder();

        int slot = event.getRawSlot();

        Player viewer = (Player) event.getWhoClicked();

        assert manageBackPointPage != null;

        BackPoint backPoint = manageBackPointPage.getBackPointList().get(slot);
        if (!BackPointManager.INSTANCE.getBackPointList().contains(backPoint)) {
            List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.notExistBackPoint");
            MessageUtil.sendMessageTo(viewer, list);
            return;
        }
        Player onlineOwner = manageBackPointPage.getOwner().getPlayer();
        if (onlineOwner == null) {
            List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.notOnline");
            MessageUtil.sendMessageTo(viewer, list);
            return;
        }

        if (!onlineOwner.isOp()) {
            Integer perm = PermUtil.getMinNumberPerm("EasyBack.teleportBackPointInterval.", onlineOwner);
            if (perm == null) {
                perm = 0;
            }
            Gamer gamer = GamerManager.INSTANCE.getGamer(onlineOwner);
            if (gamer.getLastBackTime() != null && System.currentTimeMillis() - gamer.getLastBackTime()
                    < perm * 1000L) {

                List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.tooFastWhen");
                MessageUtil.sendMessageTo(viewer, list);
                return;
            }
        }


        List<String> worldNameBlackList = SettingYaml.INSTANCE.getStringList("setting.worldBlackList");
        World world = backPoint.getLocation().getWorld();
        if (world != null && worldNameBlackList != null && worldNameBlackList.contains(world.getName())) {
            List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.worldBlackList");
            MessageUtil.sendMessageTo(viewer, list);
            return;
        }

        Double cost = SettingYaml.INSTANCE.getNonnegativeDouble("setting.teleportBackPointCost");
        if (cost != null) {
            if (Vault.hook() == null) {
                List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.notHookVault");
                MessageUtil.sendMessageTo(viewer, list);
                return;
            }
            if (Vault.hook().getBalance(onlineOwner) < cost) {
                List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.notEnoughVault");
                MessageUtil.sendMessageTo(viewer, list);
                return;
            }
            Vault.hook().withdrawPlayer(onlineOwner, cost);
        }

        viewer.teleport(backPoint.getLocation());
        Gamer gamer = GamerManager.INSTANCE.getGamer(onlineOwner);
        gamer.setLastBackTime(System.currentTimeMillis());

        List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.teleportBackPoint");
        MessageUtil.sendMessageTo(viewer, list);


    }
}
