package dev.derpynewbie.mc.soulbounditems.API;

import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class SoulboundItemsAPI {

    public abstract boolean isSoulbound(@Nullable ItemStack itemStack);

    public abstract boolean isSoulbound(@Nullable ItemStack itemStack, @NotNull UUID uniqueId);

    public abstract boolean setSoulbound(@NotNull ItemStack itemStack, @NotNull UUID uniqueId);

    public abstract boolean removeSoulbound(@NotNull ItemStack itemStack);

    @Nullable
    public abstract UUID getSoulboundUUID(@Nullable ItemStack itemStack);

}
