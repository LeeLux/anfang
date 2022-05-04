package de.leelux.anfang.sql;

import de.leelux.anfang.Anfang;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SQLGetter {

    private final Anfang plugin;

    public SQLGetter(Anfang plugin) {
        this.plugin = plugin;
    }

    public void createTables(){
        PreparedStatement ps;
        try{
            ps = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS anfang" +
                    " (name VARCHAR(16) NOT NULL,uuid VARCHAR(36) NOT NULL,money DECIMAL(20,2) NOT NULL DEFAULT 0.00,PRIMARY KEY (name))");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addPlayer(Player player){
        try{
            UUID uuid = player.getUniqueId();
            if(!playerExists(uuid)){
                PreparedStatement psaddPlayer = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO anfang"
                + " (name,uuid) VALUES (?,?)");
                psaddPlayer.setString(1, player.getName());
                psaddPlayer.setString(2, player.getUniqueId().toString());
                psaddPlayer.executeUpdate();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean playerExists(UUID uuid){
        try{
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT * FROM anfang WHERE uuid=?");
            ps.setString(1, uuid.toString());
            ResultSet result = ps.executeQuery();
            return result.next();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public List<UUID> getPlayers(){
        List<UUID> uuids = new ArrayList<>();
        try{
            PreparedStatement ps =plugin.SQL.getConnection().prepareStatement("SELECT uuid FROM anfang");
            ResultSet psq = null;
            psq = ps.executeQuery();
            while (psq.next())
            {
                //UUIDS = psq.getString(1);
                uuids.add(UUID.fromString(psq.getString(1)));
            }
            psq.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return uuids;
    }

    //set money
    public void setMoney(UUID uuid , Long money){
        try{
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE anfang SET money=? WHERE uuid=?");
            ps.setDouble(1, money);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    //add money
    public void addMoney(UUID uuid , Long money){
        try{
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE anfang SET money=? WHERE uuid=?");
            ps.setDouble(1, (getMoney(uuid)+money));
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    //get money
    public long getMoney(UUID uuid ){
        try{
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT money FROM anfang WHERE uuid=?");
            ps.setString(1, uuid.toString());
            ResultSet result = ps.executeQuery();
            long money = 0;
            if(result.next()){
                money = result.getLong("money");
                return money;
            }
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    // DELETE SOMETHING FROM THE DATABASE

    public void emptyTable(){
        try{
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("TRUNCATE anfang");
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void removePlayer(UUID uuid){
        try{
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("DELETE FROM anfang WHERE uuid=?");
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
