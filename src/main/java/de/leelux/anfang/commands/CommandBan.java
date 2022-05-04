package de.leelux.anfang.commands;

import de.leelux.anfang.Anfang;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CommandBan implements CommandExecutor , TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Use /ban <player> <reason>
        if(!(sender instanceof Player)){
            if(args.length == 1){
                Player target = Bukkit.getPlayer(args[0]);
                if(target == null){
                    sender.sendMessage("§cThe player §e" + args[0] + "§c dosn't exist or isn't online!");
                    return true;
                }
                ban(sender, target ,"Console",null);
            }
                Player target = Bukkit.getPlayer(args[0]);
                if(target == null){
                    sender.sendMessage("§cThe player §e" + args[0] + "§c dosn't exist or isn't online!");
                    return true;
                }
                String reason = "";
                for (int i = 2; i < args.length; i++) {
                    reason += args[i] + " ";
                }
                ban(sender, target ,reason,null);
        }

        return false;
    }

    private void ban(CommandSender sender, Player target,String reason, String kickMessage){
        String bumper = org.apache.commons.lang.StringUtils.repeat("\n", 35);
        if(reason == null) reason = Anfang.getPlugin().getMessage("Commands.ban.defaultMessages.Reason");
        if(kickMessage == null) kickMessage = Anfang.getPlugin().getMessage("Commands.ban.defaultMessages.Kick");
        Bukkit.getBanList(BanList.Type.NAME).addBan(target.getName(),bumper+reason+bumper,null,null);
        target.kickPlayer(kickMessage);
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command c, String s, String[] args) {
        List<String> completions = new ArrayList();
        List<String> commands = new ArrayList();
        if (args.length == 1) {
            if(cs.hasPermission("anfang.command.ban")){
                commands.add("<player>");
                Bukkit.getOnlinePlayers().forEach(o -> {
                    commands.add(o.getName());
                });
            }
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }else if (args.length == 2){
            if(cs.hasPermission("anfang.command.ban")){
                commands.add("<reason>");
            }
        }
        Collections.sort(completions);
        return completions;
    }
}
