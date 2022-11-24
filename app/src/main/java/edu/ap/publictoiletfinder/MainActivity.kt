package edu.ap.publictoiletfinder
import JsonParseModel
import Toilet
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.beust.klaxon.Klaxon
import okhttp3.*
import okhttp3.internal.wait
import java.io.IOException
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val executorService: ExecutorService = Executors.newFixedThreadPool(4)
        super.onCreate(savedInstanceState)

        val toiletList: List<Toilet> = ArrayList<Toilet>()
        val parseModel = getToiletList(executorService)

        setContentView(R.layout.activity_main)
    }

    private fun jsonParseDataFromString(json: String): Toilet {
        val klaxon = Klaxon();
        val result = klaxon.parse<Toilet>(json)!!
        return result
    }
    fun getToiletList(executorService: ExecutorService): List<Toilet> {
        var toiletList: MutableList<Toilet> = mutableListOf()
        var parseModel: JsonParseModel
        val klaxon = Klaxon();
        executorService.execute {
            val url =
                URL("https://geodata.antwerpen.be/arcgissql/rest/services/P_Portal/portal_publiek1/MapServer/8/query?where=1%3D1&outFields=ID,VRIJSTAAND,BETALEND,STRAAT,HUISNUMMER,POSTCODE,DISTRICT,INTEGRAAL_TOEGANKELIJK,GESCREEND,LUIERTAFEL,OPENINGSUREN_OPM,LAT,LONG,X_COORD,Y_COORD,CONTACTPERSOON,CONTACTGEGEVENS,DOELGROEP&outSR=4326&f=json")
            var client: OkHttpClient = OkHttpClient()
            val request: Request = Request.Builder().url(url).build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")

                        val json = response.body!!.string()

                        println(json)

                        parseModel = JsonParseModel.fromJson(json)!!

                        parseModel.features.forEach { feature -> toiletList.add(jsonParseDataFromString(feature.attributes.toString())) }
                    }
                }
            })
        }
        return toiletList
    }
}
