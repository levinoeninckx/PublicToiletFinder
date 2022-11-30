package Data

import Attributes
import android.content.ContentValues
import android.util.Log
import android.database.sqlite.SQLiteDatabase
import edu.ap.publictoiletfinder.model.DataFetcher
import okhttp3.*
import java.net.URL

class SqlLite constructor(val db: SQLiteDatabase, val url: URL){
    lateinit var toiletList: ArrayList<Attributes>
    var dict:List<HashMap<String, Any>> = mutableListOf()

    private fun insertObject(obj: Attributes){
        val values = ContentValues()

        values.put("ID",  obj.id)
        values.put("STRAAT", obj.straat)
        values.put("HUISNUMMER", obj.huisnummer)
        values.put("DOELGROEP", obj.doelgroep)
        values.put("LUIERTAFEL", obj.luiertafel)
        values.put("LATITUDE", obj.xCoord)
        values.put("LONGITUDE", obj.yCoord)
        db.insert("Toilets", null, values)
    }

    fun createTable(){
        var createTable = "CREATE TABLE IF NOT EXISTS PublicToilets (\n" +
                "\tID INTEGER PRIMARY KEY UNIQUE NOT NULL,\n" +
                "\tSTRAAT TEXT,\n" +
                "\tHUISNUMMER TEXT,\n" +
                "\tDOELGROEP TEXT,\n" +
                "\tLUIERTAFEL TEXT,\n" +
                "\tLATITUDE DOUBLE NOT NULL,\n" +
                "\tLONGITUDE DOUBLE NOT NULL\n" +
                ");"
        db.execSQL(createTable)
    }
    fun fill(){
        createTable()
            toiletList.forEach {
                insertObject(it)
            }
    }
    fun initDict() {
        createTable()
        val cursor = db.rawQuery("SELECT * FROM PublicToilets", null)
        val colNames = cursor.columnNames
        var count = 0
        if (cursor.moveToFirst()) {
            do {
                (dict as ArrayList<HashMap<String, Any>>).add(HashMap<String, Any>())
                for (i in 1..colNames.count() - 1) {
                    val data: String = cursor.getString(i)
                    dict[count][colNames[i]] = data
                }
                count += 1
            } while (cursor.moveToNext())
        }
    }
    fun getData(){
        val dataFetcher = DataFetcher()
        dataFetcher.getJsonObject()
        toiletList = dataFetcher.toiletList
    }
}
