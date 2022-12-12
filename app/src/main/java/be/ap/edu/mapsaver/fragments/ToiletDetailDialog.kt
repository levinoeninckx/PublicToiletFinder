package be.ap.edu.mapsaver.fragments

import Attributes
import Data.DataBaseHelper
import android.content.ContentValues
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import be.ap.edu.mapsaver.R

class ToiletDetailDialog: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_toilet_details_fragment, container, false)

        val item = arguments?.getSerializable("item") as Attributes

        if(item != null) {

            view.findViewById<TextView>(R.id.toilet_street).text = item.straat
            view.findViewById<TextView>(R.id.toilet_housenumber).text = item.huisnummer
            if (item.integraalToegankelijk != null) {
                view.findViewById<TextView>(R.id.toilet_rolstoelvriendelijk).text =
                    item.integraalToegankelijk
            }
            if (item.luiertafel != null) {
                view.findViewById<TextView>(R.id.toilet_vervangtafel).text = item.luiertafel
            }
            view.findViewById<TextView>(R.id.toilet_rolstoelvriendelijk).text = item.integraalToegankelijk
            view.findViewById<TextView>(R.id.toilet_vervangtafel).text = "/"


            val database = DataBaseHelper(requireActivity().applicationContext)
            val reportButton = view.findViewById<Button>(R.id.report_button)
            reportButton.setOnClickListener {
                item.isAvailable = !item.isAvailable
                if(item.isAvailable) {
                    reportButton.text = "Available"
                    reportButton.setBackgroundColor(Color.GREEN)
                }
                else{
                    reportButton.text = "Unavailable"
                    reportButton.setBackgroundColor(Color.RED)
                }
                val values = ContentValues()
                values.put("AVAILABILITY",item.isAvailable)
                database.update(item.id.toLong(),values)
            }
            if(item.isAvailable) {
                reportButton.text = "Available"
                reportButton.setBackgroundColor(Color.GREEN)
            }
            else{
                reportButton.text = "Unavailable"
                reportButton.setBackgroundColor(Color.RED)
            }
        }
        return view
    }
}