// To parse the JSON, install Klaxon and do:
//
//   val welcome3 = Welcome3.fromJson(jsonString)


import com.beust.klaxon.*

private fun <T> Klaxon.convert(k: kotlin.reflect.KClass<*>, fromJson: (JsonValue) -> T, toJson: (T) -> String, isUnion: Boolean = false) =
    this.converter(object: Converter {
        @Suppress("UNCHECKED_CAST")
        override fun toJson(value: Any)        = toJson(value as T)
        override fun fromJson(jv: JsonValue)   = fromJson(jv) as Any
        override fun canConvert(cls: Class<*>) = cls == k.java || (isUnion && cls.superclass == k.java)
    })

private val klaxon = Klaxon()
    .convert(JsonObject::class, { it.obj!! },                          { it.toJsonString() })
    .convert(Betalend::class,   { Betalend.fromValue(it.string!!) },   { "\"${it.value}\"" })
    .convert(Doelgroep::class,  { Doelgroep.fromValue(it.string!!) },  { "\"${it.value}\"" })
    .convert(Gescreend::class,  { Gescreend.fromValue(it.string!!) },  { "\"${it.value}\"" })
    .convert(Luiertafel::class, { Luiertafel.fromValue(it.string!!) }, { "\"${it.value}\"" })

data class JsonParseModel (
    val displayFieldName: String,
    val fieldAliases: FieldAliases,
    val fields: List<Any?>,
    val features: List<Feature>
) {
    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = klaxon.parse<JsonParseModel>(json)
    }
}
data class Feature (
    val attributes: Toilet,
    val geometry: Geometry
)

data class Geometry (
    val x: Double,
    val y: Double
)

data class Toilet (
    @Json(name = "ID")
    val id: Long,

    @Json(name = "VRIJSTAAND")
    val vrijstaand: Betalend? = null,

    @Json(name = "BETALEND")
    val betalend: Betalend? = null,

    @Json(name = "STRAAT")
    val straat: String? = null,

    @Json(name = "HUISNUMMER")
    val huisnummer: String,

    @Json(name = "POSTCODE")
    val postcode: Long? = null,

    @Json(name = "DISTRICT")
    val district: String? = null,

    @Json(name = "INTEGRAAL_TOEGANKELIJK")
    val integraalToegankelijk: Betalend? = null,

    @Json(name = "GESCREEND")
    val gescreend: Gescreend? = null,

    @Json(name = "LUIERTAFEL")
    val luiertafel: Luiertafel? = null,

    @Json(name = "OPENINGSUREN_OPM")
    val openingsurenOpm: String? = null,

    @Json(name = "LAT")
    val lat: String? = null,

    @Json(name = "LONG")
    val long: String? = null,

    @Json(name = "X_COORD")
    val xCoord: Double? = null,

    @Json(name = "Y_COORD")
    val yCoord: Double? = null,

    @Json(name = "CONTACTPERSOON")
    val contactpersoon: String? = null,

    @Json(name = "CONTACTGEGEVENS")
    val contactgegevens: String? = null,

    @Json(name = "DOELGROEP")
    val doelgroep: Doelgroep? = null
)


enum class Betalend(val value: String) {
    Ja("ja"),
    Nee("nee");

    companion object {
        public fun fromValue(value: String): Betalend = when (value) {
            "ja"  -> Ja
            "nee" -> Nee
            else  -> throw IllegalArgumentException()
        }
    }
}

enum class Doelgroep(val value: String) {
    Man("man"),
    ManVrouw("man/vrouw");

    companion object {
        public fun fromValue(value: String): Doelgroep = when (value) {
            "man"       -> Man
            "man/vrouw" -> ManVrouw
            else        -> throw IllegalArgumentException()
        }
    }
}

enum class Gescreend(val value: String) {
    Ja("ja"),
    NietVanToepassing("niet van toepassing");

    companion object {
        public fun fromValue(value: String): Gescreend = when (value) {
            "ja"                  -> Ja
            "niet van toepassing" -> NietVanToepassing
            else                  -> throw IllegalArgumentException()
        }
    }
}

enum class Luiertafel(val value: String) {
    Nee("nee"),
    NietVanToepassing("niet van toepassing");

    companion object {
        public fun fromValue(value: String): Luiertafel = when (value) {
            "nee"                 -> Nee
            "niet van toepassing" -> NietVanToepassing
            else                  -> throw IllegalArgumentException()
        }
    }
}

typealias FieldAliases = JsonObject
