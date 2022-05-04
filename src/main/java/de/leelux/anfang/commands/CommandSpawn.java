package de.leelux.anfang.commands;

import de.leelux.anfang.Anfang;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandSpawn implements CommandExecutor {
    public CommandSpawn() {
    }

    public boolean onCommand(CommandSender cs, Command c, String s, String[] args) {
        if (cs instanceof Player) {
            Player player = (Player)cs;
            if (player.hasPermission("anfang.spawn")) {
                if (args.length == 0) {
                    FileConfiguration config = Anfang.getPlugin().getConfig();

                    try {
                        World world = Bukkit.getWorld(config.getString("Spawn.World"));
                        double x = config.getDouble("Spawn.X");
                        double y = config.getDouble("Spawn.Y");
                        double z = config.getDouble("Spawn.Z");
                        float yaw = (float)config.getDouble("Spawn.Yaw");
                        float pitch = (float)config.getDouble("Spawn.Pitch");
                        Location location = new Location(world, x, y, z, yaw, pitch);
                        player.teleport(location);
                        player.sendMessage("§eYou are teleported to Spawn!");
                    } catch (Exception var17) {
                        cs.sendMessage("§eThere is no spawn set yet!");
                    }
                } else {
                    player.sendMessage("§cUse /spawn");
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