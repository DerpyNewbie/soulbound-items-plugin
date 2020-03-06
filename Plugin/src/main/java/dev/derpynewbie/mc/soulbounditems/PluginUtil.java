package dev.derpynewbie.mc.soulbounditems;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.util.regex.Pattern;

public abstract class PluginUtil {
    private static boolean IS_DEBUG = true;
    private static final Pattern TRUE_PATTERN = Pattern.compile("true|enable|yes|on", Pattern.CASE_INSENSITIVE);
    private static final Pattern FALSE_PATTERN = Pattern.compile("false|disable|no|off", Pattern.CASE_INSENSITIVE);

    public static void sendDebug(CommandSender p, Object... values) {
        if (IS_DEBUG) {
            p.sendMessage(valuesToString("\nDEBUG", values));
        }
    }

    public static void sendValues(CommandSender p, Object... values) {
//        StringBuilder sb = new StringBuilder();
//        ChatColor lastColor = ChatColor.DARK_GRAY;
//
//        sb.append(ChatColor.BOLD).append("\nDEBUG:\n");
//        for (int i = 0; i < values.length; i++) {
//            if (i % 2 == 0) { // label
//                lastColor = (lastColor == ChatColor.GRAY ? ChatColor.DARK_GRAY : ChatColor.GRAY);
//                sb.append(lastColor).append(values[i]).append(": ");
//            } else { // value
//                ChatColor nextColor = (lastColor == ChatColor.GRAY ? ChatColor.WHITE : ChatColor.GRAY);
//                sb.append(nextColor).append(objectToString(values[i])).append("\n");
//            }
//
//        }

        p.sendMessage(valuesToString("\nVALUES", values));
    }

    public static String valuesToString(String prefix, Object... values) {
        StringBuilder sb = new StringBuilder(prefix).append(":").append("\n");
        ChatColor lastColor = ChatColor.DARK_GRAY;

        for (int i = 0; i < values.length; i++) {
            if (i % 2 == 0) { // label
                lastColor = (lastColor == ChatColor.GRAY ? ChatColor.DARK_GRAY : ChatColor.GRAY);
                sb.append(lastColor).append(values[i]).append(": ");
            } else { // value
                ChatColor nextColor = (lastColor == ChatColor.GRAY ? ChatColor.WHITE : ChatColor.GRAY);
                sb.append(nextColor).append(objectToString(values[i])).append("\n");
            }
        }

        return sb.toString();
    }

    private static String objectToString(Object o) {
        String s = o.toString();
        if (isBoolean(s))
            return colorBool(stringToBool(s));
        else if (o instanceof Player)
            return "CraftPlayer: " + ((Player) o).getName();
        else
            return s;
    }

    public static boolean stringToBool(String s) {
        return TRUE_PATTERN.matcher(s).matches();
    }

    public static String colorBool(boolean b) {
        return b ? ChatColor.GREEN + "true" : ChatColor.RED + "false";
    }

    public static boolean isBoolean(String s) {
        return TRUE_PATTERN.matcher(s).matches() || FALSE_PATTERN.matcher(s).matches();
    }

    public static void msg(CommandSender p, String path) {
        String s = SoulboundItemsPlugin.getInstance().getConfig().getString("msg."+path);
        if (s != null && !s.isEmpty()) {
            s = ChatColor.translateAlternateColorCodes('&', s);
            p.sendMessage(s);
        }
    }

    public static void msg(CommandSender p, String path, String label) {
        String s = SoulboundItemsPlugin.getInstance().getConfig().getString("msg."+path);
        if (s != null && !s.isEmpty()) {
            s = ChatColor.translateAlternateColorCodes('&', s);
            s = s.replaceAll("<command>", label);
            p.sendMessage(s);
        }
    }

    public static boolean isSameInventory(Inventory inv1, Inventory inv2) {
        return inv1 != null && inv2 != null && (inv1.getHolder() != null && inv1.getHolder() != null && (inv1.getHolder() == inv2.getHolder()));
    }

    public static boolean isSameInventory(Inventory inv1, HumanEntity inv2) {
        return inv1 != null && inv2 != null && (inv1.getHolder() != null && (inv1.getHolder() == inv2));
    }

    public static String describeInvView(InventoryView view) {
        return PluginUtil.valuesToString("InvView",
                "title", view.getTitle(),
                "type", view.getType(),
                "player", view.getPlayer(),
                "top", describeInv(view.getTopInventory()),
                "bottom", describeInv(view.getBottomInventory())
        );
    }

    public static String describeInv(Inventory inv) {
        return inv != null ? inv.getHolder() != null ? inv.getHolder().toString() : inv.toString() : "null";
    }
}
