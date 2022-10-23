package de.leelux.anfang.commands;

import de.leelux.anfang.Anfang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandAnfang implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender.hasPermission("anfang.command.anfang"))){
            sender.sendMessage(Anfang.getPlugin().getMessage("Messages.NoPermission"));
            return true;
        }
        if(args[0].equalsIgnoreCase("reload")){
            if(!(sender.hasPermission("anfang.command.anfang.reload"))){
                sender.sendMessage(Anfang.getPlugin().getMessage("Messages.NoPermission"));
                return true;
            }
            Anfang.getPlugin().configReload();
            Bukkit.getOnlinePlayers().forEach(player -> {
                Anfang.getPlugin().getItemEffects().givePlayerEffect(player);
            });
            Anfang.getPlugin().startupdateTabList();
            sender.sendMessage(Anfang.getPlugin().getPrefix()+ChatColor.ITALIC+ChatColor.GREEN+"config reloaded!");
            return true;
        }
        if(args[0].equalsIgnoreCase("info")) {
            if(!(sender.hasPermission("anfang.command.anfang.version"))){
                sender.sendMessage(Anfang.getPlugin().getMessage("Messages.NoPermission"));
                return true;
            }
            PluginDescriptionFile pluDes = Anfang.getPlugin().getPluginDescriptionFile();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&l&7[&r&eName:&r&l&7] "+ pluDes.getName()+
                    "\n&l&7[&r&eFull name:&r&l&7] "+pluDes.getFullName()+
                    "\n&l&7[&r&eDescription:&r&l&7] "+pluDes.getDescription()+
                    "\n&l&7[&r&ePrefix:&r&l&7] "+pluDes.getPrefix()+
                    "\n&l&7[&r&eVersion:&r&l&7] "+pluDes.getVersion()+
                    "\n&l&7[&r&eAPIVersion:&r&l&7] "+pluDes.getAPIVersion()));
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        if (args.length == 1) {
            if(sender.hasPermission("anfang.command.anfang.reload")){
                commands.add("reload");
            }
            if(sender.hasPermission("anfang.command.anfang.info")){
                commands.add("info");
            }
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        if (args.length == 2) {
            StringUtil.copyPartialMatches(args[1], commands, completions);
        }
        Collections.sort(completions);
        return completions;

    }
}
