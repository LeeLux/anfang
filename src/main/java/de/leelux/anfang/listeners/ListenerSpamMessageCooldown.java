package de.leelux.anfang.listeners;

import de.leelux.anfang.Anfang;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class ListenerSpamMessageCooldown implements Listener {
    Plugin plugin = Anfang.getPlugin();
    @EventHandler
    public void onPlayerDamagePlayer(AsyncPlayerChatEvent e) {
        String message = e.getMessage();
        Player player = e.getPlayer();
        if(plugin.getConfig().getBoolean("debug")) Anfang.getPlugin().sendDebug(getClass().getName(),
                "AsyncPlayerChatEvent from player:"+player.getName()+" with message: "+message);
        List<String> playerSpamMessageCooldown = Anfang.getPlayerSpamMessageCooldown(player);
        if(!(playerSpamMessageCooldown.contains(message))){
            playerSpamMessageCooldown.add(message);
            startCooldown(player, message);
        }else{
            Anfang.getPlugin().getMessageCooldown().sendMessage(player, ChatColor.RED+"You can't write this agan for a while!");
            e.setCancelled(true);
        }
    }


    private void startCooldown(Player player, String message){
        new BukkitRunnable(){
            int SpamMessageCooldownTime = Anfang.getPlugin().getSpamMessageCooldown();
            @Override
            public void run() {
                if(plugin.getConfig().getBoolean("debug")) Anfang.getPlugin().sendDebug(getClass().getName(), SpamMessageCooldownTime+"sec to unlock: "+message);
                SpamMessageCooldownTime--;
                if(SpamMessageCooldownTime < 0){
                    Anfang.getPlayerSpamMessageCooldown(player).remove(message);
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin,20,20);
    }
}
