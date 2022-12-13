package be.ap.edu.mapsaver.fragments

import Attributes
import Data.DataBaseHelper
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import be.ap.edu.mapsaver.R
import be.ap.edu.mapsaver.adapters.ToiletListAdapter

class ToiletListFragment(val toiletList: ArrayList<Attributes>) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_toilet_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get a handle to the RecyclerView.
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)

        // Create an adapter and supply the data to be displayed.
        val adapter = ToiletListAdapter(activity as AppCompatActivity,requireContext().applicationContext,toiletList)

        // Connect the adapter with the RecyclerView.
        recyclerView.adapter = adapter

        // Give the RecyclerView a default layout manager.
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}