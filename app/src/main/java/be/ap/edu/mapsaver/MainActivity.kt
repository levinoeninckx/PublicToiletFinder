package be.ap.edu.mapsaver

import Attributes
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

        //Init toiletlist
        var toiletList = database.getToiletList()

        //Update if database is empty
        if(database.getToiletList().isEmpty()){
            database.fetchData()
        }

        setContentView(R.layout.activity_main)

        val transaction = supportFragmentManager.beginTransaction()
        val mapView = MapViewFragment(toiletList)
        transaction.add(R.id.container, mapView)
        transaction.commit()

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        //Filter
        toiletList = toiletList.filter{it.integraalToegankelijk == "ja"} as ArrayList<Attributes>
        bottomNav.setOnNavigationItemSelectedListener  {
            when(it.itemId){
                R.id.map -> {
                    loadFragment(MapViewFragment(toiletList))
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.list -> {
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