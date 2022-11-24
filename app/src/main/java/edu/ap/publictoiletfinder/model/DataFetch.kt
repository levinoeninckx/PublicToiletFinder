package edu.ap.publictoiletfinder.model

import codebeautify.Attributes
import codebeautify.JsonParseModel
import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser
import okhttp3.*
import java.io.IOException
import java.net.URL
import java.util.concurrent.ExecutorService

class DataFetch {
    companion object {
        fun getToiletList(executorService: ExecutorService): List<Attributes> {
            var toiletList: MutableList<Attributes> = mutableListOf()
            var parseModel: JsonParseModel
            val klaxon = Klaxon();
            val parser: Parser = Parser.default()

            executorService.execute {
                val url =
                    URL("https://geodata.antwerpen.be/arcgissql/rest/services/P_Portal/portal_publiek1/MapServer/8/query?where=1%3D1&outFields=ID,STRAAT,HUISNUMMER,DOELGROEP,LUIERTAFEL,LAT,LONG,POSTCODE&returnGeometry=false&outSR=4326&f=json")
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

                            val jsonObject = JsonParseModel.fromJson(json)

                            //parseModel = JsonParseModel.fromJson(json)!!

                            //parseModel.features.forEach { feature -> toiletList.add(jsonParseDataFromString(feature.attributes.toString())) }
                        }
                    }
                })
            }
            return toiletList
        }
    }
}