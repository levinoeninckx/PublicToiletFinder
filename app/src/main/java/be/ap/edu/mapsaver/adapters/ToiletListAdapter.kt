package be.ap.edu.mapsaver.adapters

import Attributes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import be.ap.edu.mapsaver.R
import kotlinx.android.synthetic.main.toilet_item.view.*

class ToiletListAdapter(var dataSet: List<Attributes>): RecyclerView.Adapter<ToiletListAdapter.ToiletListViewHolder>(){
    class ToiletListViewHolder(val view: View): RecyclerView.ViewHolder(view), View.OnClickListener{

        val toiletId: TextView = view.findViewById(R.id.toilet_gender)
        val toiletStreet: TextView = view.findViewById(R.id.toilet_street)

        override fun onClick(p0: View?) {

        }

        init {
            view.setOnClickListener(this)
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToiletListViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.toilet_item, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return ToiletListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToiletListViewHolder, position: Int) {
        if(dataSet[position].doelgroep == null){
            holder.view.toilet_gender.text = "Genderneutraal"
        } else {
            holder.view.toilet_gender.text = dataSet[position].doelgroep
        }
        if(dataSet[position].straat != null){
            holder.view.toilet_street.text = dataSet[position].straat
        }
    }

    override fun getItemCount() = dataSet.count()

}