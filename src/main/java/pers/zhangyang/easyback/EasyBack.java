package pers.zhangyang.easyback;

import org.bstats.bukkit.Metrics;
import pers.zhangyang.easylibrary.EasyPlugin;

public class EasyBack extends EasyPlugin {
    @Override
    public void onOpen() {

        // bStats统计信息
        new Metrics(EasyBack.instance, 16092);
    }

    @Override
    public void onClose() {

    }
}
