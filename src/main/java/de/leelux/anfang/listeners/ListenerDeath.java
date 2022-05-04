package de.leelux.anfang.listeners;

import de.leelux.anfang.Anfang;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class ListenerDeath implements Listener {
    public ListenerDeath() {
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        FileConfiguration config = Anfang.getPlugin().getConfig();
        config.set("Location." + p.getName() + ".OnDeath.world", p.getWorld().getName());
        config.set("Location." + p.getName() + ".OnDeath.x", p.getLocation().getX());
        config.set("Location." + p.getName() + ".OnDeath.y", p.getLocation().getY());
        config.set("Location." + p.getName() + ".OnDeath.z", p.getLocation().getZ());
        Anfang.getPlugin().saveConfig();
        String deathworld = config.getString("Location." + p.getName() + ".OnDeath.world");
        int deathx = config.getInt("Location." + p.getName() + ".OnDeath.x");
        int deathy = config.getInt("Location." + p.getName() + ".OnDeath.y");
        int deathz = config.getInt("Location." + p.getName() + ".OnDeath.z");
        Bukkit.getConsoleSender().sendMessage("§c" + p.getName() + "§e died at: §c" + deathx + " " + deathy + " " + deathz + "  §ein §c" + deathworld);
        p.sendMessage("§eYou died at: §c" + deathx + " " + deathy + " " + deathz + "  §ein §c" + deathworld);
    }
}