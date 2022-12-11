package be.ap.edu.mapsaver

import Attributes
import Data.DataBaseHelper
import Data.SqlLite
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import be.ap.edu.mapsaver.adapters.ToiletListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ToiletListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toilet_list)

        val dataBaseHelper = DataBaseHelper(applicationContext)
        val toiletList = dataBaseHelper.getToiletList()

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(baseContext, MainActivity::class.java)
            startActivity(intent)
        }

        // Get a handle to the RecyclerView.
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)

        // Create an adapter and supply the data to be displayed.
        val adapter = ToiletListAdapter(this,applicationContext,toiletList)

        // Connect the adapter with the RecyclerView.
        recyclerView.adapter = adapter

        // Give the RecyclerView a default layout manager.
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}