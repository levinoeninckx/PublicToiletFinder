package edu.ap.publictoiletfinder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import codebeautify.Attributes
import codebeautify.JsonParseModel
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser
import edu.ap.publictoiletfinder.model.DataFetch
import okhttp3.*
import okhttp3.internal.wait
import org.json.JSONObject
import java.io.IOException
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val executorService: ExecutorService = Executors.newFixedThreadPool(4)
        super.onCreate(savedInstanceState)

        val parseModel = DataFetch.getToiletList(executorService)

        setContentView(R.layout.activity_main)
    }

    private fun jsonParseDataFromString(json: String): Attributes {
        val klaxon = Klaxon();
        val result = klaxon.parse<Attributes>(json)!!
        return result
    }
}
