package de.leelux.anfang.manager;

import de.leelux.anfang.Anfang;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class ManageMoney {

    private final Anfang plugin;

    public ManageMoney(Anfang plugin) {
        this.plugin = plugin;
    }

    public String getMoney(Player player) {
        return (plugin.getConfig().getString("Database.prefix") + plugin.database.getMoney(player.getUniqueId()) +
                plugin.getConfig().getString("Database.suffix"));
    }

    public long getMoneyNumber(Player player) {
        return plugin.database.getMoney(player.getUniqueId());
    }

    public void setMoney(Player player, Long money) {
        if(money > plugin.getMaxMoney()) money = plugin.getMaxMoney();
        plugin.database.setMoney(player.getUniqueId(), money);
    }

    public void addMoney(Player player, Long money) {
        if(getMoneyNumber(player) + money > plugin.getMaxMoney()) {
            setMoney(player, plugin.getMaxMoney());
            return;
        }
        plugin.database.addMoney(player.getUniqueId(), money);
    }

    public void removeMoney(Player player, Long money) {
        plugin.database.addMoney(player.getUniqueId(), (money * -1));
    }

    public void payMoney(Player player, Long money, Player target) {
        //remove player money
        addMoney(player, money * -1);
        //add target money
        addMoney(target, money);
    }

}
