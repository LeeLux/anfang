package de.leelux.anfang.listeners;

import de.leelux.anfang.Anfang;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ListenerServerListPing implements Listener {

    @EventHandler
    public void onServerListPing(ServerListPingEvent event)
    {
        event.setMotd(Anfang.getPlugin().getConfig().getString("motd").replaceAll("&","§"));
    }
}
