package net.neednot.statApi.serializers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable

class ItemStackSerializer: JsonSerializer<ItemStack>() {
    override fun serialize(item: ItemStack, gen: JsonGenerator, serializers: SerializerProvider) {
        gen.writeStartObject()
        gen.writeStringField("type", item.type.name)
        gen.writeNumberField("amount", item.amount)
        item.itemMeta?.let { meta ->
            gen.writeObjectFieldStart("metaData")
            if (meta.hasItemName()) gen.writeStringField("itemName", meta.itemName)
            if (meta.hasDisplayName()) gen.writeStringField("displayName", meta.displayName)
            (meta as? Damageable)?.let { damageable ->
                gen.writeNumberField("damage", damageable.damage)
            }
            if (meta.hasEnchants()) {
                gen.writeArrayFieldStart("enchantments")
                meta.enchants.forEach { (enchantment, level) ->
                    gen.writeStartObject()
                    gen.writeStringField("key", enchantment.keyOrThrow.key)
                    gen.writeNumberField("level", level)
                    gen.writeEndObject()
                }
                gen.writeEndArray()
            }
            if (meta.hasLore()) {
                gen.writeArrayFieldStart("lore")
                meta.lore!!.forEach { l -> gen.writeString(l) }
                gen.writeEndArray()
            }
            gen.writeEndObject()
        }

        gen.writeEndObject()
    }
}