package net.neednot;

public class JsonItems {

    public String name;
    public String type;
    public int broken;
    public int crafted;
    public int used;
    public int dropped;
    public int pickedup;

    public void setName(String name) {
        this.name = name;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setBroken(int broken) {
        this.broken = broken;
    }
    public void setCrafted(int crafted) {
        this.crafted = crafted;
    }
    public void setUsed(int used) {
        this.used = used;
    }
    public void setDropped(int dropped) {
        this.dropped = dropped;
    }
    public void setPickedup(int pickedup) {
        this.pickedup = pickedup;
    }

}