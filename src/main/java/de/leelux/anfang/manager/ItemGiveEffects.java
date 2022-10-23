package de.leelux.anfang.manager;

import de.leelux.anfang.Anfang;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ItemGiveEffects {
    private int taskID;

    private final Anfang plugin;
    public ItemGiveEffects(Anfang plugin)  {this.plugin = plugin;}

    public int getPlayerMaterialCount(Player player, Material material){
        int amount = 0;
        ItemStack[] allItemsInInv = player.getInventory().getContents();
        for(int i = 0; i < allItemsInInv.length;i++){
            ItemStack item = allItemsInInv[i];
            if(item != null && item.getType() == material){
                //item.getItemMeta().getCustomModelData()
                if(plugin.getConfig().getBoolean("Fun.Item.UseCustomModelDates")){
                    for(int id : plugin.getConfig().getIntegerList("Fun.Item.CustomModelDates")){
                        if(item.getItemMeta().hasCustomModelData() && item.getItemMeta().getCustomModelData() == id){
                            amount += item.getAmount();
                        }
                    }
                }else{
                    amount += item.getAmount();
                }
            }
        }
        return amount;
    }

    public int getIntForAllItemToGetTheEffects(Player player){
        List<String> allMaterials = plugin.getConfig().getStringList("Fun.Item.Materials");
        int totalamount = 0;
        for(int i = 0; i < allMaterials.size();i++){
            Material material = Material.valueOf(allMaterials.get(i).toUpperCase(Locale.ROOT));
            totalamount += getPlayerMaterialCount(player, material);
        }
        return totalamount;
    }

    public void givePlayerEffect(Player player){
        if(!plugin.getConfig().getBoolean("Fun.useItemGiveEffects")) {return;}
        Bukkit.getScheduler().cancelTask(ItemGiveEffects.this.taskID);
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            final int minItemAmountToGetEffect = plugin.getConfig().getInt("Fun.minItemAmountToGetEffect");
            final int maxEffectLevel = plugin.getConfig().getInt("Fun.maxEffectLevel");
            final int ItemAmountForMaxEffectLevel = plugin.getConfig().getInt("Fun.ItemAmountForMaxEffectLevel");
            int PotionAmplifier = 0;
            final PotionEffectType potionEffectType = PotionEffectType.getByName(Objects.requireNonNull(plugin.getConfig().
                    getString("Fun.PotionEffectType").toUpperCase().replace("minecraft:","")));

            @Override
            public void run() {
                int playerItemCount = getIntForAllItemToGetTheEffects(player);
                //get the amount of the item needed to get one level of the effect
                double ItemAmountNeedForOneLevel = (double) (ItemAmountForMaxEffectLevel/maxEffectLevel);
                //get how many level you should get with you current item count/how many items are needed to get one level
                PotionAmplifier = (int) (playerItemCount/ItemAmountNeedForOneLevel);
                if(playerItemCount >= minItemAmountToGetEffect&&PotionAmplifier>0){
                    if(potionEffectType == null){
                        Bukkit.getScheduler().cancelTask(ItemGiveEffects.this.taskID);
                        return;
                    }
                    player.addPotionEffect(new PotionEffect(potionEffectType,2, PotionAmplifier-1,false,false));
                    //debug player.sendMessage("Potion level "+PotionAmplifier+" (-1)");
                }
            }
        },20,1);
    }
}
