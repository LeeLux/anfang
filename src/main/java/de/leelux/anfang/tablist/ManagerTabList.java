package de.leelux.anfang.tablist;

import de.leelux.anfang.Anfang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;

public class ManagerTabList {
    public void setPlayerListHeaderFooter(Player player, String header,String footer){
        if(!(Anfang.getPlugin().getConfig().getBoolean("TabList.use"))){
            return;
        }
        player.setPlayerListHeader(header);
        player.setPlayerListFooter(footer);
    }

    public void setPlayerListHeaderFooterfromConfig(Player player){
        if(!(Anfang.getPlugin().getConfig().getBoolean("TabList.use"))){
            return;
        }
        //header
        List<String> headerList = Anfang.getPlugin().getConfig().getStringList("TabList.header");
        StringBuilder header = new StringBuilder();
        for(int i = 0;i<headerList.size();i++){
            header.append(headerList.get(i));
            header.append("\n");
        }
        player.setPlayerListHeader(ChatColor.translateAlternateColorCodes('&',header.toString()));
        //footer
        List<String> footerList = Anfang.getPlugin().getConfig().getStringList("TabList.footer");
        StringBuilder footer = new StringBuilder();
        for(int i = 0;i<footerList.size();i++){
            footer.append(footerList.get(i));
            footer.append("\n");
        }
        player.setPlayerListFooter(ChatColor.translateAlternateColorCodes('&',footer.toString()));
    }

    public void setPlayerListHeaderFooterfromConfigforall(){
        Bukkit.getOnlinePlayers().forEach(this::setPlayerListHeaderFooterfromConfig);
    }

    public void setPlayerState(Player player,String state){
        Scoreboard scoreboard = player.getScoreboard();
        Team team = scoreboard.getTeam(state);
        if(team == null){
            team = scoreboard.registerNewTeam(state);
        }
        team.setSuffix(ChatColor.translateAlternateColorCodes('&'," &7&o"+state));
        team.addEntry(player.getName());
        Bukkit.broadcastMessage("Team: "+ team.getName()+" and player: "+ player.getName());
    }
}
