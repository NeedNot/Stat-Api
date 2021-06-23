package net.neednot;

import de.tr7zw.nbtapi.*;
import de.tr7zw.nbtapi.data.NBTData;
import de.tr7zw.nbtapi.data.PlayerData;
import net.neednot.JsonPlayer;
import net.neednot.JsonArmor;
import net.neednot.JsonHelmet;
import net.neednot.JsonChestplate;
import net.neednot.JsonLeggings;
import net.neednot.JsonBoots;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.UUID;

public class PlayerOffline {

    public JsonPlayer JP = new JsonPlayer();
    JsonArmor JA = new JsonArmor();
    JsonHelmet JH = new JsonHelmet();
    JsonChestplate JC = new JsonChestplate();
    JsonLeggings JL = new JsonLeggings();
    JsonBoots JB = new JsonBoots();
    public UUID uuid;
    public String x;

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public JsonPlayer getPlayer() {

        NBTCompound player = NBTData.getOfflinePlayerData(uuid).getCompound();
        OfflinePlayer oplayer = Bukkit.getOfflinePlayer(uuid);
        JP.setOnline(false);
        JP.setName(oplayer.getName());

        //first login
        String firstlogin;
        long firstloginlong = oplayer.getFirstPlayed();
        firstlogin = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date(firstloginlong));
        JP.setFirstlogin(firstlogin);

        //last logout
        String lastlogout;
        long lastlogoutlong = oplayer.getLastPlayed();
        if (lastlogoutlong == 0) {
            lastlogout = null;
        } else {
            lastlogout = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date(lastlogoutlong));
        }
        JP.setLastlogout(lastlogout);

        //debug
        Player ned = Bukkit.getPlayer("Need_Not");

        //health
        DecimalFormat df = new DecimalFormat("0");
        float health1 = player.getFloat("Health");
        double health = Double.valueOf(df.format(health1));
        JP.setHealth(health);

        String posX = player.getDoubleList("Pos").get(0).toString() + ",";
        String posY = player.getDoubleList("Pos").get(1).toString() + ",";
        String posZ = player.getDoubleList("Pos").get(2).toString();

        String[] pos = ("X: " + posX + "Y: " + posY + "Z: " + posZ).split(",");
        JP.setCoords(pos);

        JP.setDeaths(oplayer.getStatistic(Statistic.DEATHS));
        JP.setXplevel(player.getInteger("XpLevel"));

        JP.setWorld(player.getString("Dimension"));

        NBTCompoundList inventory = player.getCompoundList("Inventory");

        int helmetdam;
        int leggingsdam;
        int chestplatedam;
        int bootsdam;

        if (player.getCompoundList("Inventory") != null) {
            for (NBTListCompound comp : inventory) {
                //helmet
                if (comp.getByte("Slot") == 103) {
                    String type = comp.getString("id").replace("minecraft:", "").replace("_", " ");
                    JH.setType(type);


                    NBTCompound tag = comp.getCompound("tag");

                    NBTCompound text = tag.getCompound("display");
                    if (text != null && !text.equals("[]")) {
                        if (!text.getString("Name").equals("")) {
                            String name = text.getString("Name").toString();
                            JH.setName(name.substring(9 , name.length() - 2));
                        }
                        else {
                            JH.setName(type);
                        }
                    }
                    else {
                        JH.setName(type);
                    }
                    helmetdam = tag.getInteger("Damage");

                    short max = 0;
                    switch (type) {
                        case "diamond helmet":
                            max = Material.DIAMOND_HELMET.getMaxDurability();
                            break;
                        case "golden helmet":
                            max = Material.GOLDEN_HELMET.getMaxDurability();
                            break;
                        case "iron helmet":
                            max = Material.IRON_HELMET.getMaxDurability();
                            break;
                        case "netherite helmet":
                            max = Material.NETHERITE_HELMET.getMaxDurability();
                            break;
                        case "chainmail helmet":
                            max = Material.CHAINMAIL_HELMET.getMaxDurability();
                            break;
                        case "leather helmet":
                            max = Material.LEATHER_HELMET.getMaxDurability();
                            break;
                    }
                    float percent = ((max - helmetdam) * 100f / max);
                    String total = String.format("%.2f" , percent) + "%";

                    JH.setDamage(total);

                    if (!tag.getCompoundList("Enchantments").toString().equalsIgnoreCase("[]")) {

                        ArrayList<String> helmetenchants = new ArrayList<String>();
                        for (NBTListCompound enchants : tag.getCompoundList("Enchantments")) {
                            String en = enchants.getString("id") + "_" + enchants.getShort("lvl");
                            helmetenchants.add(en);
                        }
                        String helmetenchantsstr = helmetenchants.toString().replace("[", "").replace("]", "").replace("None", "").replace(" ", "");
                        String[] helmetenchantsl = helmetenchantsstr.split(",");
                        JH.setEnchants(helmetenchantsl);
                    }
                    else {
                        JH.setEnchants("".split(""));
                    }
                }
                //chestplate
                if (comp.getByte("Slot") == 102) {
                    String type = comp.getString("id").replace("minecraft:", "").replace("_", " ");
                    JC.setType(type);


                    NBTCompound tag = comp.getCompound("tag");

                    NBTCompound text = tag.getCompound("display");
                    if (text != null && !text.equals("[]")) {
                        if (!text.getString("Name").equals("")) {
                            String name = text.getString("Name").toString();
                            JC.setName(name.substring(9 , name.length() - 2));
                        }
                        else {
                            JC.setName(type);
                        }
                    }
                    else {
                        JC.setName(type);
                    }

                    chestplatedam = tag.getInteger("Damage");

                    short max = 0;
                    switch (type) {
                        case "diamond chestplate":
                            max = Material.DIAMOND_CHESTPLATE.getMaxDurability();
                            break;
                        case "golden chestplate":
                            max = Material.GOLDEN_CHESTPLATE.getMaxDurability();
                            break;
                        case "iron chestplate":
                            max = Material.IRON_CHESTPLATE.getMaxDurability();
                            break;
                        case "netherite chestplate":
                            max = Material.NETHERITE_CHESTPLATE.getMaxDurability();
                            break;
                        case "chainmail chestplate":
                            max = Material.CHAINMAIL_CHESTPLATE.getMaxDurability();

                            break;
                        case "leather chestplate":
                            max = Material.LEATHER_CHESTPLATE.getMaxDurability();
                            break;
                    }
                    float percent = ((max - chestplatedam) * 100f / max);
                    String total = String.format("%.2f" , percent) + "%";


                    JC.setDamage(total);

                    if (!tag.getCompoundList("Enchantments").toString().equalsIgnoreCase("[]")) {

                        ArrayList<String> chestplateenchants = new ArrayList<String>();
                        for (NBTListCompound enchants : tag.getCompoundList("Enchantments")) {
                            String en = enchants.getString("id") + "_" + enchants.getShort("lvl");
                            chestplateenchants.add(en);
                        }
                        String chestplateenchantsstr = chestplateenchants.toString().replace("[", "").replace("]", "").replace("None", "").replace(" ", "");
                        String[] chestplateenchantsl = chestplateenchantsstr.split(",");
                        JC.setEnchants(chestplateenchantsl);
                    }
                    else {
                        JC.setEnchants("".split(""));
                    }
                }
                //leggings
                if (comp.getByte("Slot") == 101) {
                    String type = comp.getString("id").replace("minecraft:", "").replace("_", " ");
                    JL.setType(type);


                    NBTCompound tag = comp.getCompound("tag");

                    NBTCompound text = tag.getCompound("display");
                    if (text != null && !text.equals("[]")) {
                        if (!text.getString("Name").equals("")) {
                            String name = text.getString("Name").toString();
                            JL.setName(name.substring(9 , name.length() - 2));
                        }
                        else {
                            JL.setName(type);
                        }
                    }
                    else {
                        JL.setName(type);
                    }
                    leggingsdam = tag.getInteger("Damage");

                    short max = 0;
                    switch (type) {
                        case "diamond leggings":
                            max = Material.DIAMOND_LEGGINGS.getMaxDurability();
                            break;
                        case "golden leggings":
                            max = Material.GOLDEN_LEGGINGS.getMaxDurability();
                            break;
                        case "iron leggings":
                            max = Material.IRON_LEGGINGS.getMaxDurability();
                            break;
                        case "netherite leggings":
                            max = Material.NETHERITE_LEGGINGS.getMaxDurability();
                            break;
                        case "chainmail leggings":
                            max = Material.CHAINMAIL_LEGGINGS.getMaxDurability();
                            break;
                        case "leather leggings":
                            max = Material.LEATHER_LEGGINGS.getMaxDurability();
                            break;
                    }
                    float percent = ((max - leggingsdam) * 100f / max);
                    String total = String.format("%.2f" , percent) + "%";

                    JL.setDamage(total);

                    if (!tag.getCompoundList("Enchantments").toString().equalsIgnoreCase("[]")) {

                        ArrayList<String> leggingsenchants = new ArrayList<String>();
                        for (NBTListCompound enchants : tag.getCompoundList("Enchantments")) {
                            String en = enchants.getString("id") + "_" + enchants.getShort("lvl");
                            leggingsenchants.add(en);
                        }
                        String leggingsenchantsstr = leggingsenchants.toString().replace("[", "").replace("]", "").replace("None", "").replace(" ", "");
                        String[] leggingsenchantsl = leggingsenchantsstr.split(",");
                        JL.setEnchants(leggingsenchantsl);
                    }
                    else {
                        JL.setEnchants("".split(""));
                    }
                }
                //boots
                if (comp.getByte("Slot") == 100) {
                    String type = comp.getString("id").replace("minecraft:", "").replace("_", " ");
                    JB.setType(type);


                    NBTCompound tag = comp.getCompound("tag");

                    NBTCompound text = tag.getCompound("display");
                    if (text != null && !text.equals("[]")) {
                        if (!text.getString("Name").equals("")) {
                            String name = text.getString("Name").toString();
                            JB.setName(name.substring(9 , name.length() - 2));
                            JB.setName(name);
                        }
                        else {
                            JB.setName(type);
                        }
                    }
                    else {
                        JB.setName(type);
                    }
                    bootsdam = tag.getInteger("Damage");

                    short max = 0;
                    switch (type) {
                        case "diamond boots":
                            max = Material.DIAMOND_BOOTS.getMaxDurability();
                            break;
                        case "golden boots":
                            max = Material.GOLDEN_BOOTS.getMaxDurability();
                            break;
                        case "iron boots":
                            max = Material.IRON_BOOTS.getMaxDurability();
                            break;
                        case "netherite boots":
                            max = Material.NETHERITE_BOOTS.getMaxDurability();
                            break;
                        case "chainmail boots":
                            max = Material.CHAINMAIL_BOOTS.getMaxDurability();
                            break;
                        case "leather boots":
                            max = Material.LEATHER_BOOTS.getMaxDurability();
                            break;
                    }
                    float percent = ((max - bootsdam) * 100f / max);
                    String total = String.format("%.2f" , percent) + "%";

                    JB.setDamage(total);

                    if (!tag.getCompoundList("Enchantments").toString().equalsIgnoreCase("[]")) {

                        ArrayList<String> bootsenchants = new ArrayList<String>();
                        for (NBTListCompound enchants : tag.getCompoundList("Enchantments")) {
                            String en = enchants.getString("id") + "_" + enchants.getShort("lvl");
                            bootsenchants.add(en);
                        }
                        String bootsenchantsstr = bootsenchants.toString().replace("[", "").replace("]", "").replace("None", "").replace(" ", "");
                        String[] bootsenchantsl = bootsenchantsstr.split(",");
                        JB.setEnchants(bootsenchantsl);
                    }
                    else {
                        JB.setEnchants("".split(""));
                    }
                }
            }
            JA.setHelmet(JH);
            JA.setChestplate(JC);
            JA.setLeggings(JL);
            JA.setBoots(JB);
        }
        JP.setArmor(JA);
        return JP;
    }
}