package io.musicorum.api.realms.collages.themes.serialization

import io.musicorum.api.realms.collages.themes.GridTheme
import io.musicorum.api.realms.collages.themes.Theme
import io.musicorum.api.realms.collages.themes.ThemeEnum
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

object ThemeDataSerializer : KSerializer<ThemeData> {
    override val descriptor: SerialDescriptor = ThemeData.serializer().descriptor
    override fun deserialize(decoder: Decoder): ThemeData {
        val data = decoder.decodeSerializableValue(InputTheme.serializer())

        val r = when (data.name) {
            ThemeEnum.ClassicCollage -> ThemeData(data.name, Json.decodeFromJsonElement<GridTheme.GenerationData>(data.options))
        }

        println("END")

        return r
    }

    override fun serialize(encoder: Encoder, value: ThemeData) {
        TODO("Not yet implemented")
    }
}

@Serializable
internal data class InputTheme(
    val name: ThemeEnum,
    val options: JsonObject
)

@Serializable
data class ThemeData(
    val name: ThemeEnum,
    val options: Theme.IGenerationData
)

