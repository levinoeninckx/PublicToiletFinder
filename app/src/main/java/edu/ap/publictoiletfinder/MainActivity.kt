package edu.ap.publictoiletfinder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import edu.ap.publictoiletfinder.model.Toilet
import java.io.File
import java.io.InputStream
import java.io.StringReader

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val toiletArray = ArrayList<Toilet>()
        val json = resources.openRawResource(R.raw.openbaar_toilet)


        jsonParseStream(json,toiletArray)


        setContentView(R.layout.activity_main)
    }
    private fun jsonParseStream(json: InputStream, toiletArray: ArrayList<Toilet>){
        val klaxon = Klaxon();
        JsonReader(StringReader(json)).use {
            reader -> reader.beginArray {
                while(reader.hasNext()) {
                    val toilet = klaxon.parse<Toilet>(reader)
                    toiletArray.add(toilet!!);
                }
        }
        }
    }
    private fun readFile(fileName: String) : String = File(fileName).readText(Charsets.UTF_8)
}