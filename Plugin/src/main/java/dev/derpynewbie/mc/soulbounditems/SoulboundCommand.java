package dev.derpynewbie.mc.soulbounditems;

import dev.derpynewbie.mc.soulbounditems.API.SoulboundItemsAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SoulboundCommand implements TabExecutor {

    private static Pattern CHECK_PATTERN = Pattern.compile("check|stats", Pattern.CASE_INSENSITIVE);
    private static Pattern SET_PATTERN = Pattern.compile("set|add", Pattern.CASE_INSENSITIVE);
    private static Pattern REMOVE_PATTERN = Pattern.compile("remove|clear", Pattern.CASE_INSENSITIVE);
    private static Pattern RELOAD_PATTERN = Pattern.compile("reload", Pattern.CASE_INSENSITIVE);
    private static Pattern HELP_PATTERN = Pattern.compile("help|h", Pattern.CASE_INSENSITIVE);
    private static List<String> POSSIBLE_COMMANDS = Arrays.asList("help", "reload", "check", "set", "remove");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0)
            return commandHelp(sender, label, args);

        String match = args[0];

        if (CHECK_PATTERN.matcher(match).matches())
            return commandCheck(sender);
        else if (SET_PATTERN.matcher(match).matches())
            return commandSet(sender);
        else if (REMOVE_PATTERN.matcher(match).matches())
            return commandRemove(sender);
        else if (RELOAD_PATTERN.matcher(match).matches())
            return commandReload(sender);
        else
            return commandHelp(sender, label, args);
    }

    private boolean commandReload(CommandSender sender) {
        sender.sendMessage("Reloading...");
        SoulboundItemsPlugin.getInstance().reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "Successfully reloaded.");
        return true;
    }

    private boolean commandRemove(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command cannot be executed from console.");
            return true;
        }

        SoulboundItemsAPI api = SoulboundItemsPlugin.getAPI();
        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();

        api.removeSoulbound(item);
        sender.sendMessage(ChatColor.GREEN + "Successfully removed soulbound from item.");
        return true;
    }

    private boolean commandSet(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command cannot be executed from console.");
            return true;
        }

        SoulboundItemsAPI api = SoulboundItemsPlugin.getAPI();
        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();

        api.setSoulbound(item, player.getUniqueId());
        sender.sendMessage(ChatColor.GREEN + "Successfully set soulbound on item.");
        return true;
    }

    private boolean commandCheck(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command cannot be executed from console.");
            return true;
        }

        SoulboundItemsAPI api = SoulboundItemsPlugin.getAPI();
        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();

        boolean boolResult = api.isSoulbound(item);
        UUID uuidResult = api.getSoulboundUUID(item);

        sender.sendMessage(ChatColor.GRAY + "IS SOULBOUND: " + PluginUtil.colorBool(boolResult));
        sender.sendMessage(ChatColor.DARK_GRAY + "BY :" + ChatColor.WHITE + uuidResult);
        return true;
    }

    private boolean commandHelp(CommandSender sender, String label, String[] args) {
        if (args.length <= 1) {
            PluginUtil.msg(sender, "help.index", label);
            return true;
        }

        FileConfiguration config = SoulboundItemsPlugin.getInstance().getConfig();
        String match = args[1];

        if (CHECK_PATTERN.matcher(match).matches())
            PluginUtil.msg(sender, "help.check", label);
        else if (SET_PATTERN.matcher(match).matches())
            PluginUtil.msg(sender, "help.set", label);
        else if (REMOVE_PATTERN.matcher(match).matches())
            PluginUtil.msg(sender, "help.remove", label);
        else if (RELOAD_PATTERN.matcher(match).matches())
            PluginUtil.msg(sender, "help.reload", label);
        else
            PluginUtil.msg(sender, "help.index", label);
        return true;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 0) {
            return POSSIBLE_COMMANDS;
        } else if (args.length == 1) {
            return POSSIBLE_COMMANDS.stream().filter(s -> s.startsWith(args[0])).sorted().collect(Collectors.toCollection(ArrayList::new));
        } else if (args.length == 2) {
            if (HELP_PATTERN.matcher(args[0]).matches()) {
                return POSSIBLE_COMMANDS.stream().filter(s -> s.startsWith(args[1])).sorted().collect(Collectors.toCollection(ArrayList::new));
            }
        }

        return null;
    }

}
