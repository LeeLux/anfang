package de.leelux.anfang.menusysytem.menus.commandPlayer;

import de.leelux.anfang.Anfang;
import de.leelux.anfang.manager.ItemBuilder;
import de.leelux.anfang.menusysytem.Menu;
import de.leelux.anfang.menusysytem.PlayerMenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.concurrent.atomic.AtomicInteger;

public class OnlinePlayers extends Menu {
    private final ItemBuilder itemBuilder = Anfang.getPlugin().getItemBuilder();
    public OnlinePlayers(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Online players";
    }

    @Override
    public int getSlots() {
        return 6*9;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        org.bukkit.entity.Player player = (org.bukkit.entity.Player) event.getWhoClicked();
            switch (event.getCurrentItem().getType()) {
                case BARRIER -> {
                    player.sendMessage(ChatColor.RED+"/player"+ChatColor.ITALIC+" gui closed!");
                    player.closeInventory();
                }
                case PLAYER_HEAD -> {
                    //get the player
                    ItemStack item = event.getCurrentItem();
                    ItemMeta itemmeta = event.getCurrentItem().getItemMeta();
                    SkullMeta skullmeta = (SkullMeta) itemmeta;
                    org.bukkit.entity.Player skullowner = skullmeta.getOwningPlayer().getPlayer();
                    if (event.isRightClick()) {
                        itemmeta.setLore(new UpdatePlayerheadLore().update(skullowner, true));
                        item.setItemMeta(itemmeta);
                    } else if (event.isLeftClick()) {
                        Anfang.getPlayerMenuUtility(player).openPlayerManager(player, skullowner, event);
                    }
                }
        }
    }

    @Override
    public void setMenuItems() {
        AtomicInteger onlineplayerscount = new AtomicInteger();
        //create item stacks
        ItemStack quit = itemBuilder.newItem(Material.BARRIER, ChatColor.RED + "Quit GUI");
        ItemStack non = itemBuilder.newItem(Material.BLACK_STAINED_GLASS_PANE," ");
        if (!(Material.getMaterial(Anfang.getPlugin().getConfig().getString("Items.GuiGlassPane")) == null)) {
            non = itemBuilder.newItem(Material.valueOf(Anfang.getPlugin().getConfig().getString("Items.GuiGlassPane"))," ");
        }
        ItemStack blank = itemBuilder.newItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE," ");

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, blank);
        }
        //set slots
        inventory.setItem(inventory.getSize() - 9, non);
        inventory.setItem(inventory.getSize() - 8, non);
        inventory.setItem(inventory.getSize() - 7, non);
        inventory.setItem(inventory.getSize() - 6, non);
        inventory.setItem(inventory.getSize() - 5, quit);
        inventory.setItem(inventory.getSize() - 4, non);
        inventory.setItem(inventory.getSize() - 3, non);
        inventory.setItem(inventory.getSize() - 2, non);
        inventory.setItem(inventory.getSize() - 1, non);
        //add skulls
        onlineplayerscount.set(0);
        Bukkit.getOnlinePlayers().forEach(o -> {
            int i = onlineplayerscount.get();
            ItemStack ph = new ItemStack(Material.PLAYER_HEAD);
            ItemMeta ph_meta = ph.getItemMeta();
            ph_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&r&7Player: &e" + o.getDisplayName()));
            ph_meta.setLore(new UpdatePlayerheadLore().update(o, true));
            ph.setItemMeta(ph_meta);
            SkullMeta ph_skull = (SkullMeta) ph.getItemMeta();
            ph_skull.setOwner(o.getName());
            ph.setItemMeta(ph_skull);
            inventory.setItem(i, ph);
            i++;
            onlineplayerscount.set(i);
        });
    }
}
