package pers.zhangyang.easyback.domain;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easyback.manager.BackPointManager;
import pers.zhangyang.easyback.yaml.GuiYaml;
import pers.zhangyang.easylibrary.base.BackAble;
import pers.zhangyang.easylibrary.base.GuiPage;
import pers.zhangyang.easylibrary.base.MultipleGuiPageBase;
import pers.zhangyang.easylibrary.util.CommandUtil;
import pers.zhangyang.easylibrary.util.PageUtil;
import pers.zhangyang.easylibrary.util.ReplaceUtil;
import pers.zhangyang.easylibrary.util.TimeUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ManageBackPointPage extends MultipleGuiPageBase implements BackAble {
    public ManageBackPointPage( @NotNull Player viewer, @Nullable GuiPage backPage, OfflinePlayer owner) {
        super(GuiYaml.INSTANCE.getString("gui.title.manageBackPointPage"), viewer, backPage, owner);
    }

    private List<BackPoint> backPointList;
    @Override
    public void back() {
        List<String> cmdList= GuiYaml.INSTANCE.getStringList("gui.firstPageBackCommand");
        if (cmdList==null){
            return;
        }
        CommandUtil.dispatchCommandList(viewer,cmdList);
    }

    @Override
    public void send() {
        this.pageIndex=0;
        refresh();
    }

    @Override
    public void refresh() {

        Player onlineOwner=owner.getPlayer();
        if (onlineOwner==null){
            back();
            return;
        }

        this.inventory.clear();
        List<BackPoint> backPointList=new ArrayList<>(BackPointManager.INSTANCE.getBackPointList(onlineOwner));
        Collections.reverse(backPointList);
        this.backPointList= PageUtil.page(this.pageIndex,45,backPointList );

        for (int i=0;i<45;i++){
            if (i >= backPointList.size()) {
                break;
            }

            ItemStack itemStack=GuiYaml.INSTANCE.getButtonDefault("gui.button.manageBackPointPage.teleportBackPoint");
            BackPoint ask=backPointList.get(i);
            HashMap<String,String> rep=new HashMap<>();
            World world=ask.getLocation().getWorld();
            if (world==null) {
                rep.put("{world}", "/");
            }else {
                rep.put("{world}", ask.getLocation().getWorld().getName());
            }
            rep.put("{x}", String.valueOf(ask.getLocation().getX()));
            rep.put("{y}", String.valueOf(ask.getLocation().getY()));
            rep.put("{z}", String.valueOf(ask.getLocation().getZ()));
            rep.put("{create_time}", TimeUtil.getTimeFromTimeMill(ask.getTime()));

            ReplaceUtil.replaceDisplayName(itemStack,rep);
            ReplaceUtil.replaceLore(itemStack,rep);

            this.inventory.setItem(i,itemStack);
        }




        ItemStack returnPage= GuiYaml.INSTANCE.getButtonDefault("gui.button.manageBackPointPage.back");
        this.inventory.setItem(49,returnPage);




        if (pageIndex > 0) {
            ItemStack previous = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageBackPointPage.previousPage");
            inventory.setItem(45, previous);
        }
        int maxIndex = PageUtil.computeMaxPageIndex(BackPointManager.INSTANCE.getBackPointList(onlineOwner).size(), 45);
        if (pageIndex > maxIndex) {
            this.pageIndex = maxIndex;
        }
        if (pageIndex < maxIndex) {
            ItemStack next = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageBackPointPage.nextPage");
            inventory.setItem(53, next);
        }

        viewer.openInventory(this.inventory);
    }

    public List<BackPoint> getBackPointList() {
        return backPointList;
    }
}
