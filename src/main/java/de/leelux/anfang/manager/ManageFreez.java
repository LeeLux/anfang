package de.leelux.anfang.manager;

import de.leelux.anfang.Anfang;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ManageFreez {
    private final List<Player> frozen = new ArrayList();
    private final Plugin plugin = Anfang.getPlugin();

    public ManageFreez(Plugin plugin) {
    }

    public void Managefrozen(Plugin plugin) {
    }

    public List<Player> getFreezed() {
        return this.frozen;
    }

    public boolean isFreezed(Player player) {
        return this.frozen.contains(player);
    }

    public void setFrozen(CommandSender cs, Player target, boolean bool) {
        if (bool) {
            this.frozen.add(target);
            if(!(cs==target)) cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bYou froze " + target.getName()));
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bYou are now frozen!"));
        } else {
            this.frozen.remove(target);
            if(!(cs==target))cs.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bYou unfozed " + target.getName()));
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bYou are now free!"));
        }

    }
}