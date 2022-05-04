package de.leelux.anfang.commands;

import de.leelux.anfang.Anfang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandSetspawn implements CommandExecutor {
    public CommandSetspawn() {
    }

    public boolean onCommand(CommandSender cs, Command c, String s, String[] args) {
        if (cs instanceof Player) {
            Player player = (Player)cs;
            if (player.hasPermission("anfang.setspawn")) {
                if (args.length == 0) {
                    FileConfiguration config = Anfang.getPlugin().getConfig();
                    config.set("Spawn.World", player.getWorld().getName());
                    config.set("Spawn.X", player.getLocation().getX());
                    config.set("Spawn.Y", player.getLocation().getY());
                    config.set("Spawn.Z", player.getLocation().getZ());
                    config.set("Spawn.Yaw", player.getLocation().getYaw());
                    config.set("Spawn.Pitch", player.getLocation().getPitch());
                    Anfang.getPlugin().saveConfig();
                    player.sendMessage("§eYou set a new Spawn!");
                } else {
                    player.sendMessage("§cUse /setspawn");
                }
            } else {
                player.sendMessage(Anfang.getPlugin().getMessage("Messages.NoPermission"));
            }
        } else {
            cs.sendMessage(Anfang.getPlugin().getMessage("Messages.NotaPlayer"));
        }

        return false;
    }
}