package de.leelux.anfang.commands;

import de.leelux.anfang.Anfang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTimer implements CommandExecutor {
    private int taskID;

    public CommandTimer() {
    }

    public boolean onCommand(CommandSender cs, Command c, String s, String[] args) {
        if (cs instanceof Player) {
            Player player = (Player)cs;
            if (player.hasPermission("anfang.timer")) {
                if (args.length == 1) {
                    try {
                        int n = Integer.parseInt(args[0]);
                        this.timer(n);
                    } catch (NumberFormatException var7) {
                        cs.sendMessage("§cUse /timer <number>!");
                        return true;
                    }
                } else {
                    cs.sendMessage("§cUse /timer <number>!");
                }
            } else {
                player.sendMessage(Anfang.getPlugin().getMessage("Messages.NoPermission"));
            }
        } else {
            cs.sendMessage(Anfang.getPlugin().getMessage("Messages.NotaPlayer"));
        }

        return false;
    }

    public void timer(int countdown) {
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Anfang.getPlugin(), new Runnable() {
            int i;

            {
                this.i = countdown;
            }

            public void run() {
                if (this.i > 6000000) {
                    this.i = 6000000;
                }

                if (this.i <= 0) {
                    Bukkit.broadcastMessage("§e§lThe Timer ends!");
                    Bukkit.getScheduler().cancelTask(CommandTimer.this.taskID);
                } else {
                    Bukkit.broadcastMessage("§eThe Timer ends in §c" + this.i + "§e!");
                    --this.i;
                }
            }
        }, 0L, 20L);
    }
}
