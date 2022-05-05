package de.leelux.anfang.manager;

import de.leelux.anfang.Anfang;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ManagerHeal {

    private final Anfang plugin;

    public ManagerHeal(Anfang plugin) {
        this.plugin = plugin;
    }

    public void heal(CommandSender cs, Player player){
        double maxhealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        player.setHealth(maxhealth);
        player.setFoodLevel(20);
        player.setSaturation(1);
        player.sendMessage("§eYou have been healed!");
        cs.sendMessage("§eYou healed §c" + player.getName() + "§e!");
    }

    public void healitself(Player player){
        double maxhealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        player.setHealth(maxhealth);
        player.setFoodLevel(20);
        player.setSaturation(1);
        player.sendMessage("§eYou have been healed!");
    }
}
