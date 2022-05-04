package de.leelux.anfang.manager;

import de.leelux.anfang.Anfang;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.List;

public class ItemBuilder {

    private final Plugin plugin = Anfang.getPlugin();

    public ItemBuilder(Plugin plugin) {
    }

    public ItemStack newItem(Material m,String displayname){
        if(m == null) m = Material.STONE;
        ItemStack item = new ItemStack(m);
        ItemMeta meta = item.getItemMeta();
        if(!(displayname == null)) meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',displayname));
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack newItem(Material m,int count,String displayname, List<String> lore){
        if(m == null) m = Material.STONE;
        if(count < 0) count = count*-1;
        if(count == 0) count = 1;
        ItemStack item = new ItemStack(m,count);
        ItemMeta meta = item.getItemMeta();
        if(!(displayname == null)) meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',displayname));
        if(!(lore == null)) meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack newItem(Material m,int count,String displayname, List<String> lore, boolean isUnbreakable, int CustomModelData){
        if(m == null) m = Material.STONE;
        if(count < 0) count = count*-1;
        if(count == 0) count = 1;
        ItemStack item = new ItemStack(m,count);
        ItemMeta meta = item.getItemMeta();
        if(!(displayname == null)) meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',displayname));
        if(!(lore == null)) meta.setLore(lore);
        meta.setUnbreakable(isUnbreakable);
        if(CustomModelData < 0) CustomModelData = CustomModelData*-1;
        if(CustomModelData == 0) CustomModelData = 1;
        meta.setCustomModelData(CustomModelData);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack newPotion(Material m, int count, String displayname, List<String> lore, Color color){
        if(m == null) m = Material.STONE;
        if(count < 0) count = count*-1;
        if(count == 0) count = 1;
        ItemStack item = new ItemStack(m,count);
        ItemMeta meta = item.getItemMeta();
        if(!(displayname == null)) meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',displayname));
        if(!(lore == null)) meta.setLore(lore);
        item.setItemMeta(meta);
        //potion
        PotionMeta pmeta = (PotionMeta) item.getItemMeta();
        if(!(color == null)) pmeta.setColor(color);
        item.setItemMeta(pmeta);
        return item;
    }

    public ItemStack newPotion(Material m, int count, String displayname, List<String> lore, PotionEffectType type, int durationInSeconds, int level, Color color){
        if(m == null) m = Material.STONE;
        if(count < 0) count = count*-1;
        if(count == 0) count = 1;
        ItemStack item = new ItemStack(m,count);
        ItemMeta meta = item.getItemMeta();
        if(!(displayname == null)) meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',displayname));
        if(!(lore == null)) meta.setLore(lore);
        item.setItemMeta(meta);
        //potion
        if(type == null) type = PotionEffectType.ABSORPTION;
        durationInSeconds = durationInSeconds*20;
        if(durationInSeconds < 0) durationInSeconds = durationInSeconds*-1;
        if(durationInSeconds == 0) durationInSeconds = 1;
        if(level < -1) level = level*-1;
        if(level == -1) level = 0;
        PotionMeta pmeta = (PotionMeta) item.getItemMeta();
        pmeta.addCustomEffect(new PotionEffect(type,durationInSeconds,level), true);
        if(!(color == null)) pmeta.setColor(color);
        item.setItemMeta(pmeta);
        return item;
    }

    public ItemStack newPotion(Material m, int count, String displayname, List<String> lore, PotionEffectType type, int durationInSeconds, int level, Color color, boolean isUnbreakable, int CustomModelData){
        if(m == null) m = Material.STONE;
        if(count < 0) count = count*-1;
        if(count == 0) count = 1;
        ItemStack item = new ItemStack(m,count);
        ItemMeta meta = item.getItemMeta();
        if(!(displayname == null)) meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',displayname));
        if(!(lore == null)) meta.setLore(lore);
        meta.setUnbreakable(isUnbreakable);
        if(CustomModelData < 0) CustomModelData = CustomModelData*-1;
        if(CustomModelData == 0) CustomModelData = 1;
        meta.setCustomModelData(CustomModelData);
        item.setItemMeta(meta);
        //potion
        if(type == null) type = PotionEffectType.ABSORPTION;
        durationInSeconds = durationInSeconds*20;
        if(durationInSeconds < 0) durationInSeconds = durationInSeconds*-1;
        if(durationInSeconds == 0) durationInSeconds = 1;
        if(level < -1) level = level*-1;
        if(level == -1) level = 0;
        PotionMeta pmeta = (PotionMeta) item.getItemMeta();
        pmeta.addCustomEffect(new PotionEffect(type,durationInSeconds,level), true);
        if(!(color == null)) pmeta.setColor(color);
        item.setItemMeta(pmeta);
        return item;
    }

}
