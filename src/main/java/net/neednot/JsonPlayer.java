package net.neednot;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import net.neednot.StatApi;
import net.neednot.JsonArmor;

import java.util.Date;
import java.util.UUID;

public class JsonPlayer {
    public String name;
    public String firstlogin;
    public String lastlogout;
    public boolean online;
    public double health;
    public int deaths;
    public int xplevel;
    public String world;
    public String[] coords = "None,None,None".split(",");
    public JsonArmor armor = new JsonArmor();


    public void setName(String name) {
        this.name = name;
    }
    public void setFirstlogin(String firstlogin) { ;
        this.firstlogin = firstlogin;
    }
    public void setLastlogout(String lastlogout) { ;
        this.lastlogout = lastlogout;
    }
    public void setOnline(boolean online) {
        this.online = online;
    }
    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }
    public void setHealth(double health) {
        this.health = health;
    }
    public void setArmor(JsonArmor armor) {
        this.armor = armor;
    }
    public void setXplevel(int xplevel) {
        this.xplevel = xplevel;
    }
    public void setCoords(String[] coords) {
        this.coords = coords;
    }
    public void setWorld(String world) {
        this.world = world;
    }
}