package de.leelux.anfang.commands;

import de.leelux.anfang.Anfang;
import org.bukkit.Bukkit;
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

public class CommandPvp implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender.hasPermission("anfang.command.pvp"))){
            sender.sendMessage(Anfang.getPlugin().getMessage("Messages.NoPermission"));
            return true;
        }
        if(args.length == 1){
            if(!(sender instanceof Player player)){
                Anfang.getPlugin().getMessage("Messages.NotaPlayer");
                return true;
            }
            if(args[0].equalsIgnoreCase("on")){
                setPvpPlayerOn(player);
                return true;
            }
            if(args[0].equalsIgnoreCase("off")){
                setPvpPlayerOff(player);
                return true;
            }
            if(args[0].equalsIgnoreCase("info")){
                if(Anfang.getPlugin().getpvponList().contains(player)){
                    Anfang.getPlugin().getMessageCooldown().sendMessage(player,ChatColor.YELLOW+"You pvp is currently: "+ChatColor.GREEN+"enabled");
                }else{
                    Anfang.getPlugin().getMessageCooldown().sendMessage(player,ChatColor.YELLOW+"You pvp is currently: "+ChatColor.RED+"disabled");
                }
                return true;
            }
        }
        if(args.length == 2){
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage("§cThe player §e" + args[1] + "§c dosn't exist or isn't online!");
                return true;
            }
            if(args[0].equalsIgnoreCase("info")&&sender.hasPermission("anfang.command.pvp.infoother")){
                if(sender instanceof Player){
                    getPvpPlayerinfo((Player) sender, target);
                    return true;
                }
                if(Anfang.getPlugin().getpvponList().contains(target)){
                    sender.sendMessage(ChatColor.YELLOW+target.getName()+"'s pvp is currently: "+ChatColor.GREEN+"enabled");
                }else{
                    sender.sendMessage(ChatColor.YELLOW+target.getName()+"'s pvp is currently: "+ChatColor.RED+"disabled");
                }
                return true;
            }
            if(args[0].equalsIgnoreCase("on")){
                setPvpPlayerOn(target);
                sender.sendMessage(ChatColor.YELLOW+target.getName()+"'s pvp is now: "+ChatColor.GREEN+"enabled");
                return true;
            }
            if(args[0].equalsIgnoreCase("off")){
                setPvpPlayerOff(target);
                sender.sendMessage(ChatColor.YELLOW+target.getName()+"'s pvp is now: "+ChatColor.RED+"disabled");
                return true;
            }
        }
        return false;
    }

    private void setPvpPlayerOn(Player player){
        Anfang.getPlugin().getpvponList().add(player);
        Anfang.getPlugin().getMessageCooldown().sendMessage(player,ChatColor.YELLOW+"You pvp is now: "+ChatColor.GREEN+"enabled");
    }

    private void setPvpPlayerOff(Player player) {
        Anfang.getPlugin().getpvponList().remove(player);
        Anfang.getPlugin().getMessageCooldown().sendMessage(player,ChatColor.YELLOW+"You pvp is now: "+ChatColor.RED+"disabled");
    }

    private void getPvpPlayerinfo(Player player, Player target){
        if(Anfang.getPlugin().getpvponList().contains(player)){
            Anfang.getPlugin().getMessageCooldown().sendMessage(player,ChatColor.YELLOW+target.getName()+"'s pvp is currently: "+ChatColor.GREEN+"enabled");
        }else{
            Anfang.getPlugin().getMessageCooldown().sendMessage(player,ChatColor.YELLOW+target.getName()+"'s pvp is currently: "+ChatColor.RED+"disabled");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        if (args.length == 1) {
            if(sender.hasPermission("anfang.command.pvp")){
                commands.add("on");
                commands.add("off");
                commands.add("info");
            }
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        if (args.length == 2) {
            if(sender.hasPermission("anfang.command.pvp.other")){
                commands.add("<player>");
            }
            if(sender.hasPermission("anfang.command.pvp.infoother")){
                commands.add("<player>");
                Bukkit.getOnlinePlayers().forEach(o -> {
                    commands.add(o.getName());
                });
            }
            StringUtil.copyPartialMatches(args[1], commands, completions);
        }
        Collections.sort(completions);
        return completions;


    }
}
