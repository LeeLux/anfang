package de.leelux.anfang.menusysytem;

import de.leelux.anfang.Anfang;
import de.leelux.anfang.menusysytem.menus.commandPlayer.ConfirmMenu;
import de.leelux.anfang.menusysytem.menus.commandPlayer.OnlinePlayers;
import de.leelux.anfang.menusysytem.menus.commandPlayer.PlayerManage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/*
Companion class to all menus. This is needed to pass information across the entire
 menu system no matter how many inventories are opened or closed.

 Each player has one of these objects, and only one.
 */

public class PlayerMenuUtility {

    private Player owner;
    private Player target;
    private Material material;
    private String lastinventoryname;

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public PlayerMenuUtility(Player player) {
        this.owner = player;
    }

    //getter and setter
    public Player getOwner() {
        return owner;
    }

    public Player getTarget(){
        return target;
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    public String getLastinventoryname() {
        return lastinventoryname;
    }

    public void setLastinventoryname(String lastinventoryname) {
        this.lastinventoryname = lastinventoryname;
    }
    //open inventorys simple
    public void openOnlinePlayers(Player player, InventoryClickEvent InventoryClickEvent){
        PlayerMenuUtility util = Anfang.getPlayerMenuUtility(player);
        util.setLastinventoryname(getStringforlastInventoryName(InventoryClickEvent));
        new OnlinePlayers(util).open();
    }

    public void openPlayerManager(Player player, Player target, InventoryClickEvent InventoryClickEvent){
        PlayerMenuUtility util = Anfang.getPlayerMenuUtility(player);
        util.setLastinventoryname(getStringforlastInventoryName(InventoryClickEvent));
        util.setTarget(target);
        new PlayerManage(util).open();
    }

    public void openConfirmMenu(Player player, Player target,Material m, InventoryClickEvent InventoryClickEvent){
        PlayerMenuUtility util = Anfang.getPlayerMenuUtility(player);
        util.setLastinventoryname(getStringforlastInventoryName(InventoryClickEvent));
        util.setTarget(target);
        util.setMaterial(m);
        new ConfirmMenu(util).open();
    }

    public void openGamemodechangeMenu(Player player, Player target, InventoryClickEvent InventoryClickEvent){
        PlayerMenuUtility util = Anfang.getPlayerMenuUtility(player);
        util.setLastinventoryname(getStringforlastInventoryName(InventoryClickEvent));
        util.setTarget(target);
        new ConfirmMenu(util).open();
    }
    //internal functions
    private String getStringforlastInventoryName(InventoryClickEvent InventoryClickEvent){
        String string = "";
        if(InventoryClickEvent != null) string = InventoryClickEvent.getView().getTitle();
        return string;
    }

}

