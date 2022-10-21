package de.leelux.anfang.listeners;

import de.leelux.anfang.Anfang;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class ListenerPlayerDamagePlayer implements Listener {

    @EventHandler
    public void onPlayerDamagePlayer(EntityDamageByEntityEvent e){
        if(!Anfang.getPlugin().getConfig().getBoolean("Commands.pvp.use")) return;
        if(!(e.getDamager() instanceof Player player)) return;
        if(!(e.getEntity() instanceof Player target)) return;
        if(Anfang.getPlugin().getConfig().getBoolean("Commands.pvp.ignoreWhileCreative")&&player.getGameMode() == GameMode.CREATIVE) return;
        if(!Anfang.getPlugin().getpvponList().contains(player)){
            Anfang.getPlugin().getMessageCooldown().sendMessage(player,ChatColor.YELLOW+"You pvp is currently: "+ChatColor.RED+"disabled");
            e.setCancelled(true);
            return;
        }
        if(!Anfang.getPlugin().getpvponList().contains(target)){
            Anfang.getPlugin().getMessageCooldown().sendMessage(player,ChatColor.YELLOW+target.getName()+"'s pvp is currently: "+ChatColor.RED+"disabled");
            e.setCancelled(true);
            return;
        }
    }
}
