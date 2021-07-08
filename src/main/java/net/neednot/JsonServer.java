package net.neednot;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Collection;

class JsonServer {
    public boolean Online = true;
    public int players;
    public String[] playerslist;

    public void setPlayers(int players) {
        this.players = players;
    }
    public void setPlayerslist(String[] playerslist) {
        this.playerslist = playerslist;
    }
}