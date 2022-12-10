package be.ap.edu.mapsaver.fragments

import Attributes
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import be.ap.edu.mapsaver.R

class ToiletDetailDialog: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_toilet_details, container, false)

        val item = arguments?.getSerializable("item") as Attributes

        if(item != null) {

            view.findViewById<TextView>(R.id.toilet_street).text = item.straat
            view.findViewById<TextView>(R.id.toilet_housenumber).text = item.huisnummer
            if (item.integraal_toegankelijk != null) {
                view.findViewById<TextView>(R.id.toilet_rolstoelvriendelijk).text =
                    item.integraal_toegankelijk
            }
            if (item.luiertafel != null) {
                view.findViewById<TextView>(R.id.toilet_vervangtafel).text = item.luiertafel
            }
            view.findViewById<TextView>(R.id.toilet_rolstoelvriendelijk).text = item.integraal_toegankelijk
            view.findViewById<TextView>(R.id.toilet_vervangtafel).text = "/"
        }
        return view
    }
}