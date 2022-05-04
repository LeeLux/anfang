package de.leelux.anfang.commands;

import de.leelux.anfang.Anfang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;

public class CommandSummonmax implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender.hasPermission("anfang.command.speed"))){
            sender.sendMessage(Anfang.getPlugin().getMessage("Messages.NoPermission"));
            return true;
        }
        // Use /summonmax <entity> <amount>
        if(!(sender instanceof Player)){
            sender.sendMessage(Anfang.getPlugin().getMessage("Messages.NotaPlayer"));
            return true;
        }
        EntityType entitytype;
        try {
            entitytype = EntityType.valueOf(args[0].toUpperCase(Locale.ROOT));
        }catch (IllegalArgumentException e){
            sender.sendMessage(ChatColor.RED+"Use a valid entity at <entity>");
            return true;
        }
        if(!entitytype.isSpawnable()){
            sender.sendMessage(ChatColor.RED+"Use a valid entity at <entity>");
            return true;
        }
        Player player = (Player) sender;
        if(args.length == 1){
            player.getLocation().getWorld().spawnEntity(player.getLocation(),entitytype);
            return true;
        }
        if(args.length == 2){
            int amount;
            try{
                amount = Integer.parseInt(args[1]);
            }catch (NumberFormatException e){
                sender.sendMessage(ChatColor.RED+"Use a valid number at <amount>");
                return true;
            }
            if(amount < 0) amount = amount*-1;
            int maxAmont = Anfang.getPlugin().getConfig().getInt("Commands.summonmax.MaxAmount");
            if(amount> maxAmont){
                sender.sendMessage(ChatColor.RED+"The max amount is set to: "+maxAmont);
                return true;
            }
            for(int i = 0;i<amount;i++){
                player.getLocation().getWorld().spawnEntity(player.getLocation(),entitytype);
            }
            return true;
        }

        sender.sendMessage(ChatColor.RED+"Use /summonmax <entity> <amount>");
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        if(!sender.hasPermission("anfang.command.summonmax")) return completions;
        if (args.length == 1) {
            commands.add("<entity>");
            for(EntityType entityType : EntityType.values()){
                commands.add(String.valueOf(entityType).toLowerCase(Locale.ROOT));
            }
            commands.remove("player");
            commands.remove("unknown");
            commands.remove("dropped_item");
            commands.remove("falling_block");
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        if (args.length == 2) {
            commands.add("<amount>");
            StringUtil.copyPartialMatches(args[1], commands, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
