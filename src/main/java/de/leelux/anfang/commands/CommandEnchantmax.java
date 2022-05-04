package de.leelux.anfang.commands;

import de.leelux.anfang.Anfang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandEnchantmax implements CommandExecutor, TabCompleter {
    private final Anfang plugin;

    public CommandEnchantmax(Anfang plugin) {
        this.plugin = plugin;
    }

    //get all default enchantments and put them into a List<Enchantment>
    public final List<Enchantment> availableEnchantments = Arrays.asList(org.bukkit.enchantments.Enchantment.values());

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //checks if the sender is a player and if so, if the player has the anfang.command.enchantmax permission
        //otherwise send the player a No Permission message and return
        if (sender instanceof Player player && !(player.hasPermission("anfang.command.enchantmax"))) {
            plugin.getMessage("Message.Nopermission");
            return true;
        }
        //if the command do not have the right arguments send the sender a usage message
        if (!(args.length == 1 || args.length == 2 || args.length == 3)) {
            sender.sendMessage(ChatColor.RED + "Use /enchatmax <Enchantment> <Level> <Player>");
            return true;
        }
        //if the sender is not a player, the sender must give a player (3 argument) in the command or the sender will get a usage message
        //and make sender to player
        if (!(sender instanceof Player player) && !(args.length == 3)) {
            sender.sendMessage(ChatColor.RED + "Use /enchatmax <Enchantment> <Level> <Player>");
            return true;
        }
        //make the player. Becuase in the if above we check if the sender isn't a player and if so return. So sender must be a player.
        Player player = (Player) sender;
        //get the wanted enchantment
        Enchantment commandEnchantment = null;
        for (Enchantment enchantment : availableEnchantments) {
            if (enchantment.getKey().toString().equals(args[0])) {
                commandEnchantment = enchantment;
            } else if (enchantment.getKey().toString().replace("minecraft:", "").equals(args[0])) {
                commandEnchantment = enchantment;
            }
        }
        //send you an error message if the enchantment in you command could not match to any minecraft enchantments
        if (commandEnchantment == null) {
            sender.sendMessage(ChatColor.RED + "The enchatment: " + args[0] + " is not valid");
            return true;
        }
        // /enchantmax sharpness
        if (args.length == 1) {
            plugin.getManageEnchantmax().setHanditemtoEnchantmax(sender, commandEnchantment, 1, player);
        }
        // /enchantmax sharpness 5
        if (args.length == 2) {
            plugin.getManageEnchantmax().setHanditemtoEnchantmax(sender, commandEnchantment, command2isint(sender, args), player);
        }
        // /enchantmax sharpness 5 Notch
        if (args.length == 3) {
            Player target = plugin.getServer().getPlayer(args[2]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "The player §e" + args[2] + "§c dosn't exist or isn't online!");
                return true;
            }
            plugin.getManageEnchantmax().setHanditemtoEnchantmax(sender, commandEnchantment, command2isint(sender, args), target);
        }
        return false;
    }

    private int command2isint(CommandSender sender, String[] args) {
        int level = 1;
        try {
            level = Integer.parseInt(args[1]);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "The 2 argument must be a Integer (1,2,5,10...)");
            sender.sendMessage(ChatColor.RED + "Therefor set default level to 1");
        }
        return level;
    }


    @Override
    public List<String> onTabComplete(CommandSender cs, Command c, String s, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        if (args.length == 1) {
            commands.add("<enchantment>");
            //check if the ench can be applied to item
            //test: commands.add("minecraft:quick_charge");
            for (Enchantment enchantment : availableEnchantments) {
                commands.add(enchantment.getKey().toString().replace("minecraft:", ""));
            }
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        if (args.length == 2) {
            commands.add("<amount>");
            //gets the enchantment you put into the command and gives you the StartLevel and MaxLevel otherwise the default value 1
            Enchantment commandEnchantment = null;
            for (Enchantment enchantment : availableEnchantments) {
                if (enchantment.getKey().toString().equals(args[0])) {
                    commandEnchantment = enchantment;
                } else if (enchantment.getKey().toString().replace("minecraft:", "").equals(args[0])) {
                    commandEnchantment = enchantment;
                }
            }
            //adds to the tab completer
            if (commandEnchantment == null) {
                return completions;
            }
            ;
            commands.add("" + commandEnchantment.getMaxLevel());
            StringUtil.copyPartialMatches(args[1], commands, completions);
        }
        if (args.length == 3) {
            //also maby check only for player were you can apply the enachnt
            if (cs.hasPermission("anfang.command.enchantmax.other")) {
                commands.add("<player>");
                Bukkit.getOnlinePlayers().forEach(o -> {
                    commands.add(o.getName());
                });
            }
            StringUtil.copyPartialMatches(args[2], commands, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
