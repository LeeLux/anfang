package de.leelux.anfang.menusysytem.menus.commandPlayer;

import de.leelux.anfang.Anfang;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class UpdatePlayerheadLore {
    FileConfiguration config = Anfang.getPlugin().getConfig();
    public ArrayList<String> update(Player o, boolean onrightclick) {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7DisplayName: &e" + o.getDisplayName()));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Health: &e" + o.getHealth()));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Food Level: &e" + o.getFoodLevel()));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Ping: &e" + o.getPing()));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7IP Address: &e" + o.getAddress()));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7View Distance: &e" + o.getClientViewDistance()));
        //lore.add(ChatColor.translateAlternateColorCodes('&', "&7First Played:     &e"+o.getFirstPlayed()));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Gamemode: &e" + o.getGameMode()));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Current XYZ: &e"
                + (int) o.getLocation().getX() + " "
                + (int) o.getLocation().getY() + " "
                + (int) o.getLocation().getZ()));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Last Joined XYZ: &e"
                + config.getInt("Location." + o.getName() + ".OnJoin.x") + " "
                + config.getInt("Location." + o.getName() + ".OnJoin.y") + " "
                + config.getInt("Location." + o.getName() + ".OnJoin.z")));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Last Quited XYZ: &e"
                + config.getInt("Location." + o.getName() + ".OnQuit.x") + " "
                + config.getInt("Location." + o.getName() + ".OnQuit.y") + " "
                + config.getInt("Location." + o.getName() + ".OnQuit.z")));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Last Death XYZ: &e"
                + config.getInt("Location." + o.getName() + ".OnDeath.x") + " "
                + config.getInt("Location." + o.getName() + ".OnDeath.y") + " "
                + config.getInt("Location." + o.getName() + ".OnDeath.z")));
        lore.add(ChatColor.translateAlternateColorCodes('&', ""));
        if (onrightclick) {
            lore.add(ChatColor.translateAlternateColorCodes('&', "&6&oRight click to update stats"));
        } else {
            lore.add(ChatColor.translateAlternateColorCodes('&', "&6&oClick to update stats"));
        }

        return lore;

    }
}
