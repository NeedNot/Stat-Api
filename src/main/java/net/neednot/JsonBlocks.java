package net.neednot;

import org.bukkit.Material;

import java.util.ArrayList;

public class JsonBlocks {

    public String name;
    public String type;
    public int mined;
    public int placed;
    public int crafted;
    public int dropped;
    public int broken;
    public int pickedup;

    public void setName(String name) {
        this.name = name;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setPlaced(int placed) {
     this.placed = placed;
    }
    public void setMined(int mined) {
        this.mined = mined;
    }
    public void setCrafted(int crafted) {
        this.crafted = crafted;
    }
    public void setDropped(int dropped) {
        this.dropped = dropped;
    }
    public void setBroken(int broken) {
        this.broken = broken;
    }
    public void setPickedup(int pickedup) {
        this.pickedup = pickedup;
    }
}