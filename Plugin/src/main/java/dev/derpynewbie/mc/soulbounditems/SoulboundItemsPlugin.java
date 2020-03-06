package dev.derpynewbie.mc.soulbounditems;

import dev.derpynewbie.mc.soulbounditems.API.SoulboundItemsAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class SoulboundItemsPlugin extends JavaPlugin {

    private static SoulboundItemsPlugin INSTANCE;
    private static SoulboundItemsAPI API_IMPL;

    @Override
    public void onEnable() {
        super.onEnable();

        INSTANCE = this;

        initConfig();
        initEvent();
        initCommand();

    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @SuppressWarnings("WeakerAccess")
    public static SoulboundItemsAPI getAPI() {
        if (API_IMPL == null)
            API_IMPL = new SoulboundImplementation(getInstance());
        return API_IMPL;
    }

    @SuppressWarnings("WeakerAccess")
    public static SoulboundItemsPlugin getInstance() {
        return INSTANCE;
    }




    private void initConfig() {
        saveDefaultConfig();
        reloadConfig();
    }

    private void initEvent() {
        Bukkit.getPluginManager().registerEvents(new SoulboundListener(), this);
    }

    private void initCommand() {
        PluginCommand cmd = getCommand("soulbound");
        if (cmd == null) {
            getLogger().severe("Could not initialize command. disabling...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        TabExecutor cmdImpl = new SoulboundCommand();
        cmd.setExecutor(cmdImpl);
        cmd.setTabCompleter(cmdImpl);
    }

}
