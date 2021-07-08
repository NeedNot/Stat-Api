package net.neednot;

import org.bukkit.enchantments.Enchantment;

import java.util.Map;

public class JsonHelmet {

    public String name;
    public String type;
    public String damage;
    public String[] enchants;


    public void setName(String name) {
     this.name = name;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setDamage(String damage) {
        this.damage = damage;
    }
    public void setEnchants(String[] enchants) {
        this.enchants = enchants;
    }

}