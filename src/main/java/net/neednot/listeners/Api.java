package net.neednot.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import net.neednot.JsonPlayer;
import net.neednot.StatApi;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class Api implements Listener {

    StatApi plugin;

    public Api(StatApi pl) {
        plugin = pl;
    }

    public void armor() {
        for(Player p : Bukkit.getOnlinePlayers()){
            UUID uuid = p.getUniqueId();

            String helmetname;
            String helmettype = "None";
            ArrayList<String> helmetenchants = new ArrayList<String>();
            helmetenchants.add("None");
            String helmetdamage = "None";

            String chestplatename;
            String chestplatetype = "None";
            ArrayList<String> chestplateenchants = new ArrayList<String>();
            chestplateenchants.add("None");
            String chestplatedamage = "None";

            String leggingsname;
            String leggingstype = "None";
            ArrayList<String> leggingsenchants = new ArrayList<String>();
            leggingsenchants.add("None");
            String leggingsdamage = "None";

            String bootsname;
            String bootstype = "None";
            ArrayList<String> bootsenchants = new ArrayList<String>();
            bootsenchants.add("None");
            String bootsdamage = "None";

            //health

            double health = p.getHealth();
            plugin.getConfig().set(uuid + ".health", health);


            if (p.getEquipment() != null) {
                //armor
                //helmet

                if (p.getEquipment().getHelmet() == null) {
                    helmetname = "None";
                } else {
                    if (p.getEquipment().getHelmet().hasItemMeta()) {

                        org.bukkit.inventory.meta.Damageable helmetdamagedam = ((org.bukkit.inventory.meta.Damageable) p.getInventory().getHelmet().getItemMeta());

                        int max = p.getEquipment().getHelmet().getType().getMaxDurability();

                        float percent = (max-helmetdamagedam.getDamage())*100f / max;
                        helmetdamage = String.format("%.2f", percent) + "%";



                        if (p.getEquipment().getHelmet().getItemMeta().hasEnchants()) {
                            ItemMeta item = p.getEquipment().getHelmet().getItemMeta();

                            helmetenchants.remove("None");

                            Map<Enchantment, Integer> map = item.getEnchants();
                            for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
                                helmetenchants.add(entry.getKey().getKey() + ":_" + entry.getValue().toString());
                            }
                        }
                        if (p.getEquipment().getHelmet().getItemMeta().hasDisplayName()) {
                            helmetname = p.getEquipment().getHelmet().getItemMeta().getDisplayName();
                        }
                        else {
                            helmetname = p.getEquipment().getHelmet().getType().name().replace("_", " ").toLowerCase();
                        }
                    } else {
                        helmetdamage = "100%";
                        helmetname = p.getEquipment().getHelmet().getType().name().replace("_", " ").toLowerCase();
                    }
                    helmettype = p.getEquipment().getHelmet().getType().name().replace("_", " ").toLowerCase();
                }

                //chestplate

                if (p.getEquipment().getChestplate() == null) {
                    chestplatename = "None";
                } else {
                    if (p.getEquipment().getChestplate().hasItemMeta()) {

                        ItemMeta item = p.getEquipment().getChestplate().getItemMeta();

                        org.bukkit.inventory.meta.Damageable chestplatedamagedam = ((org.bukkit.inventory.meta.Damageable) p.getInventory().getChestplate().getItemMeta());

                        int max = p.getEquipment().getHelmet().getType().getMaxDurability();

                        float percent = (max-chestplatedamagedam.getDamage())*100f / max;
                        chestplatedamage = String.format("%.2f", percent) + "%";


                        if (p.getEquipment().getChestplate().getItemMeta().hasEnchants()) {

                            chestplateenchants.remove("None");

                            Map<Enchantment, Integer> map = item.getEnchants();
                            for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
                                chestplateenchants.add(entry.getKey().getKey() + ":_" + entry.getValue().toString());
                            }
                        }
                        if (p.getEquipment().getChestplate().getItemMeta().hasDisplayName()) {
                            chestplatename = p.getEquipment().getChestplate().getItemMeta().getDisplayName();
                        }
                        else {
                            chestplatename = p.getEquipment().getChestplate().getType().name().replace("_", " ").toLowerCase();
                        }
                    } else {
                        chestplatedamage = "100%";
                        chestplatename = p.getEquipment().getChestplate().getType().name().replace("_", " ").toLowerCase();
                    }
                    chestplatetype = p.getEquipment().getChestplate().getType().name().replace("_", " ").toLowerCase();
                }

                //leggings
                //name
                if (p.getEquipment().getLeggings() == null) {
                    leggingsname = "None";
                } else {
                    if (p.getEquipment().getLeggings().hasItemMeta()) {

                        ItemMeta item = p.getEquipment().getLeggings().getItemMeta();

                        org.bukkit.inventory.meta.Damageable leggingsdamagedam = ((org.bukkit.inventory.meta.Damageable) p.getInventory().getLeggings().getItemMeta());

                        int max = p.getEquipment().getHelmet().getType().getMaxDurability();

                        float percent = (max-leggingsdamagedam.getDamage())*100f / max;
                        leggingsdamage = String.format("%.2f", percent) + "%";


                        if (p.getEquipment().getLeggings().getItemMeta().hasEnchants()) {

                            leggingsenchants.remove("None");

                            Map<Enchantment, Integer> map = item.getEnchants();
                            for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
                                leggingsenchants.add(entry.getKey().getKey() + ":_" + entry.getValue().toString());
                            }
                        }
                        if (p.getEquipment().getLeggings().getItemMeta().hasDisplayName()) {
                            leggingsname = p.getEquipment().getLeggings().getItemMeta().getDisplayName();
                        }
                        else {
                            leggingsname = p.getEquipment().getLeggings().getType().name().replace("_", " ").toLowerCase();
                        }
                    } else {
                        leggingsdamage = "100%";
                        leggingsname = p.getEquipment().getLeggings().getType().name().replace("_", " ").toLowerCase();
                    }
                    leggingstype = p.getEquipment().getLeggings().getType().name().replace("_", " ").toLowerCase();
                }

                //boots

                if (p.getEquipment().getBoots() == null) {
                    bootsname = "None";
                } else {
                    if (p.getEquipment().getBoots().hasItemMeta()) {

                        ItemMeta item = p.getEquipment().getBoots().getItemMeta();

                        org.bukkit.inventory.meta.Damageable bootsdamagedam = ((org.bukkit.inventory.meta.Damageable) p.getInventory().getBoots().getItemMeta());

                        int max = p.getEquipment().getBoots().getType().getMaxDurability();

                        float percent = (max-bootsdamagedam.getDamage())*100f / max;
                        bootsdamage = String.format("%.2f", percent) + "%";


                        if (p.getEquipment().getBoots().getItemMeta().hasEnchants()) {

                            bootsenchants.remove("None");

                            Map<Enchantment, Integer> map = item.getEnchants();
                            for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
                                bootsenchants.add(entry.getKey().getKey() + ":_" + entry.getValue().toString());
                            }
                        }
                        if (p.getEquipment().getBoots().getItemMeta().hasDisplayName()) {
                            bootsname = p.getEquipment().getBoots().getItemMeta().getDisplayName();
                        }
                        else {
                            bootsname = p.getEquipment().getBoots().getType().name().replace("_", " ").toLowerCase();
                        }
                    } else {
                        bootsdamage = "100%";
                        bootsname = p.getEquipment().getBoots().getType().name().replace("_", " ").toLowerCase();
                    }
                    bootstype = p.getEquipment().getBoots().getType().name().replace("_", " ").toLowerCase();
                }
            }
            else {
                helmetname = "None";
                chestplatename = "None";
                leggingsname = "None";
                bootsname = "None";
            }


            plugin.getConfig().set(uuid + ".helmet.name", helmetname);
            plugin.getConfig().set(uuid + ".helmet.type", helmettype);
            plugin.getConfig().set(uuid + ".helmet.enchants", helmetenchants.toString().replace("[", "").replace("]", ""));
            plugin.getConfig().set(uuid + ".helmet.damage", helmetdamage);

            plugin.getConfig().set(uuid + ".chestplate.name", chestplatename);
            plugin.getConfig().set(uuid + ".chestplate.type", chestplatetype);
            plugin.getConfig().set(uuid + ".chestplate.enchants", chestplateenchants.toString().replace("[", "").replace("]", ""));
            plugin.getConfig().set(uuid + ".chestplate.damage", chestplatedamage);

            plugin.getConfig().set(uuid + ".leggings.name", leggingsname);
            plugin.getConfig().set(uuid + ".leggings.type", leggingstype);
            plugin.getConfig().set(uuid + ".leggings.enchants", leggingsenchants.toString().replace("[", "").replace("]", ""));
            plugin.getConfig().set(uuid + ".leggings.damage", leggingsdamage);

            plugin.getConfig().set(uuid + ".boots.name", bootsname);
            plugin.getConfig().set(uuid + ".boots.type", bootstype);
            plugin.getConfig().set(uuid + ".boots.enchants", bootsenchants.toString().replace("[", "").replace("]", ""));
            plugin.getConfig().set(uuid + ".boots.damage", bootsdamage);

            plugin.saveConfig();
        }
    }

    @EventHandler
    public void loggout(EntityDamageEvent event) {

        if (event.getEntity().getType().equals(EntityType.PLAYER)) {
            Player player = (Player) event.getEntity();

            double health = player.getHealth();
            UUID uuid = player.getUniqueId();
            plugin.getConfig().set(uuid + ".health", health);
        }
    }
}