package de.leelux.anfang.listeners;

import de.leelux.anfang.Anfang;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ListenerQuit implements Listener {
    public ListenerQuit() {
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage("§c- §7" + event.getPlayer().getName());
        FileConfiguration config = Anfang.getPlugin().getConfig();
        Player p = event.getPlayer();
        config.set("Location." + p.getName() + ".OnQuit.world", p.getWorld().getName());
        config.set("Location." + p.getName() + ".OnQuit.x", p.getLocation().getX());
        config.set("Location." + p.getName() + ".OnQuit.y", p.getLocation().getY());
        config.set("Location." + p.getName() + ".OnQuit.z", p.getLocation().getZ());
        Anfang.getPlugin().saveConfig();
    }
}
