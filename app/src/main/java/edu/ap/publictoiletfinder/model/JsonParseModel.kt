// To parse the JSON, install Klaxon and do:
//
//   val welcome9 = Welcome9.fromJson(jsonString)

package codebeautify

import com.beust.klaxon.*

private fun <T> Klaxon.convert(k: kotlin.reflect.KClass<*>, fromJson: (JsonValue) -> T, toJson: (T) -> String, isUnion: Boolean = false) =
    this.converter(object: Converter {
        @Suppress("UNCHECKED_CAST")
        override fun toJson(value: Any)        = toJson(value as T)
        override fun fromJson(jv: JsonValue)   = fromJson(jv) as Any
        override fun canConvert(cls: Class<*>) = cls == k.java || (isUnion && cls.superclass == k.java)
    })

private val klaxon = Klaxon()

data class JsonParseModel (
    val displayFieldName: String?,
    val fieldAliases: FieldAliases?,
    val fields: List<Any?>?,
    val features: List<Feature>?
) {
    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = klaxon.parse<JsonParseModel>(json)
    }
}

data class Feature (
    val attributes: Attributes
)

data class Attributes (
    @Json(name = "ID",serializeNull = false)
    val id: Long,

    @Json(name = "STRAAT", serializeNull = false)
    val straat: String?,

    @Json(name = "HUISNUMMER",serializeNull = false)
    val huisnummer: String,

    @Json(name = "DOELGROEP",serializeNull = false)
    val doelgroep: String?,

    @Json(name = "LUIERTAFEL",serializeNull = false)
    val luiertafel: String?,

    @Json(name = "LAT",serializeNull = false)
    val lat: String?,

    @Json(name = "LONG",serializeNull = false)
    val long: String?,

    @Json(name = "POSTCODE",serializeNull = false)
    val postcode: Long?
)

data class FieldAliases (
    @Json(name = "ID")
    val id: String,

    @Json(name = "STRAAT")
    val straat: String,

    @Json(name = "HUISNUMMER")
    val huisnummer: String,

    @Json(name = "DOELGROEP")
    val doelgroep: String,

    @Json(name = "LUIERTAFEL")
    val luiertafel: String,

    @Json(name = "LAT")
    val lat: String,

    @Json(name = "LONG")
    val long: String,

    @Json(name = "POSTCODE")
    val postcode: String
)
