package de.leelux.anfang.commands;

import de.leelux.anfang.Anfang;
import de.leelux.anfang.manager.ManageVanish;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CommandVanish implements CommandExecutor {
    public CommandVanish() {
    }

    public boolean onCommand(CommandSender cs, Command c, String s, String[] args) {
        ManageVanish manage = Anfang.getPlugin().getVanishManager();
        Player player;
        if (cs instanceof Player) {
            player = (Player)cs;
            if (player.hasPermission("anfang.vanish")) {
                if (args.length == 0) {
                    if (manage.isVanished(player)) {
                        manage.setVanished(player, false);
                        player.sendMessage(Anfang.getPlugin().getMessage("Messages.Visible"));
                        player.removePotionEffect(PotionEffectType.INVISIBILITY);
                    } else {
                        manage.setVanished(player, true);
                        player.sendMessage(Anfang.getPlugin().getMessage("Messages.Vanished"));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 60000, 250, false));
                    }
                } else if (player.hasPermission("anfang.vanish.other")) {
                    if (args.length == 1) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null) {
                            if (manage.isVanished(target)) {
                                manage.setVanished(target, false);
                                cs.sendMessage("§eYou have made §c" + target.getName() + "§e visible!");
                                target.sendMessage(Anfang.getPlugin().getMessage("Messages.Visible"));
                                target.removePotionEffect(PotionEffectType.INVISIBILITY);
                            } else {
                                manage.setVanished(target, true);
                                cs.sendMessage("§eYou have made §c" + target.getName() + "§e vanish!");
                                target.sendMessage(Anfang.getPlugin().getMessage("Messages.Vanished"));
                                target.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 60000, 250, false));
                            }
                        } else {
                            cs.sendMessage("§cThe player §e" + args[0] + "§c dosn't exist or isn't online!");
                        }
                    } else {
                        cs.sendMessage("§cUse /vanish <player>");
                    }
                } else {
                    player.sendMessage(Anfang.getPlugin().getMessage("Messages.NoPermission"));
                }
            } else {
                player.sendMessage(Anfang.getPlugin().getMessage("Messages.NoPermission"));
            }
        } else if (args.length == 1) {
            player = Bukkit.getPlayer(args[0]);
            if (player != null) {
                if (manage.isVanished(player)) {
                    manage.setVanished(player, false);
                    cs.sendMessage("§eYou have made §c" + player.getName() + "§e visible!");
                    player.sendMessage(Anfang.getPlugin().getMessage("Messages.Visible"));
                    player.removePotionEffect(PotionEffectType.INVISIBILITY);
                } else {
                    manage.setVanished(player, true);
                    cs.sendMessage("§eYou have made §c" + player.getName() + "§e vanish!");
                    player.sendMessage(Anfang.getPlugin().getMessage("Messages.Vanished"));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 60000, 250, false));
                }
            } else {
                cs.sendMessage("§cThe player §e" + args[0] + "§c dosn't exist or isn't online!");
            }
        } else {
            cs.sendMessage("§cUse /vanish <player>");
        }

        return false;
    }
}

