package de.leelux.anfang.manager;

import de.leelux.anfang.Anfang;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class ObiToRes {
    private final Anfang plugin;
    public ObiToRes(Anfang plugin)  {this.plugin = plugin;}

    public int getPlayerMaterialCount(Player player, Material material){
        int ObiCount = 0;
        ItemStack[] allItemsInInv = player.getInventory().getContents();
        for(int i = 0; i < allItemsInInv.length;i++){
            ItemStack item = allItemsInInv[i];
            if(item != null && item.getType() == material){
                ObiCount += item.getAmount();
            }
        }
        return ObiCount;
    }

    public void giveResForObiInInv(Player player){
        if(!plugin.getConfig().getBoolean("Fun.useObiToRes")) {return;}
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            final int minObiToGiveRes = plugin.getConfig().getInt("Fun.minObiToGiveRes");
            final int maxResLevel = plugin.getConfig().getInt("Fun.maxResLevel");
            final int ObiAmountForMaxResLevel = plugin.getConfig().getInt("Fun.ObiAmountForMaxResLevel");
            int PotionAmplifier = 0;
            PotionEffectType potionEffectType = PotionEffectType.getByName(Objects.requireNonNull(plugin.getConfig().
                    getString("Fun.PotionEffectType").toUpperCase()));

            @Override
            public void run() {
                int playerObiCount = getPlayerMaterialCount(player, Material.OBSIDIAN);
                //get the amount of obi need to get one level of resistance
                double ObiNeedForOneLevel = (double) (ObiAmountForMaxResLevel/maxResLevel);
                //get how many level you should get with you current obi count/how many obi are needed to get one levle
                PotionAmplifier = (int) (playerObiCount/ObiNeedForOneLevel);
                if(playerObiCount >= minObiToGiveRes&&PotionAmplifier>0){
                    player.addPotionEffect(new PotionEffect(potionEffectType,2, PotionAmplifier-1,false,false));
                    //debug player.sendMessage("Potion level "+PotionAmplifier+" (-1)");
                }
            }
        },20,1);
    }
}
