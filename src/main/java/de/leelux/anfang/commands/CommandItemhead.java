package de.leelux.anfang.commands;

import de.leelux.anfang.Anfang;
import de.leelux.anfang.manager.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CommandItemhead implements CommandExecutor, TabCompleter {

    private final ItemBuilder itemBuilder = Anfang.getPlugin().getItemBuilder();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender.hasPermission("anfang.command.itemhead"))){
            sender.sendMessage(Anfang.getPlugin().getMessage("Messages.NoPermission"));
            return true;
        }
        if(!(args.length == 3)&&!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED+"Use /itemhead <item> <player> <override>");
            return true;
        }
        if(!(args.length == 1 || args.length == 2 || args.length == 3)){
            sender.sendMessage(ChatColor.RED+"Use /itemhead <item> <player> <override>");
            return true;
        }
        if(args.length == 1){
            Player player = (Player) sender;
            if(!(player.getInventory().getHelmet() == null)){
                sender.sendMessage(ChatColor.RED+"You already have something on head!");
                return true;
            }
            Material m;
            if(args[0].equalsIgnoreCase("<useMainHand>")){
                try{
                    player.getInventory().setHelmet(player.getInventory().getItemInMainHand());
                    return true;
                }catch (IllegalArgumentException e) {
                    sender.sendMessage(ChatColor.RED + "Use a valid item in you hand!");
                    return true;
                }
            }else{
                try{
                    m = Material.valueOf(args[0].toUpperCase(Locale.ROOT));
                }catch (IllegalArgumentException e){
                    sender.sendMessage(ChatColor.RED+"Use a valid item at <item>!");
                    return true;
                }
            }
            player.getInventory().setHelmet(itemBuilder.newItem(m,null));
            return true;
        }
        if(args.length == 2 || args.length == 3){
            if(!(sender.hasPermission("anfang.command.itemhead.other"))){
                sender.sendMessage(Anfang.getPlugin().getMessage("Messages.NoPermission"));
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage("§cThe player §e" + args[1] + "§c dosn't exist or isn't online!");
                return true;
            }
            if(args.length == 3 && !args[2].equalsIgnoreCase("true")){
                if(!(target.getInventory().getHelmet() == null)){
                    sender.sendMessage(ChatColor.RED+target.getName()+" has already something on there head!");
                    return true;
                }
            }
            if(args.length == 2){
                if(!(target.getInventory().getHelmet() == null)) {
                    sender.sendMessage(ChatColor.RED + target.getName() + " has already something on there head!");
                    return true;
                }
            }
            Material m;
            if(args[0].equalsIgnoreCase("<useMainHand>")){
                Player player = (Player) sender;
                try{
                    target.getInventory().setHelmet(player.getInventory().getItemInMainHand());
                    return true;
                }catch (IllegalArgumentException e) {
                    sender.sendMessage(ChatColor.RED + "Use a valid item in you hand!");
                    return true;
                }
            }else{
                try{
                m = Material.valueOf(args[0].toUpperCase(Locale.ROOT));
                }catch (IllegalArgumentException e){
                    sender.sendMessage(ChatColor.RED+"Use a valid item at <item>!");
                    return true;
                }
            }
            target.getInventory().setHelmet(itemBuilder.newItem(m,null));
            return true;
        }
        sender.sendMessage(ChatColor.RED+"Use /itemhead <item> <player> <override>");
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        if (args.length == 1) {
            if(sender.hasPermission("anfang.command.itemhead")){
                commands.add("<item>");
                commands.add("<useMainHand>");
                for(Material m : Material.values()){
                    commands.add(String.valueOf(m).toLowerCase(Locale.ROOT));
                }
                StringUtil.copyPartialMatches(args[0], commands, completions);
            }
        }
        if (args.length == 2) {
            if(sender.hasPermission("anfang.command.itemhead.other")) {
                commands.add("<player>");
                Bukkit.getOnlinePlayers().forEach(o -> {
                    commands.add(o.getName());
                });
                StringUtil.copyPartialMatches(args[1], commands, completions);
            }
        }
        if(args.length == 3){
            commands.add("<override>");
            commands.add("true");
            StringUtil.copyPartialMatches(args[2], commands, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
