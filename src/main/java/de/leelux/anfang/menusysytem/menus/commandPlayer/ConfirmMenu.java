package de.leelux.anfang.menusysytem.menus.commandPlayer;

import de.leelux.anfang.Anfang;
import de.leelux.anfang.manager.ItemBuilder;
import de.leelux.anfang.menusysytem.Menu;
import de.leelux.anfang.menusysytem.PlayerMenuUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Objects;

public class ConfirmMenu extends Menu {
    ItemBuilder itemBuilder = Anfang.getPlugin().getItemBuilder();
    Player target = playerMenuUtility.getTarget();
    Player player = playerMenuUtility.getOwner();
    Material m = playerMenuUtility.getMaterial();
    String lastinventoryname = playerMenuUtility.getLastinventoryname();
    public ConfirmMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Confirm";
    }

    @Override
    public int getSlots() {
        return 2*9;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        //get the player
        ItemStack[] content = event.getClickedInventory().getContents();
        ItemStack ph = content[content.length - 1];
        ItemMeta ph_meta = null;
        if (ph.hasItemMeta()) {
            ph_meta = ph.getItemMeta();
        } else {
            player.sendMessage("No Item meta found");
        }
        if (ph_meta == null) {
            player.sendMessage("Item meta is null");
            return;
        }
        SkullMeta ph_skullmeta = (SkullMeta) ph_meta;
        Player target = ph_skullmeta.getOwningPlayer().getPlayer();
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Error by getting the target player into the confirm gui");
            return;
        }
        switch (event.getCurrentItem().getType()) {
            case BARRIER -> {
                player.sendMessage(ChatColor.RED+ "Confirm gui closed!");
                player.closeInventory();
            }
            case GOLDEN_CARROT -> Anfang.getPlayerMenuUtility(player).openPlayerManager(player,target, event);

            case PLAYER_HEAD -> {
                //get the player
                ItemStack item = event.getCurrentItem();
                ItemMeta itemmeta = event.getCurrentItem().getItemMeta();
                SkullMeta skullmeta = (SkullMeta) itemmeta;
                Player skullowner = skullmeta.getOwningPlayer().getPlayer();
                itemmeta.setLore(new UpdatePlayerheadLore().update(skullowner, false));
                item.setItemMeta(itemmeta);
            }
            case COBWEB -> {
                target.getInventory().clear();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&eYou cleared "+target.getName()+"'s Inventory"));
                player.playSound(player.getLocation(), Sound.UI_LOOM_SELECT_PATTERN,1,1);
                Anfang.getPlayerMenuUtility(player).openPlayerManager(player,target, event);
            }
            case DIAMOND_SWORD -> {
                target.setHealth(0);
                Anfang.getPlayerMenuUtility(player).openPlayerManager(player,target, event);
            }
            case TOTEM_OF_UNDYING -> {
                if (target.isInvulnerable()) {
                    target.setInvulnerable(false);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + target.getName() + " &eis no longer in god mode"));
                    Anfang.getPlayerMenuUtility(player).openPlayerManager(player,target, event);
                    return;
                }
                target.setInvulnerable(true);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + target.getName() + " &eis now in god mode"));
                Anfang.getPlayerMenuUtility(player).openPlayerManager(player,target, event);
            }
            case NETHER_STAR -> {
                if (target.isOp()) {
                    target.setOp(false);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + target.getName() + " &eis no longer a operator"));
                    Anfang.getPlayerMenuUtility(player).openPlayerManager(player,target, event);
                    return;
                }
                target.setOp(true);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + target.getName() + " &eis now a operator"));
                Anfang.getPlayerMenuUtility(player).openPlayerManager(player,target, event);
            }
            case FIRE_CHARGE -> {
                Anfang.getPlayerMenuUtility(player).openPlayerManager(player,target, event);
                target.kickPlayer("Kicked by an operator");
            }
        }
    }

    @Override
    public void setMenuItems() {
        ItemStack quit = itemBuilder.newItem(Material.BARRIER, ChatColor.RED + "Quit GUI");
        ItemStack non = itemBuilder.newItem(Material.BLACK_STAINED_GLASS_PANE," ");
        if (!(Material.getMaterial(Anfang.getPlugin().getConfig().getString("Items.GuiGlassPane")) == null)) {
            non = itemBuilder.newItem(Material.valueOf(Anfang.getPlugin().getConfig().getString("Items.GuiGlassPane"))," ");
        }
        ItemStack back;
        if(Objects.equals(lastinventoryname, "")) {
            back = itemBuilder.newItem(Material.GOLDEN_CARROT,ChatColor.YELLOW + "Back");
        }else {
            back = itemBuilder.newItem(Material.GOLDEN_CARROT,ChatColor.YELLOW + "Back to "+lastinventoryname);
        }
        ItemStack yes = itemBuilder.newItem(m,ChatColor.GREEN+"Confirm");
        //set a head of the target player
        ItemStack ph = itemBuilder.newItem(Material.PLAYER_HEAD,"&r&7Player: &e" + target.getDisplayName());
        ItemMeta ph_meta = ph.getItemMeta();
        ph_meta.setLore(new UpdatePlayerheadLore().update(target, false));
        ph.setItemMeta(ph_meta);
        SkullMeta ph_skull = (SkullMeta) ph.getItemMeta();
        ph_skull.setOwner(target.getName());
        ph.setItemMeta(ph_skull);

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, yes);
        }
        //set slot
        inventory.setItem(inventory.getSize() - 9, back);
        inventory.setItem(inventory.getSize() - 8, non);
        inventory.setItem(inventory.getSize() - 7, non);
        inventory.setItem(inventory.getSize() - 6, non);
        inventory.setItem(inventory.getSize() - 5, quit);
        inventory.setItem(inventory.getSize() - 4, non);
        inventory.setItem(inventory.getSize() - 3, non);
        inventory.setItem(inventory.getSize() - 2, non);
        inventory.setItem(inventory.getSize() - 1, ph);
    }
}
