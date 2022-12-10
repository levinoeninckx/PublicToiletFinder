// To parse the JSON, install Klaxon and do:
//
//   val welcome6 = Welcome6.fromJson(jsonString)

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
    val fields: List<Field>,
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

data class Attributes(
    @Json(name = "ID", serializeNull = false)
    val id: Int,

    @Json(name = "STRAAT", serializeNull = false)
    val straat: String? = null,

    @Json(name = "HUISNUMMER",serializeNull = false)
    val huisnummer: String,

    @Json(name = "DOELGROEP",serializeNull = false)
    val doelgroep: String? = null,

    @Json(name = "INTEGRAAL_TOEGANKELIJK", serializeNull = false)
    val integraal_toegankelijk: String? = null,

    @Json(name = "X_COORD",serializeNull = false)
    val xCoord: Double? = null,

    @Json(name = "Y_COORD",serializeNull = false)
    val yCoord: Double? = null,

    @Json(name = "LUIERTAFEL",serializeNull = false)
    val luiertafel: String? = null
) : Serializable {
}

data class Geometry (
    val x: Double,
    val y: Double
)

data class FieldAliases (
    @Json(name = "ID", serializeNull = false)
    val id: String?,

    @Json(name = "STRAAT", serializeNull = false)
    val straat: String?,

    @Json(name = "HUISNUMMER", serializeNull = false)
    val huisnummer: String?,

    @Json(name = "DOELGROEP", serializeNull = false)
    val doelgroep: String?,

    @Json(name = "X_COORD", serializeNull = false)
    val xCoord: String?,

    @Json(name = "Y_COORD", serializeNull = false)
    val yCoord: String?,

    @Json(name = "POSTCODE", serializeNull = false)
    val postcode: String?,

    @Json(name = "LUIERTAFEL", serializeNull = false)
    val luiertafel: String?
)

data class Field (
    val name: String,
    val type: String,
    val alias: String,
    val length: Long? = null
)

data class SpatialReference (
    val wkid: Long,
    val latestWkid: Long
)
