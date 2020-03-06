package dev.derpynewbie.mc.soulbounditems;

import dev.derpynewbie.mc.soulbounditems.API.SoulboundItemsAPI;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SoulboundListener implements Listener {

    private final InventoryAction[] DROP_ACTIONS = new InventoryAction[]{InventoryAction.DROP_ALL_CURSOR, InventoryAction.DROP_ALL_SLOT, InventoryAction.DROP_ONE_CURSOR, InventoryAction.DROP_ALL_CURSOR};

    @EventHandler
    public void onItemDropped(EntityDropItemEvent event) {
        SoulboundItemsAPI api = SoulboundItemsPlugin.getAPI();
        boolean isDropSoulbound = api.isSoulbound(event.getItemDrop().getItemStack());
        boolean shouldReturnDropped = SoulboundItemsPlugin.getInstance().getConfig().getBoolean("cfg.return-dropped");

        PluginUtil.sendDebug(event.getEntity(),
                "drop", isDropSoulbound);

        if (isDropSoulbound) {
            event.setCancelled(true);
//            event.getEntity().sendMessage("cancelled");

            if ((event.getEntity() instanceof InventoryHolder) && shouldReturnDropped) {
                ItemStack droppedItem = event.getItemDrop().getItemStack();
                ((InventoryHolder) event.getEntity()).getInventory().addItem(droppedItem);
            }
            PluginUtil.msg(event.getEntity(), "item.illegal-drop");
        }
    }

    @EventHandler
    public void onInvDragged(InventoryDragEvent event) {
        SoulboundItemsAPI api = SoulboundItemsPlugin.getAPI();
        boolean isCursorItemSoulbound = api.isSoulbound(event.getCursor());
        boolean isOldCursorItemSoulbound = api.isSoulbound(event.getOldCursor());
        Inventory toWhere = event.getView().getInventory(Collections.min(event.getRawSlots()));
        boolean isSameInventory = PluginUtil.isSameInventory(toWhere, event.getWhoClicked());

        PluginUtil.sendDebug(event.getWhoClicked(),
                "type", event.getType(),
                "slots", event.getRawSlots(),
                "whoClicked", event.getWhoClicked(),
                "invView", PluginUtil.describeInvView(event.getView()),
                "toWhere", PluginUtil.describeInv(toWhere));

        if ((isCursorItemSoulbound || isOldCursorItemSoulbound) && !isSameInventory) {
            event.setCancelled(true);
            PluginUtil.msg(event.getWhoClicked(), "item.illegal-drag");
        }
    }

    @EventHandler
    public void onInvClicked(InventoryClickEvent event) {
        SoulboundItemsAPI api = SoulboundItemsPlugin.getAPI();
        HumanEntity whoClicked = event.getWhoClicked();
        InventoryView view = event.getView();
        boolean isCursorItemSoulbound = api.isSoulbound(event.getCursor());
        boolean isCurrentItemSoulbound = api.isSoulbound(event.getCurrentItem());
        Inventory toWhere = event.getView().getInventory(event.getRawSlot());
        boolean isSameInventory = PluginUtil.isSameInventory(toWhere, event.getWhoClicked());
        boolean isDropping = Arrays.stream(DROP_ACTIONS).anyMatch(inventoryAction -> event.getAction() == inventoryAction);
        boolean isMoving = event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY;
        boolean shouldReturnDropped = SoulboundItemsPlugin.getInstance().getConfig().getBoolean("cfg.return-dropped");

        PluginUtil.sendDebug(whoClicked,
                "cursor", isCursorItemSoulbound,
                "current", isCurrentItemSoulbound,
                "isSameInv", isSameInventory,
                "action", event.getAction(),
                "isDropping", isDropping,
                "invView", PluginUtil.describeInvView(event.getView()));

        if (isMoving && PluginUtil.isSameInventory(view.getTopInventory(), view.getBottomInventory())) {
            return;
        }

        if (((isCursorItemSoulbound || isCurrentItemSoulbound) && (!isSameInventory || isDropping || isMoving))) {
            if (isDropping && !shouldReturnDropped) {
                event.setCurrentItem(null);
            } else {
                event.setCancelled(true);
            }

            PluginUtil.msg(whoClicked, "item.illegal-move");
        }
    }

    @EventHandler
    public void onPlayerDied(PlayerDeathEvent event) {
        List<ItemStack> items = event.getDrops();
        SoulboundItemsAPI api = SoulboundItemsPlugin.getAPI();
        ItemStack[] soulboundItems = items.stream().filter(api::isSoulbound).toArray(ItemStack[]::new);
        Arrays.stream(soulboundItems).forEach(items::remove);
        if (soulboundItems.length > 0) {
            PluginUtil.msg(event.getEntity(), "item.died");
        }
    }



}
