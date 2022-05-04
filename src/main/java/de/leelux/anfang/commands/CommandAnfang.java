package de.leelux.anfang.commands;

import de.leelux.anfang.Anfang;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
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
            sender.sendMessage(ChatColor.GREEN+"reloaded: "+ChatColor.ITALIC+"config");
            return true;
        }
        if(args[0].equalsIgnoreCase("version")) {
            if(!(sender.hasPermission("anfang.command.anfang.version"))){
                sender.sendMessage(Anfang.getPlugin().getMessage("Messages.NoPermission"));
                return true;
            }
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
            if(sender.hasPermission("anfang.command.anfang.version")){
                commands.add("version");
            }
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        if (args.length == 2) {
            if(sender.hasPermission("anfang.command.anfang.reload.config")){
                commands.add("config");
            }
            if(sender.hasPermission("anfang.command.anfang.reload.tab")){
                commands.add("tab");
            }
            StringUtil.copyPartialMatches(args[1], commands, completions);
        }
        Collections.sort(completions);
        return completions;

    }
}
