package de.leelux.anfang.menusysytem.menus.commandPlayer;

import de.leelux.anfang.Anfang;
import de.leelux.anfang.manager.ItemBuilder;
import de.leelux.anfang.menusysytem.Menu;
import de.leelux.anfang.menusysytem.PlayerMenuUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Objects;

public class GamemodechangeMenu extends Menu {
    ItemBuilder itemBuilder = Anfang.getPlugin().getItemBuilder();
    Player target = playerMenuUtility.getTarget();
    Player player = playerMenuUtility.getOwner();
    String lastinventoryname = playerMenuUtility.getLastinventoryname();
    public GamemodechangeMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Change game mode";
    }

    @Override
    public int getSlots() {
        return 2*9;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {

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

        //set a head of the target player
        ItemStack ph = itemBuilder.newItem(Material.PLAYER_HEAD,"&r&7Player: &e" + target.getDisplayName());
        ItemMeta ph_meta = ph.getItemMeta();
        ph_meta.setLore(new UpdatePlayerheadLore().update(target, false));
        ph.setItemMeta(ph_meta);
        SkullMeta ph_skull = (SkullMeta) ph.getItemMeta();
        ph_skull.setOwner(target.getName());
        ph.setItemMeta(ph_skull);
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
