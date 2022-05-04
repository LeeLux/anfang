package de.leelux.anfang.listeners;
/*
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import java.util.ArrayList;
import java.util.List;

public class ListenerDoubbleJump implements Listener {

    private final List<Player> doubleJumpdone = new ArrayList<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if(p.getLocation().subtract(0,1,0).getBlock().getType().isSolid()){
            doubleJumpdone.remove(p);
        }
        p.setAllowFlight(false);
        if (!doubleJumpdone.contains(p) && e.getTo().getY() > e.getFrom().getY() &&
                p.hasPermission("anfang.dubblejum")) p.setAllowFlight(true);
        if(p.getAllowFlight()) p.sendMessage("true");
        if(!p.getAllowFlight()) p.sendMessage("false");
    }

    @EventHandler
    public void onPlayerFly(PlayerToggleFlightEvent e) {
        Player p = e.getPlayer();
        if(doubleJumpdone.contains(p)) return;
        p.setAllowFlight(true);
        if (!(p.getGameMode() == GameMode.SURVIVAL || p.getGameMode() == GameMode.ADVENTURE)) return;
        if (!(p.hasPermission("anfang.dubblejum"))) return;
        e.setCancelled(true);
        p.setAllowFlight(false);
        doubleJumpdone.add(p);
        p.setFlying(false);
        p.setVelocity(p.getLocation().getDirection().multiply(1.5d).setY(1d));
    }


}
*/
