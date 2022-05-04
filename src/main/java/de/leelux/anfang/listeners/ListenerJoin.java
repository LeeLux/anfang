package de.leelux.anfang.listeners;

import de.leelux.anfang.Anfang;
import de.leelux.anfang.manager.ManageVanish;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ListenerJoin implements Listener {
    public ListenerJoin(Anfang plugin) {
        this.plugin = plugin;
    }
    private final Anfang plugin;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage("§a+ §7" + event.getPlayer().getName());
        ManageVanish manage = plugin.getVanishManager();
        manage.hideall(event.getPlayer());

        //save player join location
        FileConfiguration config = plugin.getConfig();
        Player p = event.getPlayer();
        config.set("Location." + p.getName() + ".OnJoin.world", p.getWorld().getName());
        config.set("Location." + p.getName() + ".OnJoin.x", p.getLocation().getX());
        config.set("Location." + p.getName() + ".OnJoin.y", p.getLocation().getY());
        config.set("Location." + p.getName() + ".OnJoin.z", p.getLocation().getZ());
        Anfang.getPlugin().saveConfig();

        //database
        if(plugin.SQL.isConnected()){
            plugin.database.addPlayer(event.getPlayer());
        }

        //tablist
        plugin.getManagerTabList().setPlayerListHeaderFooterfromConfig(event.getPlayer());
    }
}