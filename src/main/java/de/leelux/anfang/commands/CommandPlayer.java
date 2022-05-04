package de.leelux.anfang.commands;

import de.leelux.anfang.Anfang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandPlayer implements CommandExecutor, TabCompleter {

    public CommandPlayer() {
    }

    public boolean onCommand(CommandSender cs, Command c, String s, String[] args) {
        FileConfiguration config = Anfang.getPlugin().getConfig();
            if (args.length == 0) {
                if (!(cs instanceof Player)) {
                    cs.sendMessage(Anfang.getPlugin().getMessage("Messages.NotaPlayer"));
                    return true;
                }
                Player player = (Player) cs;
                if (!player.hasPermission("anfang.player.gui")) {
                    player.sendMessage(Anfang.getPlugin().getMessage("Messages.NoPermission"));
                }
                Anfang.getPlayerMenuUtility(player).openOnlinePlayers(player, null);
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("list")) {
                    if (cs instanceof Player) {
                        if (!cs.hasPermission("anfang.player.list")) {
                            cs.sendMessage(Anfang.getPlugin().getMessage("Messages.NoPermission"));
                            return false;
                        }
                    }
                    if(Bukkit.getOnlinePlayers().isEmpty()){
                        cs.sendMessage("§eThere are now players online!");
                        return false;
                    }
                    cs.sendMessage("§e== Online Players: ==");
                    cs.sendMessage("§e== " + Bukkit.getServer().getOnlinePlayers().size() + "/" + Bukkit.getServer().getMaxPlayers() + " ==========");
                    Bukkit.getOnlinePlayers().forEach(o -> {
                        cs.sendMessage("  §c" + o.getName());
                            });
                        cs.sendMessage("§e================== ");
                }else
                    cs.sendMessage("§cUse /player <list>|<location player>");
            }else if (args.length == 2){
                if (args[0].equalsIgnoreCase("location")) {
                    if (cs instanceof Player){
                        if (!cs.hasPermission("anfang.player.location")) {
                            cs.sendMessage(Anfang.getPlugin().getMessage("Messages.NoPermission"));
                            return true;
                        }
                    }
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        cs.sendMessage("§cThe player §e" + args[1] + "§c dosn't exist or isn't online!");
                        return true;
                    }
                    cs.sendMessage("§e=== §c" + target.getName() + "§e location ===");
                    cs.sendMessage("§eLast Joined:");
                    cs.sendMessage("§e  World: §c" + config.getString("Location." + target.getName() + ".OnJoin.world"));
                    cs.sendMessage("§e  XYZ: §c" +
                            config.getInt("Location." + target.getName() + ".OnJoin.x") + " " +
                            config.getInt("Location." + target.getName() + ".OnJoin.y")+ " " +
                            config.getInt("Location." + target.getName() + ".OnJoin.z") + " ");
                    cs.sendMessage("§eLast Quited:");
                    cs.sendMessage("§e  World: §c" + config.getInt("Location." + target.getName() + ".OnQuit.world"));
                    cs.sendMessage("§e  XYZ: §c" +
                            config.getInt("Location." + target.getName() + ".OnQuit.x") + " " +
                            config.getInt("Location." + target.getName() + ".OnQuit.y")+ " " +
                            config.getInt("Location." + target.getName() + ".OnQuit.z") + " ");
                    cs.sendMessage("§eLast Died:");
                    cs.sendMessage("§e  World: §c" + config.getInt("Location." + target.getName() + ".OnDeath.world"));
                    cs.sendMessage("§e  XYZ: §c" +
                            config.getInt("Location." + target.getName() + ".OnDeath.x") + " " +
                            config.getInt("Location." + target.getName() + ".OnDeath.y")+ " " +
                            config.getInt("Location." + target.getName() + ".OnDeath.z")+ " ");
                    cs.sendMessage("§e======================");

                }else if(args[0].equalsIgnoreCase("manage")){
                    if (cs instanceof Player){
                        if (!cs.hasPermission("anfang.player.manage")) {
                            cs.sendMessage(Anfang.getPlugin().getMessage("Messages.NoPermission"));
                            return true;
                        }
                    }
                    Player player = (Player) cs;
                    Player target = Bukkit.getPlayer(args[1]);
                    if(target==null){
                        cs.sendMessage("§cThe player §e" + args[1] + "§c dosn't exist or isn't online!");
                        return true;
                    }
                    Anfang.getPlayerMenuUtility(player).openPlayerManager(player,target, null);
                    return true;
                } else {
                    cs.sendMessage("§cUse /player <list>|<location player>|<manage>");
                }

            }else
                cs.sendMessage("§cUse /player <list>|<location player>|<manage>");

            return false;
        }




    @Override
    public List<String> onTabComplete(CommandSender cs, Command c, String s, String[] args) {
        List<String> completions = new ArrayList();
        List<String> commands = new ArrayList();
        if (args.length == 1) {
            if(cs.hasPermission("anfang.list")){
                commands.add("list");
            }
            if(cs.hasPermission("anfang.location")){
                commands.add("location");
            }
            if(cs.hasPermission("anfang.manage")){
                commands.add("manage");
            }
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }else if (args.length == 2){
            if (args[0].equalsIgnoreCase("location") && cs.hasPermission("anfang.location")){
                Bukkit.getOnlinePlayers().forEach(o -> {
                    commands.add(o.getName());
                });
            }
            if (args[0].equalsIgnoreCase("manage") && cs.hasPermission("anfang.manage")){
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