package de.leelux.anfang.commands;

import de.leelux.anfang.Anfang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandClearchat implements CommandExecutor {

    public boolean onCommand(CommandSender cs, Command c, String s, String[] args) {
        Player player = (Player)cs;
        if (player.hasPermission("anfang.clearchat")) {
            int i;
            if (args.length == 0) {
                for(i = 0; i <= 150; ++i) {
                    Bukkit.broadcastMessage(" ");
                }

                Bukkit.broadcastMessage("§e§lThe chat has been cleared!");
            } else if (args.length == 1) {
                int n;
                try {
                    n = Integer.parseInt(args[0]);
                } catch (NumberFormatException var9) {
                    cs.sendMessage("§cUse /clearchat <number(max.1000)>");
                    return true;
                }

                if (n > 1000) {
                    n = 1000;
                }

                for(i = 0; i <= n; ++i) {
                    Bukkit.broadcastMessage(" ");
                }

                Bukkit.broadcastMessage("§e§lThe chat has been cleared!");
            } else {
                cs.sendMessage("§cUse /clearchat <number(max.1000)>");
            }
        } else {
            cs.sendMessage(Anfang.getPlugin().getMessage("Messages.NoPermission"));
        }

        return false;
    }
}
