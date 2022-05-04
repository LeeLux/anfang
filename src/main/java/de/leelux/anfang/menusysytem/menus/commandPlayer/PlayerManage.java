package de.leelux.anfang.menusysytem.menus.commandPlayer;

import de.leelux.anfang.Anfang;
import de.leelux.anfang.manager.ItemBuilder;
import de.leelux.anfang.manager.ManageFreez;
import de.leelux.anfang.manager.ManageVanish;
import de.leelux.anfang.menusysytem.Menu;
import de.leelux.anfang.menusysytem.PlayerMenuUtility;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerManage extends Menu {
    ItemBuilder itemBuilder = Anfang.getPlugin().getItemBuilder();
    Player target = playerMenuUtility.getTarget();
    Player player = playerMenuUtility.getOwner();
    String lastinventoryname = playerMenuUtility.getLastinventoryname();
    PlayerMenuUtility targetplayerMenuUtility = Anfang.getPlayerMenuUtility(target);
    PlayerMenuUtility playerplayerMenuUtility = Anfang.getPlayerMenuUtility(player);
    public PlayerManage(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Manage "+target.getName();
    }

    @Override
    public int getSlots() {
        return 4*9;
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
            player.sendMessage(ChatColor.RED + "Error by getting the target player into the plater manager gui");
            return;
        }
        switch (event.getCurrentItem().getType()) {
            case BARRIER -> {
                player.sendMessage(ChatColor.RED+"/player manage"+ChatColor.ITALIC+" gui closed!");
                player.closeInventory();
            }
            case GOLDEN_CARROT -> {
                Anfang.getPlayerMenuUtility(player).openOnlinePlayers(player,event);
            }

            case PLAYER_HEAD -> {
                //get the player
                ItemStack item = event.getCurrentItem();
                ItemMeta itemmeta = event.getCurrentItem().getItemMeta();
                SkullMeta skullmeta = (SkullMeta) itemmeta;
                Player skullowner = skullmeta.getOwningPlayer().getPlayer();
                itemmeta.setLore(new UpdatePlayerheadLore().update(skullowner, false));
                item.setItemMeta(itemmeta);
            }
            case ENDER_PEARL -> {
                player.teleport(target);
                player.closeInventory();
            }
            case ENDER_EYE -> {
                target.teleport(player);
            }
            case COBWEB -> {
                Anfang.getPlayerMenuUtility(player).openConfirmMenu(player, target,Material.COBWEB,event);
            }
            case DIAMOND_SWORD -> {
                Anfang.getPlayerMenuUtility(player).openConfirmMenu(player, target,Material.DIAMOND_SWORD,event);
            }
            case BEDROCK -> {
                Anfang.getPlayerMenuUtility(player).openGamemodechangeMenu(player, target,event);
            }
            case PACKED_ICE -> {
                ManageFreez freezmanage = Anfang.getPlugin().getFreezManager();
                if (freezmanage.isFreezed(target)) {
                    freezmanage.setFrozen(player, target, false);
                } else
                    freezmanage.setFrozen(player, target, true);
            }
            case SPLASH_POTION -> {
                Anfang.getPlugin().getManagerHeal().healitself(target);
            }
            case POTION -> {
                ManageVanish manage = Anfang.getPlugin().getVanishManager();
                if (manage.isVanished(target)) {
                    manage.setVanished(target, false);
                    if(!(target==player)) player.sendMessage("§eYou have made §c" + target.getName() + "§e visible!");
                    target.sendMessage(Anfang.getPlugin().getMessage("Messages.Visible"));
                    target.removePotionEffect(PotionEffectType.INVISIBILITY);
                } else {
                    manage.setVanished(target, true);
                    if(!(target==player)) player.sendMessage("§eYou have made §c" + target.getName() + "§e vanish!");
                    target.sendMessage(Anfang.getPlugin().getMessage("Messages.Vanished"));
                    target.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 60000, 250, false));
                }
            }
            case TOTEM_OF_UNDYING -> {
                Anfang.getPlayerMenuUtility(player).openConfirmMenu(player, target,Material.TOTEM_OF_UNDYING,event);
            }
            case NETHER_STAR -> {
                Anfang.getPlayerMenuUtility(player).openConfirmMenu(player, target,Material.NETHER_STAR,event);
            }
            case FIRE_CHARGE -> {
                Anfang.getPlayerMenuUtility(player).openConfirmMenu(player, target,Material.FIRE_CHARGE,event);
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

        ItemStack enderperl = itemBuilder.newItem(Material.ENDER_PEARL,"&7Tp to &e" + target.getName());
        ItemStack endereye = itemBuilder.newItem(Material.ENDER_EYE,"&7Tp &e" + target.getName() + " &7to you");
        ItemStack cobweb = itemBuilder.newItem(Material.COBWEB,"&7Clear &e" + target.getName() + "'s &7Inventory");
        ItemStack kill = itemBuilder.newItem(Material.DIAMOND_SWORD,"&7Kill &e" + target.getName());//tnt
        ItemStack bedrock = itemBuilder.newItem(Material.BEDROCK,"&7Change &e" + target.getName() + "'s &7gamemode");
        ItemStack ice = itemBuilder.newItem(Material.PACKED_ICE,"&7Freez &e" + target.getName());
        ItemStack heal = itemBuilder.newPotion(Material.SPLASH_POTION,1,"&7Heal &e" + target.getName()
                ,null, Color.RED);//tipped arrow of healing
        ItemStack vanish = itemBuilder.newPotion(Material.POTION,1,"&7Vanish &e" + target.getName(),
                null, Color.WHITE);//tipped arrow of invisible
        ItemStack god = itemBuilder.newItem(Material.TOTEM_OF_UNDYING,"&7Set &e" + target.getName() + " &7GOD");
        ItemStack op = itemBuilder.newItem(Material.NETHER_STAR,"&7Set &e" + target.getName() + " &7OP");
        ItemStack kick = itemBuilder.newItem(Material.FIRE_CHARGE,"&7Kick &e" + target.getName());

        //set a head of the target player
        ItemStack ph = itemBuilder.newItem(Material.PLAYER_HEAD,"&r&7Player: &e" + target.getName());
        ItemMeta ph_meta = ph.getItemMeta();
        ph_meta.setLore(new UpdatePlayerheadLore().update(target, false));
        ph.setItemMeta(ph_meta);
        SkullMeta ph_skull = (SkullMeta) ph.getItemMeta();
        ph_skull.setOwner(playerMenuUtility.getTarget().getName());
        ph.setItemMeta(ph_skull);

        //set option slots
        //{enderperl,endereye,cobweb,kill,bedrock,ice,heal,vanish,god,op}
        List<ItemStack> optionItems = new ArrayList();
        if (player.hasPermission("anfang.player.gui.tp")) {
            optionItems.add(enderperl);
        }
        if (player.hasPermission("anfang.player.gui.tptome")) {
            optionItems.add(endereye);
        }
        if (player.hasPermission("anfang.player.gui.clear")) {
            optionItems.add(cobweb);
        }
        if (player.hasPermission("anfang.player.gui.kill")) {
            optionItems.add(kill);
        }
        if (player.hasPermission("anfang.player.gui.gm")) {
            optionItems.add(bedrock);
        }
        if (player.hasPermission("anfang.player.gui.freez")) {
            optionItems.add(ice);
        }
        if (player.hasPermission("anfang.player.gui.heal")) {
            optionItems.add(heal);
        }
        if (player.hasPermission("anfang.player.gui.vanish")) {
            optionItems.add(vanish);
        }
        if (player.hasPermission("anfang.player.gui.god")) {
            optionItems.add(god);
        }
        if (player.hasPermission("anfang.player.gui.op")) {
            optionItems.add(op);
        }
        if (player.hasPermission("anfang.player.gui.kick")) {
            optionItems.add(kick);
        }
        ItemStack[] newoptionItems = optionItems.toArray(new ItemStack[0]);
        inventory.setContents(newoptionItems);

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
