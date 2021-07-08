package net.neednot;


import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import net.neednot.listeners.Api;
import io.javalin.Javalin;
import org.bukkit.scheduler.BukkitScheduler;
import org.eclipse.jetty.websocket.server.WebSocketServerFactory;

import net.neednot.JsonData;
import net.neednot.JsonPlayer;
import net.neednot.JsonServer;
import net.neednot.JsonArmor;
import net.neednot.JsonHelmet;
import net.neednot.JsonChestplate;
import net.neednot.JsonLeggings;
import net.neednot.JsonBoots;
import net.neednot.JsonError;
import net.neednot.JsonBlocks;
import net.neednot.JsonItems;
import net.neednot.JsonMobs;
import net.neednot.JsonStats;

import java.io.File;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.*;

public final class StatApi extends JavaPlugin {

    private StatApi plugin;
    private static Javalin app;

    @Override
    public void onEnable() {

        plugin = this;

        FileConfiguration armor = this.getConfig();
        armor.options().copyDefaults(true);

        getServer().getPluginManager().registerEvents(new Api(this), this);
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());

        app = Javalin.create((javalinConfig) -> {
            javalinConfig.showJavalinBanner = false;
        }).start(7000);
        app.get("/player/:uuid", ctx -> {
            //classes
            JsonData JD = new JsonData();
            JsonPlayer JP = new JsonPlayer();
            JsonArmor JA = new JsonArmor();
            JsonHelmet JH = new JsonHelmet();
            JsonChestplate JC = new JsonChestplate();
            JsonLeggings JL = new JsonLeggings();
            JsonBoots JB = new JsonBoots();
            JsonError JE = new JsonError();
            boolean error = false;
            UUID uuid = UUID.randomUUID();



            try {
                uuid = UUID.fromString(ctx.pathParam("uuid"));
            }
            catch (IllegalArgumentException e1) {
                JE.setSuccess(false);
                JE.setError("Invalid UUID! A UUID should look like this: " + UUID.randomUUID().toString());
                ctx.json(JE);
                error = true;
            }
            if (!error) {
                //uuid to player
                OfflinePlayer player = (OfflinePlayer) Bukkit.getOfflinePlayer(uuid);

                if (player.hasPlayedBefore()) {

                    //first login
                    String firstlogin;
                    long firstloginlong = player.getFirstPlayed();
                    if (firstloginlong == 0) {
                        firstlogin = null;
                    } else {
                        firstlogin = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date(firstloginlong));
                    }
                    //lastout
                    String lastlogout;
                    long lastlogoutlong = player.getLastPlayed();
                    if (lastlogoutlong == 0) {
                        lastlogout = null;
                    } else {
                        lastlogout = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date(lastlogoutlong));
                    }
                    //status
                    boolean online;
                    if (player.isOnline()) {
                        online = true;
                    } else {
                        online = false;
                    }
                    //getting deaths
                    int deaths = player.getStatistic(Statistic.DEATHS);
                    JP.setDeaths(deaths);
                    //xp

                    //armor
                    //helmet
                    String helmetname = armor.get(uuid + ".helmet.name").toString();
                    String helmettype = armor.get(uuid + ".helmet.type").toString();

                    String helmetenchantsstr = armor.get(uuid + ".helmet.enchants").toString().replace("minecraft:", "").replace(" ", "").replace("_", " ");
                    String helmetdamage = armor.get(uuid + ".helmet.damage").toString();

                    String[] helmetenchants = helmetenchantsstr.replace("None", "").split(",");

                    JH.setName(helmetname);
                    JH.setType(helmettype);
                    JH.setDamage(helmetdamage);
                    JH.setEnchants(helmetenchants);
                    JA.setHelmet(JH);

                    //chestplate
                    String chestplatename = armor.get(uuid + ".chestplate.name").toString();
                    String chestplatetype = armor.get(uuid + ".chestplate.type").toString();

                    String chestplateenchantsstr = armor.get(uuid + ".chestplate.enchants").toString().replace("minecraft:", "").replace(" ", "").replace("_", " ");
                    String chestplatedamage = armor.get(uuid + ".chestplate.damage").toString();

                    String[] chestplateenchants = chestplateenchantsstr.replace("None", "").split(",");


                    JC.setName(chestplatename);
                    JC.setType(chestplatetype);
                    JC.setDamage(chestplatedamage);
                    JC.setEnchants(chestplateenchants);
                    JA.setChestplate(JC);

                    //leggings
                    String leggingsname = armor.get(uuid + ".leggings.name").toString();
                    String leggingstype = armor.get(uuid + ".leggings.type").toString();

                    String leggingsenchantsstr = armor.get(uuid + ".leggings.enchants").toString().replace("minecraft:", "").replace(" ", "").replace("_", " ");
                    String leggingsdamage = armor.get(uuid + ".leggings.damage").toString();

                    String[] leggingsenchants = leggingsenchantsstr.replace("None", "").split(",");

                    JL.setName(leggingsname);
                    JL.setType(leggingstype);
                    JL.setDamage(leggingsdamage);
                    JL.setEnchants(leggingsenchants);
                    JA.setLeggings(JL);

                    //boots
                    String bootsname = armor.get(uuid + ".boots.name").toString();
                    String bootstype = armor.get(uuid + ".boots.type").toString();

                    String bootsenchantsstr = armor.get(uuid + ".boots.enchants").toString().replace("minecraft:", "").replace(" ", "").replace("_", " ");
                    String bootsdamage = armor.get(uuid + ".boots.damage").toString();

                    String[] bootsenchants = bootsenchantsstr.replace("None", "").split(",");

                    //health
                    DecimalFormat df = new DecimalFormat("0");
                    if (player.isOnline()) {
                        double health = player.getPlayer().getHealth();
                        health = Double.valueOf(df.format(health));
                        JP.setHealth(health);
                    } else {
                        double health = Double.parseDouble(armor.get(uuid + ".health").toString());
                        health = Double.valueOf(df.format(health));
                        JP.setHealth(health);
                    }

                    //blocks
                    ArrayList<JsonBlocks> blocks = new ArrayList<JsonBlocks>();

                    for (Material m : Material.values()) {
                        if (m.isBlock()) {

                            JsonBlocks JBL = new JsonBlocks();

                            String type = m.name();
                            String name = type.replace("_", " ").toLowerCase();

                            int placed = player.getStatistic(Statistic.USE_ITEM, m);
                            int mined = player.getStatistic(Statistic.MINE_BLOCK, m);
                            int crafted = player.getStatistic(Statistic.CRAFT_ITEM, m);
                            int dropped = player.getStatistic(Statistic.DROP, m);
                            int broken = player.getStatistic(Statistic.BREAK_ITEM, m);
                            int pickedup = player.getStatistic(Statistic.PICKUP, m);

                            JBL.setName(name);
                            JBL.setType(type);
                            JBL.setPlaced(placed);
                            JBL.setMined(mined);
                            JBL.setCrafted(crafted);
                            JBL.setDropped(dropped);
                            JBL.setBroken(broken);
                            JBL.setPickedup(pickedup);
                            blocks.add(JBL);
                        }
                    }

                    ArrayList<JsonItems> items = new ArrayList<JsonItems>();

                    for (Material m : Material.values()) {
                        if (m.isItem()) {

                            JsonItems JI = new JsonItems();

                            String type = m.name();
                            String name = type.replace("_", " ").toLowerCase();

                            int broken = player.getStatistic(Statistic.BREAK_ITEM, m);
                            int crafted = player.getStatistic(Statistic.CRAFT_ITEM, m);
                            int used = player.getStatistic(Statistic.USE_ITEM, m);
                            int dropped = player.getStatistic(Statistic.DROP, m);
                            int pickedup = player.getStatistic(Statistic.PICKUP, m);

                            JI.setName(name);
                            JI.setType(type);
                            JI.setBroken(broken);
                            JI.setCrafted(crafted);
                            JI.setUsed(used);
                            JI.setDropped(dropped);
                            JI.setPickedup(pickedup);
                            items.add(JI);
                        }
                    }

                    //mobs
                    ArrayList<JsonMobs> mobs = new ArrayList<JsonMobs>();

                    for (EntityType e : EntityType.values()) {

                        JsonMobs JM = new JsonMobs();

                        String mob = e.name();
                        String name = e.name().replace("_", " ").toLowerCase();
                        int kills;
                        int mdeaths;
                        try {
                            if (e.name().equalsIgnoreCase("PLAYER")) {
                                kills = player.getStatistic(Statistic.PLAYER_KILLS);
                            } else {
                                kills = player.getStatistic(Statistic.KILL_ENTITY, e);
                            }
                        } catch (IllegalArgumentException e1) {
                            kills = 0;
                        }
                        try {
                            mdeaths = player.getStatistic(Statistic.ENTITY_KILLED_BY, e);
                        } catch (IllegalArgumentException e1) {
                            mdeaths = 0;
                        }

                        JM.setMob(mob);
                        JM.setName(name);
                        JM.setKills(kills);
                        JM.setDeaths(mdeaths);

                        mobs.add(JM);

                    }

                    //items
                    ArrayList<JsonStats> stats = new ArrayList<JsonStats>();
                    for (Statistic s : Statistic.values()) {

                        JsonStats JS = new JsonStats();


                        if (s.getType() == Statistic.Type.UNTYPED) {
                            String stat = s.name();
                            int value = player.getStatistic(s);

                            JS.setValue(value);
                            JS.setStat(stat);

                            stats.add(JS);
                        }

                    }

                    JB.setName(bootsname);
                    JB.setType(bootstype);
                    JB.setDamage(bootsdamage);
                    JB.setEnchants(bootsenchants);
                    JA.setBoots(JB);

                    //writing them down
                    String name = player.getName();
                    JP.setFirstlogin(firstlogin);
                    JP.setLastlogout(lastlogout);
                    JP.setName(name);
                    JP.setOnline(online);
                    JP.setArmor(JA);
                    JD.setPlayer(JP);
                    JD.setBlocks(blocks);
                    JD.setItems(items);
                    JD.setMobs(mobs);
                    JD.setStats(stats);
                    JD.setSuccess(true);
                    ctx.json(JD);
                } else {
                    JE.setSuccess(false);
                    JE.setError("Player not found!");
                    ctx.json(JE);
                }
            }
        });
        app.get("/server", ctx -> {
            JsonServer JS = new JsonServer();
            int players = Bukkit.getOnlinePlayers().size();
            JS.setPlayers(players);
            String playerslistSTR = Bukkit.getOnlinePlayers().toString().replace("CraftPlayer{name=","").replace("}", "").replace("[", "").replace("]", "").replace(",", "");
            String[] playerslist = playerslistSTR.split(" ");

            JS.setPlayerslist(playerslist);

            ctx.json(JS);
        });
        app.get("/whitelist/remove/:name", ctx -> {

            String name =  ctx.pathParam("name");

            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin , new Runnable() {
                @Override
                public void run() {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "whitelist remove " + name);
                }
            }, 20L);

            ctx.json("unwhitelisted " + name);

        });
        app.get("/whitelist/add/:name", ctx -> {
            String name =  ctx.pathParam("name");

            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin , new Runnable() {
                @Override
                public void run() {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "whitelist add " + name);
                }
            }, 20L);

            ctx.json("whitelisted " + name);
        });
        app.get("/kick/:uuid", ctx -> {
            boolean error = false;
            UUID uuid = UUID.randomUUID();

            try {
                uuid = UUID.fromString(ctx.pathParam("uuid"));
            }
            catch (IllegalArgumentException e1) {
                ctx.json("No player found");
                error = true;
            }
            if (!error) {
                //uuid to player
                OfflinePlayer player = (OfflinePlayer) Bukkit.getOfflinePlayer(uuid);

                if (player.isOnline() && player.hasPlayedBefore()) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin , new Runnable() {
                        @Override
                        public void run() {
                            player.getPlayer().kickPlayer("Kicked by discord admin");
                        }
                    }, 20L);
                }

                ctx.json("kicked " + player.getName());
            }
        });
        app.get("/ban/:uuid", ctx -> {
            boolean error = false;
            UUID uuid = UUID.randomUUID();

            try {
                uuid = UUID.fromString(ctx.pathParam("uuid"));
            }
            catch (IllegalArgumentException e1) {
                ctx.json("No player found");
                error = true;
            }
            if (!error) {
                //uuid to player
                OfflinePlayer player = (OfflinePlayer) Bukkit.getOfflinePlayer(uuid);

                Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), "Banned", null, "Admin on discord");
                if (player.isOnline() && player.hasPlayedBefore()) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin , new Runnable() {
                        @Override
                        public void run() {
                            player.getPlayer().kickPlayer("Banned");
                        }
                    }, 20L);
                }

                ctx.json("Banned " + player.getName());
            }
        });
        app.get("/unban/:uuid", ctx -> {
            boolean error = false;
            UUID uuid = UUID.randomUUID();

            try {
                uuid = UUID.fromString(ctx.pathParam("uuid"));
            }
            catch (IllegalArgumentException e1) {
                ctx.json("No player found");
                error = true;
            }
            if (!error) {
                //uuid to player
                OfflinePlayer player = (OfflinePlayer) Bukkit.getOfflinePlayer(uuid);

                getServer().getBanList(BanList.Type.NAME).pardon(player.getName());

                ctx.json("Unbanned " + player.getName());
            }
        });
        //Bukkit.getOfflinePlayer(ctx.pathParam("uuid")).getFirstPlayed();
        Thread.currentThread().setContextClassLoader(classLoader);

    }

    @Override
    public void onDisable() {
        app.stop();
    }
}