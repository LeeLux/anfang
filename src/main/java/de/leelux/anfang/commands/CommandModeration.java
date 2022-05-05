package de.leelux.anfang.commands;

import de.leelux.anfang.Anfang;
import de.leelux.anfang.manager.ManageModeration;
import de.leelux.anfang.manager.MessageCooldown;
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

public class CommandModeration implements CommandExecutor, TabCompleter {

    ManageModeration manageModeration = Anfang.getPlugin().getManageModeration();
    MessageCooldown mc = Anfang.getPlugin().getMessageCooldown();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender.hasPermission("anfang.command.moderation"))){
            Anfang.getPlugin().getMessage("NotaPlayer");
            return true;
        }
        //player command
        if(sender instanceof Player player){
            if(args.length == 0){
                if(manageModeration.getModeration(player)){
                    mc.sendMessage(player, ChatColor.YELLOW+"You Moderation Mode is currently: "+ChatColor.GREEN+"true");
                }else{
                    mc.sendMessage(player, ChatColor.YELLOW+"You Moderation Mode is currently: "+ChatColor.RED+"false");
                }
                return true;
            }
            if(args.length == 1){
                if(args[0].equalsIgnoreCase("true")) {
                    if(manageModeration.getModeration(player)){
                        mc.sendMessage(player,ChatColor.RED+"You are already in Moderation Mode!");
                        return true;
                    }
                    manageModeration.setModeration(player, true);
                    mc.sendMessage(player,ChatColor.YELLOW+"You set you Moderation Mode to: "+ChatColor.GREEN+"true");
                }
                if(args[0].equalsIgnoreCase("false")) {
                    if(!manageModeration.getModeration(player)){
                        mc.sendMessage(player,ChatColor.RED+"You are already in Normal Mode!");
                        return true;
                    }
                    manageModeration.setModeration(player, false);
                    mc.sendMessage(player,ChatColor.YELLOW+"You set you Moderation Mode to: "+ChatColor.RED+"false");
                }
            }
            if(args.length == 2){
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage("§cThe player §e" + args[1] + "§c doesn't exist or isn't online!");
                    return true;
                }
                if(args[0].equalsIgnoreCase("true")) {
                    if(manageModeration.getModeration(player)){
                        mc.sendMessage(target,ChatColor.RED+"You Moderation Mode is already: "+ChatColor.GREEN+"true");
                        return true;
                    }
                    manageModeration.setModeration(target, true);
                    mc.sendMessage(player,ChatColor.YELLOW+"You set "+target.getName()+"'s Moderation Mode to: "+ChatColor.GREEN+"true");
                    return true;
                }
                if(args[0].equalsIgnoreCase("false")) {
                    if(!manageModeration.getModeration(player)){
                        mc.sendMessage(target,ChatColor.RED+"You Moderation Mode is already: "+ChatColor.RED+"false");
                        return true;
                    }
                    manageModeration.setModeration(target, true);
                    mc.sendMessage(player,ChatColor.YELLOW+"You set "+target.getName()+"'s Moderation Mode to: "+ChatColor.RED+"false");
                    return true;
                }
            }
        }else{
            //non player command
            if(args.length != 2){
                sender.sendMessage(ChatColor.RED+"Use /moderator <true <player>|false <player>>");
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage("§cThe player §e" + args[1] + "§c doesn't exist or isn't online!");
                return true;
            }
            if(args[0].equalsIgnoreCase("true")) {
                if(manageModeration.getModeration(target)){
                    sender.sendMessage(target.getName()+ChatColor.RED+"'s Moderation Mode is already: "+ChatColor.GREEN+"true");
                    return true;
                }
                manageModeration.setModeration(target, true);
                sender.sendMessage(ChatColor.YELLOW+"You set "+target.getName()+"'s Moderation Mode to: "+ChatColor.GREEN+"true");
                mc.sendMessage(target,ChatColor.YELLOW+"You set you Moderation Mode to: "+ChatColor.GREEN+"true");
                return true;
            }
            if(args[0].equalsIgnoreCase("false")) {
                if(!manageModeration.getModeration(target)){
                    sender.sendMessage(target.getName()+ChatColor.RED+"'s Moderation Mode is already: "+ChatColor.RED+"false");
                    return true;
                }
                manageModeration.setModeration(target, true);
                sender.sendMessage(ChatColor.YELLOW+"You set "+target.getName()+"'s Moderation Mode to: "+ChatColor.RED+"false");
                mc.sendMessage(target,ChatColor.YELLOW+"You set you Moderation Mode to: "+ChatColor.RED+"false");
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
            if(sender.hasPermission("anfang.command.moderator")){
                commands.add("true");
                commands.add("false");
            }
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        if (args.length == 2) {
            if(sender.hasPermission("anfang.command.moderator.other")){
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
