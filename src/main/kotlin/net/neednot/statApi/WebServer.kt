package net.neednot.statApi

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import io.javalin.Javalin
import io.javalin.json.JavalinJackson
import net.neednot.statApi.handlers.AccessHandler
import net.neednot.statApi.handlers.playerHandler
import net.neednot.statApi.handlers.serverHandler
import net.neednot.statApi.handlers.statisticsHandler
import net.neednot.statApi.serializers.ItemStackSerializer
import net.neednot.statApi.serializers.LocationSerializer
import org.bukkit.Location
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class WebServer(port: Int, val plugin: JavaPlugin) {
    private var app: Javalin

    init {
        val mapper = ObjectMapper()
        mapper.registerModule(BukkitModule)

        app = Javalin.create { config ->
            config.jsonMapper(JavalinJackson(mapper))
        }.start(port)

        val accessHandler = AccessHandler(plugin)
        app.beforeMatched(accessHandler::invoke)
        app.get("/server", ::serverHandler)
        app.get("/player/{tag}/statistics/{category}", ::statisticsHandler)
        app.get("/player/{tag}", ::playerHandler)
    }

    fun stop(): Javalin? = app.stop()
}

object BukkitModule: SimpleModule() {
    private fun readResolve(): Any = BukkitModule

    init {
        addSerializer(ItemStack::class.java, ItemStackSerializer())
        addSerializer(Location::class.java, LocationSerializer())
    }
}