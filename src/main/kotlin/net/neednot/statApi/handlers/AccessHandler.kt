package net.neednot.statApi.handlers

import io.javalin.http.Context
import io.javalin.http.UnauthorizedResponse
import org.bukkit.plugin.java.JavaPlugin
import java.security.MessageDigest

class AccessHandler(plugin: JavaPlugin) {
    private val apiKeys: List<String> = plugin.config.getStringList("api-keys")
    operator fun invoke(context: Context) {
        if (apiKeys.isEmpty()) return

        val rawToken = context.header("authorization")?.removePrefix("Bearer ")
        if (rawToken.isNullOrBlank() || sha256(rawToken) !in apiKeys) {
            throw UnauthorizedResponse()
        }
    }
}

private fun sha256(input: String): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hashBytes = digest.digest(input.toByteArray(Charsets.UTF_8))

    return hashBytes.joinToString("") { String.format("%02x", it) }
}