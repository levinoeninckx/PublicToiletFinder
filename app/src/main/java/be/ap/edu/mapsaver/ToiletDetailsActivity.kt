package be.ap.edu.mapsaver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ToiletDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toilet_details)

        val toiletStreet = intent.getStringExtra("toilet_street")
        val toiletHouseNumber = intent.getStringExtra("toilet_housenumber")
        val toiletGender = intent.getStringExtra("toilet_gender")
        val toiletChangingStation = intent.getStringExtra("toilet_vervangtafel")
        val toiletWheelChairAccess = intent.getStringExtra("toilet_rolstoelvriendlijk")

        val toiletStreetView = findViewById<TextView>(R.id.toilet_street)
        val toiletGenderView = findViewById<TextView>(R.id.toilet_genderlbl)
        val toiletHouseNumberView = findViewById<TextView>(R.id.toilet_housenumber)
        val toiletChangingStationView = findViewById<TextView>(R.id.toilet_vervangtafel)
        val toiletWheelChairAccessView = findViewById<TextView>(R.id.toilet_rolstoelvriendelijk)

        toiletGenderView.text = toiletGender
        toiletStreetView.text = toiletStreet
        toiletHouseNumberView.text = toiletHouseNumber
        toiletChangingStationView.text = toiletChangingStation
        toiletWheelChairAccessView.text = toiletWheelChairAccess
    }
}