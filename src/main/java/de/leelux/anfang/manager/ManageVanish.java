package de.leelux.anfang.manager;

import de.leelux.anfang.Anfang;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.entity.Player;

public class ManageVanish {
    private final List<Player> vanished = new ArrayList();
    private final Anfang plugin;

    public ManageVanish(Anfang plugin) {
        this.plugin = plugin;
    }

    public List<Player> getVanished() {
        return this.vanished;
    }

    public boolean isVanished(Player player) {
        return this.vanished.contains(player);
    }

    public void setVanished(Player player, boolean bool) {
        if (bool) {
            this.vanished.add(player);
        } else {
            this.vanished.remove(player);
        }

        Iterator var4 = plugin.getServer().getOnlinePlayers().iterator();

        while(var4.hasNext()) {
            Player onlinePlayer = (Player)var4.next();
            if (!player.equals(onlinePlayer)) {
                if (bool) {
                    onlinePlayer.hidePlayer(this.plugin, player);
                } else {
                    onlinePlayer.showPlayer(this.plugin, player);
                }
            }
        }

    }

    public void hideall(Player player) {
        this.vanished.forEach((player1) -> {
            player.hidePlayer(this.plugin, player);
        });
    }
}