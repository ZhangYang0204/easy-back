package pers.zhangyang.easyback.domain;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easyback.enumeration.ManageBackPointPageStatsEnum;
import pers.zhangyang.easyback.manager.BackPointManager;
import pers.zhangyang.easyback.yaml.GuiYaml;
import pers.zhangyang.easylibrary.base.BackAble;
import pers.zhangyang.easylibrary.base.GuiPage;
import pers.zhangyang.easylibrary.base.MultipleGuiPageBase;
import pers.zhangyang.easylibrary.util.*;

import java.util.*;

public class ManageBackPointPage extends MultipleGuiPageBase implements BackAble {
    private List<BackPoint> backPointList;

    String searchWorldName;
    ManageBackPointPageStatsEnum stats;

    public ManageBackPointPage(@NotNull Player viewer, @Nullable GuiPage backPage, OfflinePlayer owner) {
        super(GuiYaml.INSTANCE.getString("gui.title.manageBackPointPage"), viewer, backPage, owner, 54);
    }
    public void searchByWorldName(@NotNull String name) {
        this.stats = ManageBackPointPageStatsEnum.SEARCH_WORLD_NAME;
        this.searchWorldName = name;
        this.pageIndex = 0;
        refresh();
    }
    @Override
    public void back() {
        List<String> cmdList = GuiYaml.INSTANCE.getStringList("gui.firstPageBackCommand");
        if (cmdList == null) {
            return;
        }
        CommandUtil.dispatchCommandList(viewer, cmdList);
    }

    @Override
    public int getBackSlot() {
        return 49;
    }

    @Override
    public void send() {

        this.stats = ManageBackPointPageStatsEnum.NORMAL;
        this.searchWorldName = null;
        this.pageIndex = 0;
        refresh();
    }

    @Override
    public void refresh() {

        Player onlineOwner = owner.getPlayer();
        if (onlineOwner == null) {
            back();
            return;
        }

        this.inventory.clear();

        List<BackPoint> backPointList = new ArrayList<>(BackPointManager.INSTANCE.getBackPointList(onlineOwner));
        Collections.reverse(backPointList);

        if (stats.equals(ManageBackPointPageStatsEnum.SEARCH_WORLD_NAME)){
            backPointList.removeIf(backPoint -> !Objects.requireNonNull(backPoint.getLocation().getWorld()).getName().contains(searchWorldName));
        }
        this.backPointList = PageUtil.page(this.pageIndex, 45, backPointList);

        for (int i = 0; i < 45; i++) {
            if (i >= backPointList.size()) {
                break;
            }

            ItemStack itemStack = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageBackPointPage.teleportBackPoint");
            BackPoint ask = backPointList.get(i);
            HashMap<String, String> rep = new HashMap<>();
            World world = ask.getLocation().getWorld();
            if (world == null) {
                rep.put("{world}", "/");
            } else {
                rep.put("{world}", ask.getLocation().getWorld().getName());
            }
            rep.put("{x}", String.valueOf(ask.getLocation().getX()));
            rep.put("{y}", String.valueOf(ask.getLocation().getY()));
            rep.put("{z}", String.valueOf(ask.getLocation().getZ()));
            rep.put("{create_time}", TimeUtil.getTimeFromTimeMill(ask.getTime()));

            ReplaceUtil.replaceDisplayName(itemStack, rep);
            ReplaceUtil.replaceLore(itemStack, rep);

            this.inventory.setItem(i, itemStack);
        }


        ItemStack returnPage = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageBackPointPage.back");
        this.inventory.setItem(49, returnPage);

        ItemStack searchByWorldName = GuiYaml.INSTANCE.getButtonDefault("gui.button.manageBackPointPage.searchByWorldName");
        this.inventory.setItem(47, searchByWorldName);

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

    @Override
    public int getPreviousPageSlot() {
        return 45;
    }

    @Override
    public int getNextPageSlot() {
        return 53;
    }

    public List<BackPoint> getBackPointList() {
        return backPointList;
    }
}
