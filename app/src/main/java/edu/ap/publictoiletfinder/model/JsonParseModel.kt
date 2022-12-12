

import com.beust.klaxon.*
import java.io.Serializable

private fun <T> Klaxon.convert(k: kotlin.reflect.KClass<*>, fromJson: (JsonValue) -> T, toJson: (T) -> String, isUnion: Boolean = false) =
    this.converter(object: Converter {
        @Suppress("UNCHECKED_CAST")
        override fun toJson(value: Any)        = toJson(value as T)
        override fun fromJson(jv: JsonValue)   = fromJson(jv) as Any
        override fun canConvert(cls: Class<*>) = cls == k.java || (isUnion && cls.superclass == k.java)
    })

private val klaxon = Klaxon()

data class JsonParseModel (
    val displayFieldName: String,
    val fieldAliases: FieldAliases,
    val geometryType: String,
    val spatialReference: SpatialReference,
    val fields: List<Any?>,
    val features: List<Feature>
) {
    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = klaxon.parse<JsonParseModel>(json)
    }
}

data class Feature (
    val attributes: Attributes,
    val geometry: Geometry
)

data class Attributes (
    @Json(name = "ID")
    val id: Int,

    @Json(name = "STRAAT", serializeNull = false)
    val straat: String? = null,

    @Json(name = "HUISNUMMER", serializeNull = false)
    val huisnummer: String? = null,

    @Json(name = "INTEGRAAL_TOEGANKELIJK", serializeNull = false)
    val integraalToegankelijk: String? = null,

    @Json(name = "LUIERTAFEL", serializeNull = false)
    val luiertafel: String? = null,

    @Json(name = "DOELGROEP", serializeNull = false)
    val doelgroep: String? = null,

    var xCoord: Double? = null,
    var yCoord: Double? = null,
    var isAvailable: Boolean = true
) : Serializable

data class Geometry (
    val x: Double,
    val y: Double
)

data class FieldAliases (
    @Json(name = "ID", serializeNull = false)
    val id: String? = null,

    @Json(name = "STRAAT", serializeNull = false)
    val straat: String? = null,

    @Json(name = "HUISNUMMER", serializeNull = false)
    val huisnummer: String? = null,

    @Json(name = "INTEGRAAL_TOEGANKELIJK", serializeNull = false)
    val integraalToegankelijk: String? = null,

    @Json(name = "LUIERTAFEL", serializeNull = false)
    val luiertafel: String? = null,

    @Json(name = "DOELGROEP", serializeNull = false)
    val doelgroep: String? = null
)

data class SpatialReference (
    val wkid: Long,
    val latestWkid: Long
)
