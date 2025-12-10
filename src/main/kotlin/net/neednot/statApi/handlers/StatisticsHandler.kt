package net.neednot.statApi.handlers

import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.http.NotFoundResponse
import net.neednot.statApi.WebServer
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.Statistic
import org.bukkit.entity.EntityType
import java.util.UUID

fun WebServer.statisticsHandler(ctx: Context) {
    val tag = ctx.pathParam("tag")
    val category = ctx.pathParam("category")
    if (!arrayOf("general", "mobs", "items").contains(category.lowercase())) {
        throw BadRequestResponse("Category must be 'general', 'mobs' or 'items'")
    }

    val player: OfflinePlayer
    if (tag.length == 36) {
        val uuid = UUID.fromString(tag)
        player = plugin.server.getOfflinePlayer(uuid)
    } else {
        @Suppress("DEPRECATION")
        player = plugin.server.getOfflinePlayer(tag)
    }
    if (!player.hasPlayedBefore()) {
        throw NotFoundResponse("Cannot find player with UUID or name of $tag")
    }
    val statistics = when (category) {
        "mobs" -> getMobStatistics(player)
        "items" -> getItemStatistics(player)
        else -> getGeneralStatistics(player)
    }
    ctx.json(statistics)
}

fun getGeneralStatistics(player: OfflinePlayer): Map<Statistic, Int> {
    return Statistic.entries.filter { it.type == Statistic.Type.UNTYPED }.associateWith { player.getStatistic(it ) }
}

fun getMobStatistics(player: OfflinePlayer): Map<EntityType, Map<Statistic, Int>> {
    val statistics = Statistic.entries.filter { it.type == Statistic.Type.ENTITY }
    return EntityType.entries.filter { it.isAlive }.associateWith { entity ->
        statistics.associateWith { player.getStatistic(it, entity) }
    }
}

fun getItemStatistics(player: OfflinePlayer): Map<Material, Map<Statistic, Int>> {
    val statistics = Statistic.entries.filter { it.type == Statistic.Type.BLOCK || it.type == Statistic.Type.ITEM }
    return Material.entries.filter { it.isItem }.associateWith { material ->
        statistics.filterNot { (!material.isBlock && it.isBlock) }.associateWith { player.getStatistic(it, material) }
    }
}