package de.leelux.anfang.listeners;

import de.leelux.anfang.Anfang;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class ListenerMove implements Listener {

Anfang plugin;

    public ListenerMove(Anfang plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent event) {

        if(plugin.getFreezManager().isFreezed(event.getPlayer())){
            if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getY() != event.getTo().getY() || event.getFrom().getZ() != event.getTo().getZ()) {
                Location loc = event.getFrom();
                event.getPlayer().teleport(loc.setDirection(event.getTo().getDirection()));
            }
        }else
            return;
    }
}
