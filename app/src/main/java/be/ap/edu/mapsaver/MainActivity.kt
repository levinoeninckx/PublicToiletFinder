package be.ap.edu.mapsaver

import Attributes
import Data.DataBaseHelper
import Toilet
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import be.ap.edu.mapsaver.fragments.MapViewFragment
import be.ap.edu.mapsaver.fragments.ToiletListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.reflect.typeOf


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

        //Get filter buttons
        val fabFilterMan = findViewById<FloatingActionButton>(R.id.filter_men)
        val fabFilterWoman = findViewById<FloatingActionButton>(R.id.filter_woman)
        val fabFilterWheelchair = findViewById<FloatingActionButton>(R.id.filter_wheelchair)
        val fabFilterChangingstation = findViewById<FloatingActionButton>(R.id.filter_changingstation)

        var filteredOnMan = false
        var filteredOnWoman = false
        var filteredOnWheelchair = false
        var filteredOnChangingstation = false

        var filteredToiletList = ArrayList<Toilet>()

        val listener = View.OnClickListener {view ->
            when(view.id){
                R.id.filter_men -> {
                    filteredOnMan = !filteredOnMan
                }
                R.id.filter_woman -> {
                    filteredOnWoman = !filteredOnWoman
                }
                R.id.filter_changingstation -> {
                    filteredOnChangingstation = !filteredOnChangingstation
                }
                R.id.filter_wheelchair -> {
                    filteredOnWheelchair = !filteredOnWheelchair
                }
            }
            filteredToiletList = ArrayList()
            if(filteredOnMan) {
                fabFilterMan.setBackgroundColor(Color.RED)
                filteredToiletList += toiletList.filter {
                    it.doelgroep == "M/V" || it.doelgroep == "M"
                } as ArrayList<Toilet>
            }
            if(filteredOnWoman) {
                fabFilterWoman.setBackgroundColor(Color.RED)
                filteredToiletList += toiletList.filter {
                    it.doelgroep == "M/V" || it.doelgroep == "V"
                } as ArrayList<Toilet>
            }
            if(filteredOnChangingstation) {
                fabFilterChangingstation.setBackgroundColor(Color.RED)
                filteredToiletList += toiletList.filter {
                    it.luiertafel == "ja"
                } as ArrayList<Toilet>
            }
            if(filteredOnWheelchair) {
                fabFilterWheelchair.setBackgroundColor(Color.RED)
                filteredToiletList += toiletList.filter {
                    it.integraalToegankelijk == "ja"
                } as ArrayList<Toilet>
            }
            if(filteredToiletList.isEmpty()){
                filteredToiletList = toiletList
            }
            refreshFragment(filteredToiletList)
        }
        fabFilterMan.setOnClickListener(listener)
        fabFilterWoman.setOnClickListener(listener)
        fabFilterChangingstation.setOnClickListener(listener)
        fabFilterWheelchair.setOnClickListener(listener)

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
    private fun refreshFragment(updatedToiletList: ArrayList<Toilet>){
        var fragment = supportFragmentManager.findFragmentById(R.id.container)!!

        if(fragment is MapViewFragment){
            fragment = fragment as MapViewFragment
            fragment.toiletList = updatedToiletList
        }
        if(fragment is ToiletListFragment){
            fragment.toiletList = updatedToiletList
        }
        supportFragmentManager.beginTransaction()
            .detach(fragment)
            .commit()
        supportFragmentManager.beginTransaction()
            .attach(fragment)
            .commit()
    }
}