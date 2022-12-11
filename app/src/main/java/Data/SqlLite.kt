package Data

import Attributes
import android.content.ContentValues
import android.util.Log
import android.database.sqlite.SQLiteDatabase
import edu.ap.publictoiletfinder.model.DataFetcher
import okhttp3.*
import org.osmdroid.util.GeoPoint
import java.net.URL

class SqlLite constructor(val db: SQLiteDatabase){
    lateinit var toiletList: ArrayList<Attributes>
    lateinit var gepointList: ArrayList<GeoPoint>

    private fun insertObject(obj: Attributes){
        val values = ContentValues()
        values.put("ID",  obj.id)
        values.put("STRAAT", obj.straat)
        values.put("HUISNUMMER", obj.huisnummer)
        values.put("DOELGROEP", obj.doelgroep)
        values.put("LUIERTAFEL", obj.luiertafel)
        values.put("INTEGRAAL_TOEGANKELIJK", obj.integraal_toegankelijk)
        values.put("LATITUDE", obj.xCoord)
        values.put("LONGITUDE", obj.yCoord)
        values.put("AVAILABILITY", obj.isAvailable)
        db.insert("PublicToilets", null, values)
    }
    fun createTable(){
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
        db.execSQL(createTable)
    }
    fun fill(){
        createTable()
            toiletList.forEach {
                insertObject(it)
            }
    }
    fun initList() {
        createTable()
        val cursor = db.rawQuery("SELECT * FROM PublicToilets", null)
        gepointList = arrayListOf()
        if (cursor.moveToFirst()) {
            do {
                try {
                    gepointList.add(GeoPoint(cursor.getString(7).toDouble(),cursor.getString(6).toDouble()))
                } catch(e: Exception){
                    e.printStackTrace()
                }
            } while (cursor.moveToNext())
        }
    }
    fun getData(){
        val dataFetcher = DataFetcher()
        dataFetcher.getJsonObject()
        toiletList = dataFetcher.toiletList
    }
    fun retrieveDataAsList(): List<Attributes> {
        val cursor = db.rawQuery("SELECT * FROM PublicToilets", null)

        if (cursor.moveToFirst()) {
            do {
                try {
                    toiletList.add(
                        Attributes(
                            cursor.getString(0).toInt(),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            luiertafel = cursor.getString(5)
                        )
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } while (cursor.moveToNext())
        }
        return toiletList
    }
}
