package Data

import Attributes
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import okhttp3.*
import okhttp3.internal.wait
import org.osmdroid.util.GeoPoint
import java.io.IOException
import java.net.URL

class DataBaseHelper(context: Context): SQLiteOpenHelper(context,"Toilets",null,1) {
    private lateinit var geopointList: ArrayList<GeoPoint>
    private lateinit var toiletList: ArrayList<Attributes>

    override fun onCreate(db: SQLiteDatabase?) {
        var createTable = "CREATE TABLE IF NOT EXISTS PublicToilets (\n" +
                "\tID INTEGER PRIMARY KEY UNIQUE NOT NULL,\n" +
                "\tSTRAAT TEXT,\n" +
                "\tHUISNUMMER TEXT,\n" +
                "\tDOELGROEP TEXT,\n" +
                "\tINTEGRAAL_TOEGANKELIJK TEXT,\n" +
                "\tLUIERTAFEL TEXT,\n" +
                "\tLATITUDE DOUBLE NOT NULL,\n" +
                "\tLONGITUDE DOUBLE NOT NULL,\n" +
                "\tAVAILABILITY BOOL NOT NULL\n" +
                ");"
        db?.execSQL(createTable)

        //Fetch data from api and store it in local list
        fetchData()
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
    fun insert(obj: Attributes){
        val values = ContentValues()
        values.put("ID",  obj.id)
        values.put("STRAAT", obj.straat)
        values.put("HUISNUMMER", obj.huisnummer)
        values.put("DOELGROEP", obj.doelgroep)
        values.put("LUIERTAFEL", obj.luiertafel)
        values.put("INTEGRAAL_TOEGANKELIJK", obj.integraalToegankelijk)
        values.put("LATITUDE", obj.xCoord)
        values.put("LONGITUDE", obj.yCoord)
        values.put("AVAILABILITY", obj.isAvailable)
        writableDatabase.insert("PublicToilets", null, values)
    }
    fun update(id: Long, values: ContentValues){
        val selection = "id = ?"
        val selectionArgs = arrayOf(id.toString())
        writableDatabase.update("PublicToilets", values, selection, selectionArgs)
    }
    fun fillDatabase(toiletList: ArrayList<Attributes>){
        toiletList.forEach {
            insert(it)
        }
    }
    fun getGeoPoints(): ArrayList<GeoPoint> {
        val cursor = readableDatabase.rawQuery("SELECT * FROM PublicToilets", null)
        geopointList = arrayListOf()
        if (cursor.moveToFirst()) {
            do {
                try {
                    geopointList.add(GeoPoint(cursor.getString(6).toDouble(),cursor.getString(7).toDouble()))
                } catch(e: Exception){
                    e.printStackTrace()
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        return geopointList as ArrayList<GeoPoint>
    }
    private fun fetchData() {
        toiletList = arrayListOf<Attributes>()
            val url =
                URL("https://geodata.antwerpen.be/arcgissql/rest/services/P_Portal/portal_publiek1/MapServer/8/query?where=1%3D1&outFields=ID,POSTCODE,INTEGRAAL_TOEGANKELIJK,DOELGROEP,HUISNUMMER,STRAAT&outSR=4326&f=json")
            var client = OkHttpClient()
            val request: Request = Request.Builder().url(url).build()
        Thread {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }
                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")

                        val json = response.body!!.string()
                        println(json)
                        val jsonObject = JsonParseModel.fromJson(json)

                        jsonObject!!.features.forEach {
                            it.attributes.yCoord = it.geometry.x
                            it.attributes.xCoord = it.geometry.y
                            insert(it.attributes)
                        }
                    }
                }
            })
        }.start()
    }
    fun getToiletList(): ArrayList<Attributes>{
        toiletList = arrayListOf()
        val cursor = readableDatabase.rawQuery("SELECT * FROM PublicToilets", null)
        if (cursor.moveToFirst()) {
            do {
                try {
                    val toilet = Attributes(
                        cursor.getString(0).toInt(),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(3),
                        cursor.getString(6).toDouble(),
                        cursor.getString(7).toDouble(),
                    )
                    if(cursor.getString(8) == "0") toilet.isAvailable = false
                    toiletList.add(
                        toilet
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        return toiletList
    }
}