package edu.ap.publictoiletfinder.model

import Attributes
import JsonParseModel
import android.annotation.SuppressLint
import com.beust.klaxon.Klaxon
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.net.URL

class DataFetcher {
        lateinit var toiletList: ArrayList<Attributes>
        @SuppressLint("SuspiciousIndentation")
        fun getJsonObject(): ArrayList<Attributes> {
            toiletList = arrayListOf()
            Thread(Runnable {
                val url =
                    URL("https://geodata.antwerpen.be/arcgissql/rest/services/P_Portal/portal_publiek1/MapServer/8/query?where=1%3D1&outFields=ID,POSTCODE,X_COORD,Y_COORD,INTEGRAAL_TOEGANKELIJK,DOELGROEP,HUISNUMMER,STRAAT&outSR=4326&f=json")
                var client = OkHttpClient()
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

                            jsonObject!!.features.forEach {
                                val attributes = Attributes(
                                    it.attributes.id,
                                    it.attributes.straat,
                                    it.attributes.huisnummer,
                                    it.attributes.doelgroep,
                                    it.attributes.integraal_toegankelijk,
                                    it.geometry.x,
                                    it.geometry.y,
                                    it.attributes.postcode,
                                    it.attributes.luiertafel
                                )
                                toiletList.add(attributes)
                                println(attributes.id)
                            }
                        }
                    }
                })
            }).start()
            return toiletList
        }
}