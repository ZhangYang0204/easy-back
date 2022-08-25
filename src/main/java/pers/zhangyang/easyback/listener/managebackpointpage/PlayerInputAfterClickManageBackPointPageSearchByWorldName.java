package pers.zhangyang.easyback.listener.managebackpointpage;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyback.domain.ManageBackPointPage;
import pers.zhangyang.easyback.yaml.MessageYaml;
import pers.zhangyang.easylibrary.base.FiniteInputListenerBase;
import pers.zhangyang.easylibrary.base.GuiPage;
import pers.zhangyang.easylibrary.util.MessageUtil;

import java.util.List;

public class PlayerInputAfterClickManageBackPointPageSearchByWorldName extends FiniteInputListenerBase {


    public PlayerInputAfterClickManageBackPointPageSearchByWorldName(Player player, OfflinePlayer owner, GuiPage previousPage) {
        super(player, owner, previousPage, 1);

        List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.howToSearchByWorldName");
        MessageUtil.sendMessageTo(player, list);
    }

    @Override
    public void run() {
        ManageBackPointPage manageBackPointPage= (ManageBackPointPage) previousPage;
        manageBackPointPage.searchByWorldName(messages[0]);
    }
}
