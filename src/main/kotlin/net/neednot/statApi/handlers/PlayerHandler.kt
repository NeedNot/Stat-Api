package net.neednot.statApi.handlers
import de.tr7zw.nbtapi.NBT
import de.tr7zw.nbtapi.iface.ReadWriteNBT
import io.javalin.http.Context
import io.javalin.http.NotFoundResponse
import net.neednot.statApi.WebServer
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.inventory.ItemStack
import java.io.File
import java.util.*


data class Armor(
    val helmet: ItemStack?,
    val chestplate: ItemStack?,
    val leggings: ItemStack?,
    val boots: ItemStack?
) {
    companion object {
        fun fromArmorContents(armor: Array<ItemStack?>): Armor {
            return Armor(
                helmet = armor[3],
                chestplate = armor[2],
                leggings = armor[1],
                boots = armor[0]
            )
        }

        fun fromNBTInventory(equipment: ReadWriteNBT?): Armor {
            if (equipment?.keys.isNullOrEmpty()) return Armor(null, null, null, null)
            val head = NBT.itemStackFromNBT(equipment.getCompound("head"))
            val chest = NBT.itemStackFromNBT(equipment.getCompound("chest"))
            val legs = NBT.itemStackFromNBT(equipment.getCompound("legs"))
            val feet = NBT.itemStackFromNBT(equipment.getCompound("feet"))

            return Armor(head, chest, legs, feet)
        }
    }
}

data class PlayerResponse(
    val uuid: UUID,
    val name: String,
    val health: Double? = null,
    val foodLevel: Int? = null,
    val location: Location?,
    val armor: Armor? = null,
    val firstPlayed: Long,
    val lastPlayed: Long,
    val online: Boolean
)

fun WebServer.playerHandler(ctx: Context) {
    val tag = ctx.pathParam("tag")
    val player: OfflinePlayer?
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

    val showPlayerCoordinates = plugin.config.getBoolean("show-player-coordinates")
    val showPlayerArmor = plugin.config.getBoolean("show-player-armor")

    if (player.player != null) {
        val onlinePlayer = player.player!!
        ctx.json(PlayerResponse(
            uuid = onlinePlayer.uniqueId,
            name = onlinePlayer.name,
            health = onlinePlayer.health,
            foodLevel = onlinePlayer.foodLevel,
            location = if (showPlayerCoordinates) onlinePlayer.location else null,
            armor = if (showPlayerArmor) Armor.fromArmorContents(onlinePlayer.inventory.armorContents) else null,
            firstPlayed = onlinePlayer.firstPlayed,
            lastPlayed = onlinePlayer.lastPlayed,
            online = onlinePlayer.isOnline
        ))
    } else {
        val worldDataFolder = plugin.server.worlds.first().worldFolder
        val playerFile = File(worldDataFolder, "playerdata/" + player.uniqueId + ".dat")
        if (!playerFile.exists()) throw NotFoundResponse("NBT file not found for uuid ${player.uniqueId}")
        val playerNBT = NBT.readFile(playerFile)

        ctx.json(PlayerResponse(
            uuid = player.uniqueId,
            name = player.name!!,
            health = playerNBT.getDouble("Health"),
            foodLevel = playerNBT.getInteger("foodLevel"),
            location = if (showPlayerCoordinates) player.location!! else null,
            armor = if (showPlayerArmor) Armor.fromNBTInventory(playerNBT.getCompound("equipment")) else null,
            firstPlayed = player.firstPlayed,
            lastPlayed = player.lastPlayed,
            online = player.isOnline
        ))
    }
}