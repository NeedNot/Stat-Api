package net.neednot.statApi.serializers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.bukkit.Location

class LocationSerializer: JsonSerializer<Location>() {
    override fun serialize(location: Location, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()
        gen.writeNumberField("x", location.x)
        gen.writeNumberField("y", location.y)
        gen.writeNumberField("z", location.z)
        gen.writeStringField("world", location.world?.environment?.name)
        gen.writeEndObject()
    }
}