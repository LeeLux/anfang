package de.leelux.anfang.manager;

import de.leelux.anfang.Anfang;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class ManageEnchantmax {
    private final Anfang plugin;

    public ManageEnchantmax(Anfang plugin) {
        this.plugin = plugin;
    }

    public void setHanditemtoEnchantmax(CommandSender sender, Enchantment enchantment, Integer level, Player target) {
        boolean sendertarget = sender.equals(target);
        //get target itemstack from his main hand
        ItemStack targethand = target.getInventory().getItemInMainHand();
        //if null send sender a message
        if(targethand.getType().equals(Material.AIR)){
            if(!(sendertarget)) sender.sendMessage(ChatColor.WHITE+target.getName()+" does not have any item in there main hand");
            if(sendertarget)sender.sendMessage(ChatColor.WHITE+"You do not have any item in you main hand");
            return;
        }
        if(plugin.getConfig().getBoolean("debug")) plugin.sendDebug(getClass().getName(),
                enchantment.getKey().toString() + nonegative(level) + target.getName());
        //modifying the item with new enchantment
        targethand.addUnsafeEnchantment(enchantment, nonegative(level));
        target.getInventory().setItemInMainHand(targethand);
        //sen confirm message to sender
        if(!(sendertarget)&&targethand.getItemMeta().hasDisplayName()) sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&fYou applied &7"+
                enchantment.getKey().toString().replace("minecraft:", "")+" "+nonegative(level)+"&f "+target.getName()+"'s "+targethand.getItemMeta().getDisplayName()));
        if(!(sendertarget)&&!(targethand.getItemMeta().hasDisplayName())) sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&fYou applied &7"+
                enchantment.getKey().toString().replace("minecraft:", "")+" "+nonegative(level)+"&f "+target.getName()+"'s "+targethand.getType().toString().toLowerCase(Locale.ROOT)));
        //if you applied it on your item
        if(sendertarget&&targethand.getItemMeta().hasDisplayName()) sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&fYou applied &7"+
                enchantment.getKey().toString().replace("minecraft:", "")+" "+nonegative(level)+"&f on your "+targethand.getItemMeta().getDisplayName()));
        if(sendertarget&&!(targethand.getItemMeta().hasDisplayName())) sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&fYou applied &7"+
                enchantment.getKey().toString().replace("minecraft:", "")+" "+nonegative(level)+"&f on your "+targethand.getType().toString().toLowerCase(Locale.ROOT)));
    }

    public int nonegative(int num){
        if(num < 0) num = num*-1;
        if(num > 255) num = 255;
        return num;
    }
}
