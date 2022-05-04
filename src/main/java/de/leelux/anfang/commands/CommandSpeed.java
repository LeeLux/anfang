package de.leelux.anfang.commands;

import de.leelux.anfang.Anfang;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandSpeed implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender.hasPermission("anfang.command.speed"))){
            sender.sendMessage(Anfang.getPlugin().getMessage("Messages.NoPermission"));
            return true;
        }
        if(!(args.length == 2)&&!(sender instanceof Player)){
            sender.sendMessage("§cUse /speed <number> <player>");
            return true;
        }
        if(!(args.length == 1 || args.length == 2)){
            sender.sendMessage("§cUse /speed <number> <player>");
            return true;
        }
        int speedlevelInt;
        try {
            speedlevelInt = Integer.parseInt(args[0]);
        } catch (NumberFormatException var7) {
            sender.sendMessage("§cUse /speed <number> <player>");
            return true;
        }
        if(speedlevelInt > 10) speedlevelInt = 10;
        if(speedlevelInt < -10) speedlevelInt = -10;
        float speedlevel = (float) speedlevelInt / 10;
        if(args.length == 1){
            Player player = (Player) sender;
            if(!(player.getGameMode() == GameMode.CREATIVE||player.getGameMode() == GameMode.SPECTATOR)){
                sender.sendMessage("§cFly speed can't be changed for players that are not flying!");
                return  true;
            }
            player.setFlySpeed(speedlevel);
            player.sendMessage("§eYou fly speed is set to: §a"+speedlevelInt);

        }
        if(args.length == 2){
            if(!(sender.hasPermission("anfang.command.speed.other"))){
                sender.sendMessage(Anfang.getPlugin().getMessage("Messages.NoPermission"));
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                    sender.sendMessage("§cThe player §e" + args[1] + "§c dosn't exist or isn't online!");
                return true;
            }
            if(!(target.getGameMode() == GameMode.CREATIVE||target.getGameMode() == GameMode.SPECTATOR)){
                sender.sendMessage("§cFly speed can't be changed for players that are not flying!");
                return  true;
            }
            target.setFlySpeed(speedlevel);
            target.sendMessage("§eYou fly speed is set to: §a"+speedlevelInt);
            sender.sendMessage("§eYou changed §a"+target.getName()+"'s §efly speed to: §a"+speedlevelInt);
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList();
        List<String> commands = new ArrayList();
        if (args.length == 1) {
            if(sender.hasPermission("anfang.command.speed")){
                commands.add("<number>");
            }
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }else if (args.length == 2){
            if(sender.hasPermission("anfang.command.speed.other")){
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
