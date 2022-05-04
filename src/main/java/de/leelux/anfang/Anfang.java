package de.leelux.anfang;

import de.leelux.anfang.commands.*;
import de.leelux.anfang.listeners.*;
import de.leelux.anfang.manager.*;
import de.leelux.anfang.menusysytem.MenuListener;
import de.leelux.anfang.menusysytem.PlayerMenuUtility;
import de.leelux.anfang.sql.MySQL;
import de.leelux.anfang.sql.SQLGetter;
import de.leelux.anfang.tablist.ManagerTabList;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class Anfang extends JavaPlugin {
    private static Anfang plugin;
    private ManageVanish vanishManager;
    private ManageFreez freezManager;
    private ManagerHeal managerHeal;
    private ManageEnchantmax manageEnchantmax;
    private ManageMoney manageMoney;
    private ItemBuilder itemBuilder;
    private ManagerTabList managerTabList;
    private MessageCooldown messageCooldown;
    private ManageModeration manageModeration;

    public MySQL SQL;
    public SQLGetter database;

    public Anfang() {
    }

    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();
    private static final HashMap<Player, List<String>> playerMessageCooldown = new HashMap<>();
    private static final HashMap<Player, List<String>> playerSpamMessageCooldown = new HashMap<>();
    private static final List<Player> pvpoffList = new ArrayList<>();
    private final int basicMessageCooldown = this.getConfig().getInt("basicMessageCooldown");
    private final int spamMessageCooldown = this.getConfig().getInt("spamMessageCooldown");

    public void onLoad() {
        plugin = this;
        FileConfiguration config = this.getConfig();
        config.options().copyDefaults(true);
        this.saveDefaultConfig();
    }


    @Override
    public void onEnable() {
        plugin = this;
        //connect to database
        this.SQL = new MySQL();
        this.database = new SQLGetter(this);
        try {
            SQL.connect();
        } catch (ClassNotFoundException | SQLException e) {
            //e.printStackTrace();  not recommend
            //login info's are incorrect or not useing the database
            Bukkit.getConsoleSender().sendMessage(getPrefix()+"Could not connect to database");
        }
        //if database is connected give info about it
        if(SQL.isConnected()){
            Bukkit.getConsoleSender().sendMessage(getPrefix()+"Database is connected");
            database.createTables();
        }
        //database end
        this.managerHeal = new ManagerHeal(this);
        this.manageEnchantmax = new ManageEnchantmax(this);
        this.manageMoney = new ManageMoney(this);
        this.itemBuilder = new ItemBuilder(this);
        this.managerTabList = new ManagerTabList();
        this.messageCooldown = new MessageCooldown(this);
        this.manageModeration = new ManageModeration(this);
        this.setVanishManager(new ManageVanish(this));
        this.setFreezManager(new ManageFreez((Plugin) null));
        this.getCommand("heal").setExecutor(new CommandHeal(plugin));
        this.getCommand("kit").setExecutor(new CommandKit());
        this.getCommand("setspawn").setExecutor(new CommandSetspawn());
        this.getCommand("spawn").setExecutor(new CommandSpawn());
        this.getCommand("timer").setExecutor(new CommandTimer());
        this.getCommand("clearchat").setExecutor(new CommandClearchat());
        this.getCommand("vanish").setExecutor(new CommandVanish());
        this.getCommand("player").setExecutor(new CommandPlayer());
        this.getCommand("freez").setExecutor(new CommandFreez(plugin));
        this.getCommand("enchantmax").setExecutor(new CommandEnchantmax(plugin));
        this.getCommand("summonmax").setExecutor(new CommandSummonmax());
        this.getCommand("money").setExecutor(new CommandMoney(plugin));
        this.getCommand("motd").setExecutor(new CommandMotd());
        this.getCommand("speed").setExecutor(new CommandSpeed());
        this.getCommand("itemhead").setExecutor(new CommandItemhead());
        this.getCommand("gm").setExecutor(new CommandGm());
        this.getCommand("anfang").setExecutor(new CommandAnfang());
        this.getCommand("state").setExecutor(new CommandState());
        this.getCommand("pvp").setExecutor(new CommandPvp());
        this.getCommand("moderation").setExecutor(new CommandModeration());
        //this.getCommand("ban").setExecutor(new CommandBan());
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new ListenerJoin(plugin), this);
        pluginManager.registerEvents(new ListenerQuit(), this);
        //pluginManager.registerEvents(new GuiPlayer(plugin), this);
        pluginManager.registerEvents(new ListenerMove(plugin), this);
        pluginManager.registerEvents(new ListenerCrossBowShoot(plugin), this);
        pluginManager.registerEvents(new ListenerServerListPing(), this);
        pluginManager.registerEvents(new ListenerPlayerDamagePlayer(),this);
        pluginManager.registerEvents(new ListenerSpamMessageCooldown(), this);
        //MeunuListner
        pluginManager.registerEvents(new MenuListener(),this);
        // pluginManager.registerEvents(new ListenerDoubbleJump(),this);
        Bukkit.getConsoleSender().sendMessage(getPrefix()+"========");
        Bukkit.getConsoleSender().sendMessage(getPrefix()+"Started!");
        Bukkit.getConsoleSender().sendMessage(getPrefix()+"========");

        startupdateTabList();
    }
    //motd
    public void setmotd(String newmotd){
        getPlugin().getConfig().set("motd", newmotd);
    }

    public String getmotd(){
        return ChatColor.translateAlternateColorCodes('&',getPlugin().getConfig().getString("motd"));
    }


    public void onDisable(){
        //disconnect from database
        SQL.disconnect();
        Bukkit.getConsoleSender().sendMessage(getPrefix()+"Disconnect from database");
    }

    public static Anfang getPlugin() {
        return plugin;
    }

    public String getPrefix(){
        return getMessage("prefix")+" ";
        //return "§7[§eAnfang§7] ";
    }

    public String getMessage(String path) {
        return ChatColor.translateAlternateColorCodes('&', this.getConfig().getString(path));
    }

    public void configReload(){
        try{
            reloadConfig();
        } catch (Exception e) {
            //getLogger().log(Level.CONFIG, ChatColor.RED+"Could not reload config. ", e);
            Bukkit.broadcastMessage( ChatColor.RED+"Could not reload config. "+ e);
        }
    }

    public long getMaxMoney(){
        Long maxmoney;
        try {
            maxmoney = this.getConfig().getLong("Database.MaxMoney");
        }catch (Exception e){
            maxmoney = 922337203685477580L;
        }
        if(maxmoney > 922337203685477580L) maxmoney = 922337203685477580L;
        return maxmoney;
    }

    public void sendDebug(String surce, String string){
        Bukkit.broadcastMessage("[debug-"+surce+"] >"+ChatColor.GRAY + string);
    }

    public ManageVanish getVanishManager() {
        return this.vanishManager;
    }

    public void setVanishManager(ManageVanish vanishManager) {
        this.vanishManager = vanishManager;
    }

    public ManageFreez getFreezManager() {
        return this.freezManager;
    }

    public void setFreezManager(ManageFreez freezManager) {
        this.freezManager = freezManager;
    }

    public ManagerHeal getManagerHeal() {
        return managerHeal;
    }

    public ManageEnchantmax getManageEnchantmax() {
        return manageEnchantmax;
    }

    public ManageMoney getManageMoney() {return manageMoney;}

    public ItemBuilder getItemBuilder() {return itemBuilder;}

    public ManagerTabList getManagerTabList() {return managerTabList;}

    public ManageModeration getManageModeration() {return manageModeration;}

    //Provide a player and return a menu system for that player
    //create one if they don't already have one
    public static PlayerMenuUtility getPlayerMenuUtility(Player p) {
        PlayerMenuUtility playerMenuUtility;
        if (!(playerMenuUtilityMap.containsKey(p))) { //See if the player has a playermenuutility "saved" for them

            //This player doesn't. Make one for them add it to the hashmap
            playerMenuUtility = new PlayerMenuUtility(p);
            playerMenuUtilityMap.put(p, playerMenuUtility);

            return playerMenuUtility;
        } else {
            return playerMenuUtilityMap.get(p); //Return the object by using the provided player
        }
    }

    public static List<String> getPlayerSpamMessageCooldown(Player player){
        if(!(playerSpamMessageCooldown.containsKey(player))){
            playerSpamMessageCooldown.put(player, new ArrayList<String>());
        }
        return playerSpamMessageCooldown.get(player);
    }

    public static List<String> getPlayerMessageCooldown(Player player){
        if(!(playerMessageCooldown.containsKey(player))){
            playerMessageCooldown.put(player, new ArrayList<String>());
        }
        return playerMessageCooldown.get(player);
    }

    public List<Player> getpvponList(){
        return pvpoffList;
    }
    public int getBasicMessageCooldown() {return basicMessageCooldown;}
    public int getSpamMessageCooldown() {return spamMessageCooldown;}

    public void startupdateTabList(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

            @Override
            public void run() {
                for(Player all : Bukkit.getOnlinePlayers()) {
                    getManagerTabList().setPlayerListHeaderFooterfromConfigforall();
                }

            }
        }, 0, 20);
    }

    public MessageCooldown getMessageCooldown() {return messageCooldown;}
}
/*
== template code ===
== TabCompleter ==
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        if (args.length == 1) {

            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        if (args.length == 2) {

            StringUtil.copyPartialMatches(args[1], commands, completions);
        }
        Collections.sort(completions);
        return completions;


        }



                        Bukkit.getOnlinePlayers().forEach(o -> {
                    commands.add(o.getName());
                });
== ==
 */
