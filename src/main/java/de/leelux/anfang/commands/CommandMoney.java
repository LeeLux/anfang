package de.leelux.anfang.commands;

import de.leelux.anfang.Anfang;
import net.md_5.bungee.api.ChatColor;
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

/*
=== PLAYER COMMANDS ===
/money
/money <player>
/money pay <player> <money>
=== ADMIN COMMANDS ===
/money payfor <player> <money> <player>
D/money add <player> <money>
D/money remove <player> <money>
D/money set <player> <money>
D/money reset <player>
 */
public class CommandMoney implements CommandExecutor, TabCompleter {

    private final Anfang plugin;

    public CommandMoney(Anfang plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //checks if the database is online and if not return + message
        if (!plugin.SQL.isConnected()) {
            if (sender.isOp()) {
                //special op message
                sender.sendMessage(plugin.getMessage("Database.CommNotAvailableAdmin"));
                return true;
            }
            //normal player message
            sender.sendMessage(plugin.getMessage("Database.CommNotAvailable"));
            return true;
        }
        //if the sender is not a player return + message
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getMessage("Messages.NotaPlayer"));
            return true;
        }
        //=== ALL COMMANDS WITH JUST ONE ARGUMENTS ===
        if(args.length == 1){
            if (player.hasPermission("anfang.command.money.other") ||
                    player.hasPermission("anfang.command.money.*")) {
                if (!inDatabase(sender, args, 0)) return true;
                Player target = plugin.getServer().getPlayer(args[0]);
                //if the target has a special hidemoney permission normal player cant get the money of the target unless they have op
                if(target.hasPermission("anfang.hidemoney")&&!(player.isOp())) {
                    player.sendMessage("§cYou don't have permission to show "+target.getName()+"'s money.");
                    return true;
                }
                //sender message
                sender.sendMessage("§c"+target.getName()+"§e have: §c" + plugin.getManageMoney().getMoney(target) +
                        "§e in his bank account.");
                return true;
            }
        }
        //=== ALL COMMANDS WITH JUST TWO ARGUMENTS ===
        if (args.length == 2) {
            //=== /money reset <player>
            if (args[0].equalsIgnoreCase("reset") && (player.hasPermission("anfang.command.money.reset") ||
                    player.hasPermission("anfang.command.money.*"))) {
                if (!inDatabase(sender, args, 1)) return true;
                Player target = plugin.getServer().getPlayer(args[1]);
                plugin.getManageMoney().setMoney(target, (long) 0);
                //sender message
                sender.sendMessage("§eYou reseted §c" + target.getName() + "'s §ebank account.");
                return true;
            }
        }
        //=== ALL COMMANDS WITH JUST THREE ARGUMENTS ===
        if (args.length == 3) {
            //=== /money set <player> <money>
            if (args[0].equalsIgnoreCase("set") && (player.hasPermission("anfang.command.money.set") ||
                    player.hasPermission("anfang.command.money.*"))) {
                if (!inDatabase(sender, args, 1)) {
                    return true;
                }
                Player target = plugin.getServer().getPlayer(args[1]);
                if (!isLong(sender, args, 2)) {
                    return true;
                }
                long money = Long.parseLong(args[2]);
                plugin.getManageMoney().setMoney(target, money);
                //sender message
                sender.sendMessage("§eYou set §c" + target.getName() + "'s §ebank account to §c" + plugin.getManageMoney().getMoney(target) + "§e.");
                return true;
            }
            //=== /money add <player> <money>
            if (args[0].equalsIgnoreCase("add") && (player.hasPermission("anfang.command.money.add") ||
                    player.hasPermission("anfang.command.money.*"))) {
                if (!inDatabase(sender, args, 1)) {
                    return true;
                }
                Player target = plugin.getServer().getPlayer(args[1]);
                if (!isLong(sender, args, 2)) {
                    return true;
                }
                long money = Long.parseLong(args[2]);
                plugin.getManageMoney().addMoney(target, money);
                //sender message
                sender.sendMessage("§eYou added §c" + Money(money) + "§e to §e" + target.getName() + "'s §ebank account now: §c" +
                        plugin.getManageMoney().getMoney(target) + "§e.");
                return true;
            }
            //=== /money remove <player> <money>
            if (args[0].equalsIgnoreCase("remove") && (player.hasPermission("anfang.command.money.remove") ||
                    player.hasPermission("anfang.command.money.*"))) {
                if (!inDatabase(sender, args, 1)) {
                    return true;
                }
                Player target = plugin.getServer().getPlayer(args[1]);
                if (!isLong(sender, args, 2)) {
                    return true;
                }
                long money = Long.parseLong(args[2]);
                plugin.getManageMoney().addMoney(target, money);
                //sender message
                sender.sendMessage("§eYou removed §c" + Money(money) + "§e from §e" + target.getName() + "'s §ebank account now: §c" +
                        plugin.getManageMoney().getMoney(target) + "§e.");
                return true;
            }
            //=== /money pay <player> <money>
            if (args[0].equalsIgnoreCase("pay") && (player.hasPermission("anfang.command.money.pay") ||
                    player.hasPermission("anfang.command.money.*"))) {
                if (!inDatabase(sender, args, 1)) {
                    return true;
                }
                Player target = plugin.getServer().getPlayer(args[1]);
                if (!isLong(sender, args, 2)) {
                    return true;
                }
                long money = Long.parseLong(args[2]);
                //if money would make you balance negative return + message
                if (plugin.database.getMoney(player.getUniqueId()) - money < 0) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cYou don't have enough money to pay so much."));
                    return true;
                }
                plugin.getManageMoney().payMoney(player, money, target);
                //sender messages
                sender.sendMessage("§eYou payed §c" + target.getName() + " " + Money(money) + "§e.");
                target.sendMessage("§c"+player.getName()+"§e payed you §c"+ Money(money) + "§e.");
                return true;
            }
        }
        if(args.length == 4){
            //=== /money payfor <player> <money> <player>
            if (args[0].equalsIgnoreCase("payfor") && (player.hasPermission("anfang.command.money.payfor") ||
                    player.hasPermission("anfang.command.money.*"))) {
                //first target
                if (!inDatabase(sender, args, 1)) {
                    return true;
                }
                //second target
                Player target1 = plugin.getServer().getPlayer(args[1]);
                if (!inDatabase(sender, args, 3)) {
                    return true;
                }
                Player target2 = plugin.getServer().getPlayer(args[3]);
                //id money is a valid number
                if (!isLong(sender, args, 2)) {
                    return true;
                }
                long money = Long.parseLong(args[2]);
                //if money would make you balance negative return + message
                if (plugin.database.getMoney(target1.getUniqueId()) - money < 0) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&c"+target1.getName()+" don't have enough money to pay so much."));
                    return true;
                }
                plugin.getManageMoney().payMoney(target1, money, target2);
                //sender messages
                sender.sendMessage("§c"+target1.getName()+"§e payed §c" + target2.getName() + " " + Money(money) + "§e.");
                return true;
            }
        }
        // === /money
        if (args.length == 0 && (player.hasPermission("anfang.command.money") || player.hasPermission("anfang.command.money.*"))) {
            sender.sendMessage("§eYou have: §c" + plugin.getManageMoney().getMoney(player) + "§e in your bank account.");
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        if (args.length == 1) {
            if (sender.hasPermission("anfang.command.money.other")) {
                commands.add("<player>");
                Bukkit.getOnlinePlayers().forEach(o -> {
                    commands.add(o.getName());
                });
                /*
                //try to get all players in database
                plugin.database.getPlayers().forEach(o -> {
                    Player player = Bukkit.getPlayer(UUID.fromString(String.valueOf(o)));
                    if(player==null){
                        //if the player is not online try to get him offline
                        OfflinePlayer playerOffline = Bukkit.getServer().getOfflinePlayer(UUID.fromString(String.valueOf(o)));
                        Player po = playerOffline.getPlayer();
                        if(po==null) return;
                        if(!(po.hasPermission("anfang.hidemoney"))) commands.add(po.getName());
                        if(sender.isOp()) commands.add(po.getName());
                    }else{
                        //if the player is online = normal
                        if(!(player.hasPermission("anfang.hidemoney"))) commands.add(player.getName());
                        if(sender.isOp()) commands.add(player.getName());
                    }
                });
                */
            }
            if (sender.hasPermission("anfang.command.money.set")) commands.add("set");
            if (sender.hasPermission("anfang.command.money.add")) commands.add("add");
            if (sender.hasPermission("anfang.command.money.remove")) commands.add("remove");
            if (sender.hasPermission("anfang.command.money.reset")) commands.add("reset");
            if (sender.hasPermission("anfang.command.money.pay")) commands.add("pay");
            if (sender.hasPermission("anfang.command.money.patfor")) commands.add("payfor");
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("set") && sender.hasPermission("anfang.command.money.set")) {
                commands.add("<player>");
                Bukkit.getOnlinePlayers().forEach(o -> {
                    commands.add(o.getName());
                });
            }
            if (args[0].equalsIgnoreCase("add") && sender.hasPermission("anfang.command.money.add")) {
                commands.add("<player>");
                Bukkit.getOnlinePlayers().forEach(o -> {
                    commands.add(o.getName());
                });
            }
            if (args[0].equalsIgnoreCase("remove") && sender.hasPermission("anfang.command.money.remove")) {
                commands.add("<player>");
                Bukkit.getOnlinePlayers().forEach(o -> {
                    commands.add(o.getName());
                });
            }
            if (args[0].equalsIgnoreCase("reset") && sender.hasPermission("anfang.command.money.reset")) {
                commands.add("<player>");
                Bukkit.getOnlinePlayers().forEach(o -> {
                    commands.add(o.getName());
                });
            }
            if (args[0].equalsIgnoreCase("pay") && sender.hasPermission("anfang.command.money.pay")) {
                commands.add("<player>");
                Bukkit.getOnlinePlayers().forEach(o -> {
                    commands.add(o.getName());
                });
            }
            if (args[0].equalsIgnoreCase("payfor") && sender.hasPermission("anfang.command.money.payfor")) {
                commands.add("<player>");
                Bukkit.getOnlinePlayers().forEach(o -> {
                    commands.add(o.getName());
                });
            }
            StringUtil.copyPartialMatches(args[1], commands, completions);
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("set") && sender.hasPermission("anfang.command.money.set")) {
                commands.add("<money>");
            }
            if (args[0].equalsIgnoreCase("add") && sender.hasPermission("anfang.command.money.add")) {
                commands.add("<money>");
            }
            if (args[0].equalsIgnoreCase("remove") && sender.hasPermission("anfang.command.money.remove")) {
                commands.add("<money>");
            }
            if (args[0].equalsIgnoreCase("reset") && sender.hasPermission("anfang.command.money.reset")) {
                commands.add("<money>");
            }
            if (args[0].equalsIgnoreCase("pay") && sender.hasPermission("anfang.command.money.pay")) {
                commands.add("<money>");
            }
            if (args[0].equalsIgnoreCase("payfor") && sender.hasPermission("anfang.command.money.payfor")) {
                commands.add("<money>");
            }
            StringUtil.copyPartialMatches(args[2], commands, completions);
        }
        if(args.length == 4){
            if (args[0].equalsIgnoreCase("payfor") && sender.hasPermission("anfang.command.money.payfor")) {
                commands.add("<player>");
                Bukkit.getOnlinePlayers().forEach(o -> {
                    commands.add(o.getName());
                });
            }
            StringUtil.copyPartialMatches(args[3], commands, completions);
        }
        Collections.sort(completions);
        return completions;

        /*
                Bukkit.getOnlinePlayers().forEach(o -> {
                    commands.add(o.getName());
                });
         */

    }


    // === ===
    public boolean inDatabase(CommandSender sender, String[] args, int argsint) {
        Player player = plugin.getServer().getPlayer(args[argsint]);
        //OfflinePlayer player = Bukkit.getOfflinePlayer(args[argsint]);
        /*
        if(!(player.hasPlayedBefore())){
            sender.sendMessage("§cThe player §e" + args[argsint] + "§c dose not yet joined the server!");
            return false;
        }
        */
        if (player == null) {
            sender.sendMessage("§cThe player §e" + args[argsint] + "§c dosn't exist!");
            return false;
        }
                //Player player = plugin.getServer()getPlayer(args[argsint]);
        if (!(plugin.database.playerExists(player.getUniqueId()))) {
            sender.sendMessage("§cThe player §e" + args[argsint] + "§c isn't in the database!");
            return false;
        }
        return true;
    }

    public boolean isLong(CommandSender sender, String[] args, int argsint) {
        try {
            long money = Long.parseLong(args[argsint]);
            return true;
        } catch (NumberFormatException e) {
            sender.sendMessage("§cThe number §e" + args[argsint] + "§c is not valid!");
            return false;
        }
    }

    public String Money(Long money) {
        return (plugin.getConfig().getString("Database.prefix") + money + plugin.getConfig().getString("Database.suffix"));
    }
}


