package be.ap.edu.mapsaver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ToiletDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toilet_details)

        val toiletStreet = intent.getStringExtra("toilet_street")
        val toiletHouseNumber = intent.getStringExtra("toilet_house_number")
        val toiletGender = intent.getStringExtra("toilet_gender")

        val toiletStreetView = findViewById<TextView>(R.id.toilet_street)
        val toiletGenderView = findViewById<TextView>(R.id.toilet_genderlbl)

        toiletGenderView.text = toiletGender
        toiletStreetView.text = toiletStreet
    }
}