package net.neednot;

import de.tr7zw.nbtapi.NBTListCompound;
import net.neednot.JsonHelmet;
import net.neednot.JsonChestplate;
import net.neednot.JsonLeggings;
import net.neednot.JsonBoots;

public class JsonArmor {

    public JsonHelmet helmet;
    public JsonChestplate chestplate;
    public JsonLeggings leggings;
    public JsonBoots boots;

    public void setHelmet(JsonHelmet helmet) {
        this.helmet = helmet;
    }
    public void setChestplate(JsonChestplate chestplate) {
        this.chestplate = chestplate;
    }
    public void setLeggings(JsonLeggings leggings) {
        this.leggings = leggings;
    }
    public void setBoots(JsonBoots boots) {
        this.boots = boots;
    }
}