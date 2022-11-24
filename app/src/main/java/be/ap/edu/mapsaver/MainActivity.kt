package be.ap.edu.mapsaver

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedOverlay
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.OverlayItem
import java.io.File
import java.net.URL
import java.net.URLEncoder
import java.util.*

class MainActivity : Activity() {

    private lateinit var mMapView: MapView
    private var mMyLocationOverlay: ItemizedOverlay<OverlayItem>? = null
    private var items = ArrayList<OverlayItem>()
    private var searchField: EditText? = null
    private var searchButton: Button? = null
    private var clearButton: Button? = null
    private val urlNominatim = "https://nominatim.openstreetmap.org/"
    private var notificationManager: NotificationManager? = null
    private var mChannel: NotificationChannel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Problem with SQLite db, solution :
        // https://stackoverflow.com/questions/40100080/osmdroid-maps-not-loading-on-my-device
        val osmConfig = Configuration.getInstance()
        osmConfig.userAgentValue = packageName
        val basePath = File(cacheDir.absolutePath, "osmdroid")
        osmConfig.osmdroidBasePath = basePath
        val tileCache = File(osmConfig.osmdroidBasePath, "tile")
        osmConfig.osmdroidTileCache = tileCache

        setContentView(R.layout.activity_main)
        mMapView = findViewById(R.id.mapview)

        searchField = findViewById(R.id.search_txtview)
        searchButton = findViewById(R.id.search_button)
        searchButton?.setOnClickListener {
            val url = URL(urlNominatim + "search?q=" + URLEncoder.encode(searchField?.text.toString(), "UTF-8") + "&format=json")
            it.hideKeyboard()
            //val task = MyAsyncTask()
            //task.execute(url)
            getAddressOrLocation(url)
        }

        clearButton = findViewById(R.id.clear_button)
        clearButton?.setOnClickListener {
            mMapView?.overlays?.clear()
            // Redraw map
            mMapView?.invalidate()
        }
         // Permissions
        if (hasPermissions()) {
            initMap()
        }
        else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION), 100)
        }
        // Notifications
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        mChannel = NotificationChannel("my_channel_01","My Channel", NotificationManager.IMPORTANCE_HIGH)
        mChannel?.setShowBadge(true)
        //mChannel?.enableLights(true)
        //mChannel?.enableVibration(true)
        //mChannel?.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)

        notificationManager?.createNotificationChannel(mChannel!!)
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun hasPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (hasPermissions()) {
                initMap()
            } else {
                finish()
            }
        }
    }

    private fun initMap() {
        mMapView?.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        // add receiver to get location from tap
        val mReceive: MapEventsReceiver = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                //Toast.makeText(baseContext, p.latitude.toString() + " - " + p.longitude, Toast.LENGTH_LONG).show()
                val url = URL(urlNominatim + "reverse?lat=" + p.latitude.toString() + "&lon=" + p.longitude.toString() + "&format=json")
                //val task = MyAsyncTask()
                //task.execute(url)
                getAddressOrLocation(url)
                return false
            }

            override fun longPressHelper(p: GeoPoint): Boolean {
                return false
            }
        }
        mMapView?.overlays?.add(MapEventsOverlay(mReceive))

         // MiniMap
        //val miniMapOverlay = MinimapOverlay(this, mMapView!!.tileRequestCompleteHandler)
        //this.mMapView?.overlays?.add(miniMapOverlay)

        mMapView?.controller?.setZoom(17.0)
        // default = Ellermanstraat 33
        setCenter(GeoPoint(51.23020595, 4.41655480828479), "Campus Ellermanstraat")
    }

    private fun addMarker(geoPoint: GeoPoint, name: String) {
        items.add(OverlayItem(name, name, geoPoint))
        mMyLocationOverlay = ItemizedIconOverlay(items, null, applicationContext)
        mMapView?.overlays?.add(mMyLocationOverlay)
    }

    private fun setCenter(geoPoint: GeoPoint, name: String) {
        mMapView?.controller?.setCenter(geoPoint)
        addMarker(geoPoint, name)
    }

    fun createNotification(iconRes: Int, title: String, body: String, channelId: String) {
        notificationManager?.createNotificationChannel(mChannel!!)
        val notification: Notification = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(iconRes)
                .setContentTitle(title)
                .setContentText(body)
                .build()

        notificationManager?.notify(0, notification)
    }

    override fun onPause() {
        super.onPause()
        mMapView?.onPause()
    }

    override fun onResume() {
        super.onResume()
        mMapView?.onResume()
    }

    private fun getAddressOrLocation(url : URL) {

        var searchReverse = false

        Thread(Runnable {
            searchReverse = (url.toString().indexOf("reverse", 0, true) > -1)
            val client = OkHttpClient()
            val response: Response
            val request = Request.Builder()
                .url(url)
                .build()
            response = client.newCall(request).execute()

            val result = response.body!!.string()

            runOnUiThread {
                val jsonString = StringBuilder(result!!)
                Log.d("be.ap.edu.mapsaver", jsonString.toString())

                val parser: Parser = Parser.default()

                if (searchReverse) {
                    val obj = parser.parse(jsonString) as JsonObject

                    createNotification(R.drawable.ic_menu_compass,
                        "Reverse lookup result",
                        obj.string("display_name")!!,
                        "my_channel_01")
                }
                else {
                    val array = parser.parse(jsonString) as JsonArray<JsonObject>

                    if (array.size > 0) {
                        val obj = array[0]
                        // mapView center must be updated here and not in doInBackground because of UIThread exception
                        val geoPoint = GeoPoint(obj.string("lat")!!.toDouble(), obj.string("lon")!!.toDouble())
                        setCenter(geoPoint, obj.string("display_name")!!)
                    }
                    else {
                        Toast.makeText(applicationContext, "Address not found", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }).start()
    }

    // AsyncTask inner class
    /*@SuppressLint("StaticFieldLeak")
    inner class MyAsyncTask : AsyncTask<URL, Int, String>() {

        private var searchReverse = false

        override fun doInBackground(vararg params: URL?): String {

            searchReverse = (params[0]!!.toString().indexOf("reverse", 0, true) > -1)
            val client = OkHttpClient()
            val response: Response
            val request = Request.Builder()
                    .url(params[0]!!)
                    .build()
            response = client.newCall(request).execute()

            return response.body!!.string()
        }

        // vararg : variable number of arguments
        // * : spread operator, unpacks an array into the list of values from the array
        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            val jsonString = StringBuilder(result!!)
            Log.d("be.ap.edu.mapsaver", jsonString.toString())

            val parser: Parser = Parser.default()

            if (searchReverse) {
                val obj = parser.parse(jsonString) as JsonObject

                createNotification(R.drawable.ic_menu_compass,
                        "Reverse lookup result",
                        obj.string("display_name")!!,
                        "my_channel_01")
            }
            else {
                val array = parser.parse(jsonString) as JsonArray<JsonObject>

                if (array.size > 0) {
                    val obj = array[0]
                    // mapView center must be updated here and not in doInBackground because of UIThread exception
                    val geoPoint = GeoPoint(obj.string("lat")!!.toDouble(), obj.string("lon")!!.toDouble())
                    setCenter(geoPoint, obj.string("display_name")!!)
                }
                else {
                    Toast.makeText(applicationContext, "Address not found", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }*/
}
