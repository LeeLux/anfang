package de.leelux.anfang.commands;

import de.leelux.anfang.Anfang;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandKit implements CommandExecutor {
    public CommandKit() {
    }

    public boolean onCommand(CommandSender cs, Command c, String s, String[] args) {
        if (cs instanceof Player) {
            Player player = (Player)cs;
            if (player.hasPermission("anfang.kit")) {
                if (args.length == 0) {
                    Inventory inv = Bukkit.createInventory((InventoryHolder)null, 9, "§eKit Selector");
                    ItemStack item = new ItemStack(Material.WOODEN_SWORD, 10);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName("§e§lStarter Kit");
                    ArrayList<String> lore = new ArrayList();
                    lore.add("");
                    lore.add("");
                    lore.add("§eClick to claim!");
                    meta.setLore(lore);
                    meta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
                    item.setItemMeta(meta);
                    inv.setItem(4, item);
                    player.openInventory(inv);
                    player.sendMessage("§eKit Selector opened!");
                } else {
                    player.sendMessage("§cUse /kit");
                }
            } else {
                player.sendMessage(Anfang.getPlugin().getMessage("Messages.NoPermission"));
            }
        } else {
            cs.sendMessage(Anfang.getPlugin().getMessage("Messages.NotaPlayer"));
        }

        return false;
    }
}
