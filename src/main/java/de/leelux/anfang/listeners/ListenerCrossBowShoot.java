package de.leelux.anfang.listeners;

import de.leelux.anfang.Anfang;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.projectiles.ProjectileSource;

public class ListenerCrossBowShoot implements Listener {
    private final Anfang plugin;

    public ListenerCrossBowShoot(Anfang plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCrossbowShoot(ProjectileLaunchEvent event) {
        if (!(plugin.getConfig().getBoolean("Fun.OpCrossbowCanActivate"))) {
            if(plugin.getConfig().getBoolean("debug")) plugin.sendDebug(getClass().getName(),
                    "Config says \"Fun.OpCrossbowCanActivate: "+plugin.getConfig().getBoolean("Fun.OpCrossbowCanActivate")+"\"");
            return;
        }
        ProjectileSource shooter = event.getEntity().getShooter();
        if (!(shooter instanceof LivingEntity entity)) return;
        ItemStack item = ((LivingEntity) shooter).getEquipment().getItemInMainHand();
        if (!(item.getType().equals(Material.CROSSBOW))) return;
        CrossbowMeta item_meta = (CrossbowMeta) item.getItemMeta();
        item_meta.addChargedProjectile(new ItemStack(Material.ARROW));
        item.setItemMeta(item_meta);
        if (!(item.getItemMeta().getDisplayName().equals(plugin.getConfig().getString("Fun.OpCrossbowNameToActivate")))) {
            if(plugin.getConfig().getBoolean("debug")) plugin.sendDebug(getClass().getName(),
                    "The Name was wrong to activate the op crossbow: Is:\"" + item.getItemMeta().getDisplayName()+
                            "\" schould be:\""+plugin.getConfig().getString("Fun.OpCrossbowNameToActivate")+"\"");
            return;
        }
        if (shooter instanceof Player player) {
            if (!(player.hasPermission("anfang.crossbowOP"))) {
                if(plugin.getConfig().getBoolean("debug")) plugin.sendDebug(getClass().getName(),
                        "The player do not have the permission anfang.crossbowOP");
                return;
            }
        }
        event.getEntity().setGravity(false);
        event.getEntity().setCustomName("Mega ARROW");
        event.getEntity().setVelocity(entity.getLocation().getDirection().multiply(100));
        entity.getEquipment().setItemInMainHand(item);
    }
}