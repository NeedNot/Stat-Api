package net.neednot.statApi

import org.bukkit.plugin.java.JavaPlugin

class StatApi : JavaPlugin() {
    private lateinit var webServer: WebServer
    override fun onEnable() {
        config.options().copyDefaults(true)
        saveConfig()
        val port = config.getInt("port", 7000)
        val classLoader = Thread.currentThread().getContextClassLoader()
        Thread.currentThread().setContextClassLoader(this.getClassLoader())
        webServer = WebServer(port, this)
        Thread.currentThread().setContextClassLoader(classLoader)

        logger.info("Stat Api server is enabled")
    }

    override fun onDisable() {
       webServer.stop()
    }
}
