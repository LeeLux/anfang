package de.leelux.anfang.commands;

import de.leelux.anfang.Anfang;
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

public class CommandHeal implements CommandExecutor, TabCompleter {
    public CommandHeal(Anfang plugin) {
        this.plugin = plugin;
    }

    private final Anfang plugin;

    @Override
    public boolean onCommand(CommandSender cs, Command c, String s, String[] args) {
        if (!(cs instanceof Player)) {
            if (args.length != 1) {
                cs.sendMessage("§cUse /heal <player>");
                return true;
            }
            Player player = plugin.getServer().getPlayer(args[0]);
            if (player == null) {
                cs.sendMessage("§cThe player §e" + args[0] + "§c dosn't exist or isn't online!");
                return true;
            }
            plugin.getManagerHeal().heal(cs, player);
            return true;
        }
        Player player = (Player) cs;
        if (!player.hasPermission("anfang.heal")) {
            player.sendMessage(Anfang.getPlugin().getMessage("Messages.NoPermission"));
            return true;
        }
        if (args.length == 0) {
            plugin.getManagerHeal().healitself(player);
            return true;
        }
        if (args.length != 1) {
            player.sendMessage("§cUse /heal <player>");
            return true;
        }
        if (!player.hasPermission("anfang.heal.other")) {
            player.sendMessage(Anfang.getPlugin().getMessage("Messages.NoPermission"));
            return true;
        }
        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage("§cThe player §e" + args[0] + "§c dosn't exist or isn't online!");
            return true;
        }
        plugin.getManagerHeal().heal(player, target);

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command c, String s, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        if (args.length == 1) {
            if (cs.hasPermission("anfang.heal.other")) {
                Bukkit.getOnlinePlayers().forEach(o -> {
                    commands.add(o.getName());
                });
            }
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}