package net.neednot.statApi.handlers

import io.javalin.http.Context
import net.neednot.statApi.WebServer
import java.util.UUID

data class Player(
    val uuid: UUID,
    val name: String
)

data class ServerResponse(
    val playerCount: Int,
    val players: List<Player>
)

fun WebServer.serverHandler(ctx: Context) {
    val players = plugin.server.onlinePlayers.map { p -> Player(p.uniqueId, p.name) }
    ctx.json(ServerResponse(players.size, players))
}