package de.leelux.anfang.manager;

import de.leelux.anfang.Anfang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class ManageModeration {
    private final Anfang plugin;
    public ManageModeration(Anfang plugin) {
        this.plugin = plugin;
    }

    public void setModeration(Player player, boolean bol){
        File file = new File(plugin.getDataFolder(),"moderation.yml");
        YamlConfiguration moderation = YamlConfiguration.loadConfiguration(file);
        moderation.addDefault("teleportBackToLocation",true);
        if(!(moderation.contains(player.getUniqueId().toString()))){
            moderation.set(player.getUniqueId()+".Name",player.getName());
        }
        moderation.set(player.getUniqueId()+".InModerationMode", bol);
        trySaveConfig(moderation,file);
        if(bol){
            moderation.set(player.getUniqueId()+".AllowFlight",player.getAllowFlight());
            moderation.set(player.getUniqueId()+".Exp",player.getExp());
            moderation.set(player.getUniqueId()+".Flying",player.isFlying());
            moderation.set(player.getUniqueId()+".FoodLevel",player.getFoodLevel());
            moderation.set(player.getUniqueId()+".GameMode",player.getGameMode().toString());
            moderation.set(player.getUniqueId()+".Gliding",player.isGliding());
            moderation.set(player.getUniqueId()+".Health",player.getHealth());
            moderation.set(player.getUniqueId()+".Inventory",player.getInventory().getContents());
            moderation.set(player.getUniqueId()+".Location",player.getLocation());

            trySaveConfig(moderation,file);
            player.getInventory().clear();
            player.setGameMode(GameMode.CREATIVE);
        }else{
            player.getInventory().clear();
            //restore old player data
            //allowflight
            player.setAllowFlight(moderation.getBoolean(player.getUniqueId()+".AllowFlight"));
            //exp
            player.setExp(moderation.getLong(player.getUniqueId()+".Exp"));
            //flying
            player.setFlying(moderation.getBoolean(player.getUniqueId()+".Flying"));
            //food
            player.setFoodLevel(moderation.getInt(player.getUniqueId()+".FoodLevel"));
            //gamemode
            String gameMode = Objects.requireNonNull(moderation.get(player.getUniqueId() + ".GameMode")).toString();
            if(gameMode.equalsIgnoreCase("adventure")|| gameMode.equalsIgnoreCase("survival")||
                    gameMode.equalsIgnoreCase("spectator")|| gameMode.equalsIgnoreCase("creative")){
                player.setGameMode(GameMode.valueOf(gameMode.toUpperCase()));
            }else{
                player.sendMessage(ChatColor.RED+"Couldn't load the GameMode from moderation config! (now default survival)");
                player.setGameMode(GameMode.SURVIVAL);
            }
            //gliding
            player.setGliding(moderation.getBoolean(player.getUniqueId()+".Gliding"));
            //health
            player.setHealth(moderation.getDouble(player.getUniqueId()+".Health"));
            //inventory
            List<ItemStack> invList = (List<ItemStack>)moderation.get(player.getUniqueId() + ".Inventory");
            ItemStack[] inv = invList.toArray(new ItemStack[0]);
            player.getInventory().setContents(inv);
            //location
            if(moderation.getBoolean("teleportBackToLocation")){
                player.teleport(Objects.requireNonNull(moderation.getLocation(player.getUniqueId() + ".Location")));
            }
        }
    }
    public boolean getModeration(Player player){
        File file = new File(plugin.getDataFolder(),"moderation.yml");
        YamlConfiguration moderation = YamlConfiguration.loadConfiguration(file);
        return moderation.getBoolean(player.getUniqueId()+".InModerationMode");
    }
    public void toggleModeration(Player player){
        setModeration(player, !getModeration(player));
    }
    private void trySaveConfig(YamlConfiguration yamlConfiguration,File file){
        try{
            yamlConfiguration.save(file);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.CONFIG,"Couldn't save: "+file.getName());
            e.printStackTrace();
        }
    }
}
