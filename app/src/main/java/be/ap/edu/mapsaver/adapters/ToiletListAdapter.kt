package be.ap.edu.mapsaver.adapters

import Attributes
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import be.ap.edu.mapsaver.R
import be.ap.edu.mapsaver.ToiletDetailsActivity
import kotlinx.android.synthetic.main.toilet_item.view.*

class ToiletListAdapter(val context: Context, var dataSet: List<Attributes>): RecyclerView.Adapter<ToiletListAdapter.ToiletListViewHolder>(){
    class ToiletListViewHolder(val view: View): RecyclerView.ViewHolder(view)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToiletListViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.toilet_item, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return ToiletListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToiletListViewHolder, position: Int) {
        val item = dataSet[position]
        if(dataSet[position].doelgroep == null){
            holder.view.toilet_genderlbl.text = "Genderneutraal"
        } else {
            holder.view.toilet_genderlbl.text = dataSet[position].doelgroep
        }
        if(dataSet[position].straat != null){
            holder.view.toilet_street.text = dataSet[position].straat
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context,ToiletDetailsActivity::class.java)
            intent.putExtra("toilet_street",dataSet[position].straat)
            intent.putExtra("toilet_gender",dataSet[position].doelgroep)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
    override fun getItemCount() = dataSet.count()

}