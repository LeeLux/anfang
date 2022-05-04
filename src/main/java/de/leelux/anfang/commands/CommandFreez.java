package de.leelux.anfang.commands;

import de.leelux.anfang.Anfang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandFreez implements CommandExecutor, TabCompleter {

    Anfang plugin;

    public CommandFreez(Anfang plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command c, String s, String[] args) {
        if (args.length == 0) {
            cs.sendMessage("§cUse /freez <player> <true | false>");
            return true;
        }else if (args.length == 1 || args.length == 2){

            if (cs instanceof Player){
                if(cs.hasPermission("anfang.freez")){
                }else {
                    cs.sendMessage(Anfang.getPlugin().getMessage("Messages.NoPermission"));
                    return true;
                }
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (args.length == 2){
                if(args[1].equalsIgnoreCase("true")){
                    if(target != null) {
                    plugin.getFreezManager().setFrozen(cs, target, true);
                    }else
                    cs.sendMessage("§cThe player §e" + args[0] + "§c dosn't exist or isn't online!");
                }else if(args[1].equalsIgnoreCase("false")){
                    if(target != null) {
                    plugin.getFreezManager().setFrozen(cs, target, false);
                    }else
                    cs.sendMessage("§cThe player §e" + args[0] + "§c dosn't exist or isn't online!");
                }else
                cs.sendMessage("§cUse /freez <player> <true | false>");
                return false;
            }
            if(target != null){
                if(plugin.getFreezManager().isFreezed(target)){
                    plugin.getFreezManager().setFrozen(cs, target, false);
                }else {
                    plugin.getFreezManager().setFrozen(cs, target, true);
                }
            }else
                cs.sendMessage("§cThe player §e" + args[0] + "§c dosn't exist or isn't online!");
        }else
            cs.sendMessage("§cUse /freez <player> <true | false>");

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command c, String s, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        if(cs.hasPermission("anfang.freez")) {
            if (args.length == 1) {
                commands.add("<player>");
                Bukkit.getOnlinePlayers().forEach(o -> {
                    commands.add(o.getName());
                });
                StringUtil.copyPartialMatches(args[0], commands, completions);
            } else if (args.length == 2) {
                commands.add("true");
                commands.add("false");
                StringUtil.copyPartialMatches(args[1], commands, completions);
            }
        }
        Collections.sort(completions);
        return completions;
    }
}
