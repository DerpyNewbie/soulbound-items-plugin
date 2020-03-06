package dev.derpynewbie.mc.soulbounditems;

import dev.derpynewbie.mc.soulbounditems.API.SoulboundItemsAPI;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class SoulboundImplementation extends SoulboundItemsAPI {

    private NamespacedKey soulboundKey;
    private Logger logger;

    @Override
    public boolean isSoulbound(@Nullable ItemStack itemStack) {
        UUID uuid = getSoulboundUUID(itemStack);
        return uuid != null;
    }

    @Override
    public boolean isSoulbound(@Nullable ItemStack itemStack, @NotNull UUID uuid) {
        UUID soulboundUUID = getSoulboundUUID(itemStack);
        return soulboundUUID == uuid;
    }

    @Override
    public boolean setSoulbound(@NotNull ItemStack itemStack, @NotNull UUID uuid) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.getPersistentDataContainer().set(soulboundKey, PersistentDataType.STRING, uuid.toString());
            addLore(itemMeta);
            itemStack.setItemMeta(itemMeta);
            return true;
        }

        return false;
    }

    @Override
    public boolean removeSoulbound(@NotNull ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.getPersistentDataContainer().remove(soulboundKey);
            removeLore(itemMeta);
            itemStack.setItemMeta(itemMeta);
            return true;
        }

        return false;
    }

    @Override
    public UUID getSoulboundUUID(@Nullable ItemStack itemStack) {
        if (itemStack == null || itemStack.getItemMeta() == null)
            return null;
        ItemMeta itemMeta = itemStack.getItemMeta();
        String strUUID = itemMeta.getPersistentDataContainer().getOrDefault(soulboundKey, PersistentDataType.STRING, "");
        if (strUUID.isEmpty())
            return null;
        return UUID.fromString(strUUID);
    }

    SoulboundImplementation(Plugin pl) {
        soulboundKey = new NamespacedKey(pl, "soulboundPlayer");
        logger = pl.getLogger();
    }

    private void addLore(ItemMeta meta) {
        List<String> lore = meta.getLore();
        if (lore == null)
            lore = new ArrayList<>();
        lore.add(0, ChatColor.GOLD + "Soulbound");
        meta.setLore(lore);
    }

    private void removeLore(ItemMeta meta) {
        List<String> lore = meta.getLore();
        if (lore == null)
            lore = new ArrayList<>();
        lore.remove(ChatColor.GOLD + "Soulbound");
        meta.setLore(lore);
    }
}
