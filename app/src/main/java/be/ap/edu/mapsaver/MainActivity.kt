package be.ap.edu.mapsaver

import Data.DataBaseHelper
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import be.ap.edu.mapsaver.fragments.MapViewFragment
import be.ap.edu.mapsaver.fragments.ToiletListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initialize database
        val database = DataBaseHelper(applicationContext)

        //Update if database is empty
        if(database.getToiletList().isEmpty()){
            database.fetchData()
        }

        setContentView(R.layout.activity_main)

        val transaction = supportFragmentManager.beginTransaction()
        val geopointList = database.getGeoPoints()
        val mapView = MapViewFragment(geopointList)
        transaction.add(R.id.container, mapView)
        transaction.commit()

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNav.setOnNavigationItemSelectedListener  {
            when(it.itemId){
                R.id.map -> {
                    val geopointList = database.getGeoPoints()
                    loadFragment(MapViewFragment(geopointList))
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.list -> {
                    val toiletList = database.getToiletList()
                    loadFragment(ToiletListFragment(toiletList))
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}