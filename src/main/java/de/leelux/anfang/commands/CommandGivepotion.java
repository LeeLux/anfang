package de.leelux.anfang.commands;

import de.leelux.anfang.Anfang;
import de.leelux.anfang.manager.ItemBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandGivepotion implements CommandExecutor, TabCompleter {
    private final ItemBuilder itemBuilder = Anfang.getPlugin().getItemBuilder();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //if you aren't a player return
        if(!(sender instanceof Player player)){
            sender.sendMessage(Anfang.getPlugin().getMessage("Messages.NotaPlayer"));
            return true;
        }
        //if you don't have the permission return
        if(!sender.hasPermission("anfang.commands.givepotion")){
            player.sendMessage(Anfang.getPlugin().getMessage("Messages.NoPermission"));
            return true;
        }

        player.getInventory().addItem();

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        if (args.length == 1) {

            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        if (args.length == 2) {

            StringUtil.copyPartialMatches(args[1], commands, completions);
        }
        Collections.sort(completions);
        return completions;


    }
}
