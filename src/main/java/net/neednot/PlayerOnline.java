package net.neednot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import net.neednot.JsonPlayer;
import net.neednot.JsonArmor;
import net.neednot.JsonHelmet;
import net.neednot.JsonChestplate;
import net.neednot.JsonLeggings;
import net.neednot.JsonBoots;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerOnline {

    public JsonPlayer JP = new JsonPlayer();
    JsonArmor JA = new JsonArmor();
    JsonHelmet JH = new JsonHelmet();
    JsonChestplate JC = new JsonChestplate();
    JsonLeggings JL = new JsonLeggings();
    JsonBoots JB = new JsonBoots();
    public UUID uuid;

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public JsonPlayer getPlayer() {

        Player player = Bukkit.getPlayer(uuid);

        JP.setName(player.getName());
        //first login
        String firstlogin;
        long firstloginlong = player.getFirstPlayed();
        firstlogin = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date(firstloginlong));
        JP.setFirstlogin(firstlogin);

        //lastout
        String lastlogout;
        long lastlogoutlong = player.getLastPlayed();
        if (lastlogoutlong == 0) {
            lastlogout = null;
        } else {
            lastlogout = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date(lastlogoutlong));
        }
        JP.setLastlogout(lastlogout);
        JP.setOnline(true);

        //coords
        String[] coords = ("X: " + player.getLocation().getX() + "," + "Y: " + player.getLocation().getY() + "," + "Z: " + player.getLocation().getZ()).split(",");
        JP.setCoords(coords);

        //dimension
        String world = player.getWorld().getName();

        switch (world) {
            case "world":
                world = "minecraft:overworld";
                break;

            case "world_the_end":
                world = "minecraft:the_end";
                break;

            case "world_nether":
                world = "minecraft:the_nether";
                break;
        }
        JP.setWorld(world);

        //health
        DecimalFormat df = new DecimalFormat("0");
        if (player.isOnline()) {
            double health = player.getPlayer().getHealth();
            health = Double.valueOf(df.format(health));
            JP.setHealth(health);
        }

        JP.setDeaths(player.getStatistic(Statistic.DEATHS));
        JP.setXplevel(player.getLevel());


        //armor
        if (player.getEquipment() != null) {
            //helmet
            String helmetname;
            String helmettype = "None";
            ArrayList<String> helmetenchants = new ArrayList<String>();
            helmetenchants.add("None");
            String helmetdamage = "None";

            if (player.getEquipment().getHelmet() == null) {
                helmetname = "None";
            } else {
                if (player.getEquipment().getHelmet().hasItemMeta()) {

                    org.bukkit.inventory.meta.Damageable helmetdamagedam = ((org.bukkit.inventory.meta.Damageable) player.getInventory().getHelmet().getItemMeta());

                    int max = player.getEquipment().getHelmet().getType().getMaxDurability();

                    float percent = (max - helmetdamagedam.getDamage()) * 100f / max;
                    helmetdamage = String.format("%.2f", percent) + "%";


                    if (player.getEquipment().getHelmet().getItemMeta().hasEnchants()) {
                        ItemMeta item = player.getEquipment().getHelmet().getItemMeta();

                        helmetenchants.remove("None");

                        Map<Enchantment, Integer> map = item.getEnchants();
                        for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
                            helmetenchants.add(entry.getKey().getKey() + ":_" + entry.getValue().toString());
                        }
                    }
                    if (player.getEquipment().getHelmet().getItemMeta().hasDisplayName()) {
                        helmetname = player.getEquipment().getHelmet().getItemMeta().getDisplayName();
                    } else {
                        helmetname = player.getEquipment().getHelmet().getType().name().replace("_", " ").toLowerCase();
                    }
                } else {
                    helmetdamage = "100%";
                    helmetname = player.getEquipment().getHelmet().getType().name().replace("_", " ").toLowerCase();
                }
                helmettype = player.getEquipment().getHelmet().getType().name().replace("_", " ").toLowerCase();
            }
            JH.setName(helmetname);
            JH.setType(helmettype);
            JH.setDamage(helmetdamage);
            String helmetenchantsstr = helmetenchants.toString().replace("[", "").replace("]", "").replace("None", "");
            String[] helmetenchantsl = helmetenchantsstr.split(",");
            JH.setEnchants(helmetenchantsl);
            JA.setHelmet(JH);

            //chestplate

            String chestplatename;
            String chestplatetype = "None";
            ArrayList<String> chestplateenchants = new ArrayList<String>();
            chestplateenchants.add("None");
            String chestplatedamage = "None";

            if (player.getEquipment().getChestplate() == null) {
                chestplatename = "None";
            } else {
                if (player.getEquipment().getChestplate().hasItemMeta()) {

                    ItemMeta item = player.getEquipment().getChestplate().getItemMeta();

                    org.bukkit.inventory.meta.Damageable chestplatedamagedam = ((org.bukkit.inventory.meta.Damageable) player.getInventory().getChestplate().getItemMeta());

                    int max = player.getEquipment().getChestplate().getType().getMaxDurability();

                    float percent = (max - chestplatedamagedam.getDamage()) * 100f / max;
                    chestplatedamage = String.format("%.2f", percent) + "%";


                    if (player.getEquipment().getChestplate().getItemMeta().hasEnchants()) {

                        chestplateenchants.remove("None");

                        Map<Enchantment, Integer> map = item.getEnchants();
                        for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
                            chestplateenchants.add(entry.getKey().getKey() + ":_" + entry.getValue().toString());
                        }
                    }
                    if (player.getEquipment().getChestplate().getItemMeta().hasDisplayName()) {
                        chestplatename = player.getEquipment().getChestplate().getItemMeta().getDisplayName();
                    } else {
                        chestplatename = player.getEquipment().getChestplate().getType().name().replace("_", " ").toLowerCase();
                    }
                } else {
                    chestplatedamage = "100%";
                    chestplatename = player.getEquipment().getChestplate().getType().name().replace("_", " ").toLowerCase();
                }
                chestplatetype = player.getEquipment().getChestplate().getType().name().replace("_", " ").toLowerCase();
            }
            JC.setName(chestplatename);
            JC.setType(chestplatetype);
            JC.setDamage(chestplatedamage);
            String chestplateenchantsstr = chestplateenchants.toString().replace("[", "").replace("]", "").replace("None", "");
            String[] chestplateenchantsl = chestplateenchantsstr.split(",");
            JC.setEnchants(chestplateenchantsl);
            JA.setChestplate(JC);

            //leggings

            String leggingsname;
            String leggingstype = "None";
            ArrayList<String> leggingsenchants = new ArrayList<String>();
            leggingsenchants.add("None");
            String leggingsdamage = "None";

            if (player.getEquipment().getLeggings() == null) {
                leggingsname = "None";
            } else {
                if (player.getEquipment().getLeggings().hasItemMeta()) {

                    ItemMeta item = player.getEquipment().getLeggings().getItemMeta();

                    org.bukkit.inventory.meta.Damageable leggingsdamagedam = ((org.bukkit.inventory.meta.Damageable) player.getInventory().getLeggings().getItemMeta());

                    int max = player.getEquipment().getLeggings().getType().getMaxDurability();

                    float percent = (max - leggingsdamagedam.getDamage()) * 100f / max;
                    leggingsdamage = String.format("%.2f", percent) + "%";


                    if (player.getEquipment().getLeggings().getItemMeta().hasEnchants()) {

                        leggingsenchants.remove("None");

                        Map<Enchantment, Integer> map = item.getEnchants();
                        for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
                            leggingsenchants.add(entry.getKey().getKey() + ":_" + entry.getValue().toString());
                        }
                    }
                    if (player.getEquipment().getLeggings().getItemMeta().hasDisplayName()) {
                        leggingsname = player.getEquipment().getLeggings().getItemMeta().getDisplayName();
                    } else {
                        leggingsname = player.getEquipment().getLeggings().getType().name().replace("_", " ").toLowerCase();
                    }
                } else {
                    leggingsdamage = "100%";
                    leggingsname = player.getEquipment().getLeggings().getType().name().replace("_", " ").toLowerCase();
                }
                leggingstype = player.getEquipment().getLeggings().getType().name().replace("_", " ").toLowerCase();
            }
            JL.setName(leggingsname);
            JL.setType(leggingstype);
            JL.setDamage(leggingsdamage);
            String leggingsenchantsstr = leggingsenchants.toString().replace("[", "").replace("]", "").replace("None", "");
            String[] leggingsenchantsl = leggingsenchantsstr.split(",");
            JL.setEnchants(leggingsenchantsl);
            JA.setLeggings(JL);

            //boots

            String bootsname;
            String bootstype = "None";
            ArrayList<String> bootsenchants = new ArrayList<String>();
            bootsenchants.add("None");
            String bootsdamage = "None";

            if (player.getEquipment().getBoots() == null) {
                bootsname = "None";
            } else {
                if (player.getEquipment().getBoots().hasItemMeta()) {

                    ItemMeta item = player.getEquipment().getBoots().getItemMeta();

                    org.bukkit.inventory.meta.Damageable bootsdamagedam = ((org.bukkit.inventory.meta.Damageable) player.getInventory().getBoots().getItemMeta());

                    int max = player.getEquipment().getBoots().getType().getMaxDurability();

                    float percent = (max - bootsdamagedam.getDamage()) * 100f / max;
                    bootsdamage = String.format("%.2f", percent) + "%";


                    if (player.getEquipment().getBoots().getItemMeta().hasEnchants()) {

                        bootsenchants.remove("None");

                        Map<Enchantment, Integer> map = item.getEnchants();
                        for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
                            bootsenchants.add(entry.getKey().getKey() + ":_" + entry.getValue().toString());
                        }
                    }
                    if (player.getEquipment().getBoots().getItemMeta().hasDisplayName()) {
                        bootsname = player.getEquipment().getBoots().getItemMeta().getDisplayName();
                    } else {
                        bootsname = player.getEquipment().getBoots().getType().name().replace("_", " ").toLowerCase();
                    }
                } else {
                    bootsdamage = "100%";
                    bootsname = player.getEquipment().getBoots().getType().name().replace("_", " ").toLowerCase();
                }
                bootstype = player.getEquipment().getBoots().getType().name().replace("_", " ").toLowerCase();
            }

            JB.setName(bootsname);
            JB.setType(bootstype);
            JB.setDamage(bootsdamage);
            String bootsenchantsstr = bootsenchants.toString().replace("[", "").replace("]", "").replace("None", "");
            String[] bootsenchantsl = bootsenchantsstr.split(",");
            JB.setEnchants(bootsenchantsl);
            JA.setBoots(JB);

            JP.setArmor(JA);

        }
        return JP;
    }

}