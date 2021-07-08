
package net.neednot;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xerces.internal.xs.StringList;
import jdk.nashorn.internal.ir.ObjectNode;
import net.neednot.StatApi;
import org.bukkit.Bukkit;
import net.neednot.JsonPlayer;
import net.neednot.JsonBlocks;
import net.neednot.JsonItems;
import net.neednot.JsonMobs;
import net.neednot.JsonStats;

import java.util.ArrayList;

class JsonData {
    public boolean success;
    public JsonPlayer player = new JsonPlayer();

    public void setSuccess(boolean working) {
        this.success = working;
    }
    public void setPlayer(JsonPlayer JP) {
        this.player = JP;
    }
}