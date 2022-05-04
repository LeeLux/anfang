package de.leelux.anfang.commands;

import de.leelux.anfang.Anfang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandMotd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0){
                sender.sendMessage("§fMotd: §r\n" + Anfang.getPlugin().getmotd());
                return true;
        }
        if(!(args.length == 0)){
                Anfang.getPlugin().setmotd(String.join(" ", args));
                sender.sendMessage("§fChanged Motd: §r\n" + Anfang.getPlugin().getmotd());
                return true;
        }
        return false;
    }
}
