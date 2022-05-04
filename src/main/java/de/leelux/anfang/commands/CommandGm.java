package de.leelux.anfang.commands;

import de.leelux.anfang.Anfang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;

public class CommandGm implements CommandExecutor, TabCompleter {
    List<String> gms = Arrays.asList("adventure","a","adv","2","creative","c","cre","1","survival","s","sur","0","spectator","spe","3");
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender.hasPermission("anfang.command.gm"))){
            sender.sendMessage(Anfang.getPlugin().getMessage("Messages.NoPermission"));
            return true;
        }
        // Use /gm <gamemode> <player>
        if(!(args.length == 2)&&!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED+"Use /gm <gamemode> <player>");
            return true;
        }
        if(args.length == 1&&gms.contains(args[0])){
            Player player = (Player) sender;
            GameMode gm;
            switch (args[0]) {
                case "adventure", "adv", "2", "a" -> gm = GameMode.ADVENTURE;
                case "creative", "cre", "1", "c" -> gm = GameMode.CREATIVE;
                case "survival", "sur", "0", "s" -> gm = GameMode.SURVIVAL;
                case "spectator", "spe", "3" -> gm = GameMode.SPECTATOR;
                default -> {
                    sender.sendMessage(ChatColor.RED + "Use a valid gamemode at <gamemode>!");
                    return true;
                }
            }
            if(player.getGameMode() == gm){
                return true;
            }
            player.setGameMode(gm);
            player.sendMessage("Set own game mode to "+gm.toString().toLowerCase(Locale.ROOT)+" Mode");
            return true;
        }
        if(args.length == 2&&gms.contains(args[0])){
            if(!(sender.hasPermission("anfang.command.gm.other"))){
                sender.sendMessage(Anfang.getPlugin().getMessage("Messages.NoPermission"));
                return true;
            }
            GameMode gm;
            switch (args[0]) {
                case "adventure", "adv", "2", "a" -> gm = GameMode.ADVENTURE;
                case "creative", "cre", "1", "c" -> gm = GameMode.CREATIVE;
                case "survival", "sur", "0", "s" -> gm = GameMode.SURVIVAL;
                case "spectator", "spe", "3" -> gm = GameMode.SPECTATOR;
                default -> {
                    sender.sendMessage(ChatColor.RED + "Use a valid gamemode at <gamemode>!");
                    return true;
                }
            }
            Player target = Bukkit.getPlayer(args[1]);
            if(target == null){
                sender.sendMessage("§cThe player §e" + args[1] + "§c dosn't exist or isn't online!");
                return true;
            }
            if(target.getGameMode() == gm){
                return true;
            }
            target.setGameMode(gm);
            target.sendMessage("Set own game mode to "+gm.toString().toLowerCase(Locale.ROOT)+" Mode");
            if(!(target == sender)) sender.sendMessage("Set "+target.getName()+" to "+gm.toString().toLowerCase(Locale.ROOT)+" Mode");
            return true;
        }
        sender.sendMessage(ChatColor.RED+"Use /gm <gamemode> <player>");
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        if (args.length == 1) {
            if(sender.hasPermission("anfang.command.gm")){
                commands.add("<gamemode>");
                commands.add("adv");
                commands.add("cre");
                commands.add("sur");
                commands.add("spe");
            }
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        if (args.length == 2) {
            if(sender.hasPermission("anfang.command.gm.other")) {
                commands.add("<player>");
                Bukkit.getOnlinePlayers().forEach(o -> commands.add(o.getName()));
            }
            StringUtil.copyPartialMatches(args[1], commands, completions);
        }
        Collections.sort(completions);
        return completions;

    }
}
