package pers.zhangyang.easyback.listener.managebackpointpage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import pers.zhangyang.easyback.domain.BackPoint;
import pers.zhangyang.easyback.domain.Gamer;
import pers.zhangyang.easyback.domain.ManageBackPointPage;
import pers.zhangyang.easyback.manager.BackPointManager;
import pers.zhangyang.easyback.manager.GamerManager;
import pers.zhangyang.easyback.yaml.SettingYaml;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;
import pers.zhangyang.easylibrary.annotation.GuiSerialButtonHandler;
import pers.zhangyang.easylibrary.other.vault.Vault;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.PermUtil;
import pers.zhangyang.easylibrary.yaml.MessageYaml;

import java.util.List;

@EventListener
public class PlayerClickManageBackPointPageBackPoint implements Listener {
    @GuiSerialButtonHandler(guiPage = ManageBackPointPage.class,from = 0,to = 44)
    public void on(InventoryClickEvent event){

        ManageBackPointPage manageBackPointPage= (ManageBackPointPage) event.getInventory().getHolder();

        int slot=event.getRawSlot();

        Player viewer= (Player) event.getWhoClicked();

        assert manageBackPointPage != null;

        BackPoint backPoint=manageBackPointPage.getBackPointList().get(slot);
        if (!BackPointManager.INSTANCE.getBackPointList().contains(backPoint)){
            List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.notExistBackPoint");
            MessageUtil.sendMessageTo(viewer, list);
            manageBackPointPage.refresh();
            return;
        }
        Player onlineOwner=manageBackPointPage.getOwner().getPlayer();
        if (onlineOwner==null){
            List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.notOnline");
            MessageUtil.sendMessageTo(viewer, list);
            return;
        }

        if (!onlineOwner.isOp()) {
            Integer perm = PermUtil.getMinNumberPerm("EasyBack.backBackPointInterval.", onlineOwner);
            if (perm == null) {
                perm = 0;
            }
            Gamer gamer= GamerManager.INSTANCE.getGamer(onlineOwner);
            if (gamer.getLastBackTime() != null && System.currentTimeMillis() - gamer.getLastBackTime()
                    < perm * 1000L) {

                List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.tooFastWhenBackBackPoint");
                MessageUtil.sendMessageTo(viewer, list);
                return;
            }
        }


        if (Vault.hook()==null){
            List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.notHookVault");
            MessageUtil.sendMessageTo(viewer, list);
            return;
        }
        if (Vault.hook().getBalance(onlineOwner)< SettingYaml.INSTANCE.backCost()){
            List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.notEnoughVaultWhenBackBackPoint");
            MessageUtil.sendMessageTo(viewer, list);
            return;
        }
        Vault.hook().withdrawPlayer(onlineOwner,SettingYaml.INSTANCE.backCost());



        viewer.teleport(backPoint.getLocation());
        Gamer gamer= GamerManager.INSTANCE.getGamer(onlineOwner);
        gamer.setLastBackTime(System.currentTimeMillis());

        manageBackPointPage.refresh();

        List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.backBackPoint");
        MessageUtil.sendMessageTo(viewer, list);


    }
}