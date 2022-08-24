package pers.zhangyang.easyback.executor;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easyback.manager.BackPointManager;
import pers.zhangyang.easyback.manager.GamerManager;
import pers.zhangyang.easyback.yaml.MessageYaml;
import pers.zhangyang.easylibrary.base.ExecutorBase;
import pers.zhangyang.easylibrary.util.MessageUtil;

import java.util.List;

public class ReloadPluginExecutor extends ExecutorBase {
    public ReloadPluginExecutor(@NotNull CommandSender sender, String commandName, @NotNull String[] args) {
        super(sender, commandName, args);
    }

    @Override
    protected void run() {
        if (args.length != 0) {
            return;
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            GamerManager.INSTANCE.remove(p);
        }
        BackPointManager.INSTANCE.getBackPointList().clear();

        List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.reloadPlugin");
        MessageUtil.sendMessageTo(this.sender, list);
    }
}
