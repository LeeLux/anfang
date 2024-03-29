package de.leelux.anfang.commands;

import de.leelux.anfang.Anfang;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandState implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player player)){
            Anfang.getPlugin().getMessage("Messages.NotaPlayer");
            return true;
        }
        if(!(player.hasPermission("anfang.command.state"))){
            Anfang.getPlugin().getMessage("Messages.NoPermission");
            return true;
        }
        List<String> currentstates = Anfang.getPlugin().getConfig().getStringList("States");
        if(args.length == 2){
            if(args[0].equalsIgnoreCase("#add")){
                if(!(player.hasPermission("anfang.command.state.add"))){
                    Anfang.getPlugin().getMessage("Messages.NoPermission");
                    return true;
                }
                String newstate = args[1];
                if(currentstates.contains(newstate)){
                    player.sendMessage(ChatColor.RED+"That state is already exists!");
                    return true;
                }
                currentstates.add(newstate);
                Anfang.getPlugin().getConfig().set("States", currentstates);
                Anfang.getPlugin().saveConfig();
                player.sendMessage(ChatColor.GREEN+"Added "+ChatColor.GRAY+newstate+ChatColor.GREEN+" to state list.");
                return true;
            }
            if(args[0].equalsIgnoreCase("#remove")){
                if(!(player.hasPermission("anfang.command.state.remove"))){
                    Anfang.getPlugin().getMessage("Messages.NoPermission");
                    return true;
                }
                String removestate = args[1];
                if(currentstates.contains(removestate)){
                    currentstates.remove(removestate);
                    player.sendMessage(ChatColor.RED+"Removed "+ChatColor.GRAY+removestate+ChatColor.RED+" from state list.");
                    Anfang.getPlugin().getConfig().set("States", currentstates);
                    Anfang.getPlugin().saveConfig();
                    return true;
                }

                player.sendMessage(ChatColor.RED+"There is no state called "+ChatColor.GRAY+removestate+ChatColor.RED+" to remove!");
                return true;
            }
        }

        if(!(args.length == 1)){
            player.sendMessage(getUsage());
            return true;
        }
        if(args[0].equalsIgnoreCase("#add")){
            player.sendMessage(ChatColor.RED+"Use /state #add <name>");
            return true;
        }
        if(args[0].equalsIgnoreCase("#remove")){
            player.sendMessage(ChatColor.RED+"Use /state #remove <name>");
            return true;
        }
        if(!currentstates.contains(args[0])){
            player.sendMessage(ChatColor.RED+"The state "+ChatColor.GRAY+args[0]+ChatColor.RED+" don't exist yet.");
            return true;
        }
        Anfang.getPlugin().getManagerTabList().setPlayerState(player, args[0]);
        player.sendMessage(ChatColor.GREEN+"You new state is now: "+ChatColor.GRAY+args[0]);
        return true;
    }

    private String getUsage(){
        return ChatColor.RED+"Use /state <state>|<#add|#remove <state>>";
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        if (args.length == 1) {
            if(sender.hasPermission("anfang.command.state.add")){
                commands.add("#add");
            }
            if(sender.hasPermission("anfang.command.state.remove")){
                commands.add("#remove");
            }
            if(sender.hasPermission("anfang.command.state")){
                List<String> stateList = Anfang.getPlugin().getConfig().getStringList("States");
                commands.addAll(stateList);
            }
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        if (args.length == 2) {
            if(args[0].equalsIgnoreCase("#remove")){
                if(sender.hasPermission("anfang.command.state.remove")){
                    List<String> stateList = Anfang.getPlugin().getConfig().getStringList("States");
                    commands.addAll(stateList);
                }
            }
            StringUtil.copyPartialMatches(args[1], commands, completions);
        }
        Collections.sort(completions);
        return completions;


    }
}
