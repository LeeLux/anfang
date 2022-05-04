package de.leelux.anfang.manager;

import de.leelux.anfang.Anfang;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class MessageCooldown {

    private final Plugin plugin = Anfang.getPlugin();

    public MessageCooldown(Plugin plugin) {
    }

    public void sendMessage(Player player, String message){
        List<String> playerMessageCooldown = Anfang.getPlayerMessageCooldown(player);
        if(!(playerMessageCooldown.contains(message))){
            playerMessageCooldown.add(message);
            startCooldown(player, message,Anfang.getPlugin().getBasicMessageCooldown());
            player.sendMessage(message);
        }
    }

    public void sendMessage(Player player, String message, int seconds){
        List<String> playerMessageCooldown = Anfang.getPlayerMessageCooldown(player);
        if(!(playerMessageCooldown.contains(message))){
            playerMessageCooldown.add(message);
            startCooldown(player, message, seconds);
            player.sendMessage(message);
        }
    }

    private void startCooldown(Player player, String message, int seconds){
        new BukkitRunnable(){
            int MessageCooldownTime = seconds;
            @Override
            public void run() {
                MessageCooldownTime--;
                if(MessageCooldownTime < 0){
                    Anfang.getPlayerMessageCooldown(player).remove(message);
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin,20,20);
    }
}
