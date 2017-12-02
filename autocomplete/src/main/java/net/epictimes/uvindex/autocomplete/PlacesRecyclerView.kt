package net.epictimes.uvindex.autocomplete

import android.location.Address
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class PlacesRecyclerView : RecyclerView.Adapter<PlaceViewHolder>() {
    private val addresses = mutableListOf<Address>()

    fun setAddresses(newAddresses: List<Address>) {
        with(addresses) {
            clear()
            addAll(newAddresses)
        }

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(PlaceViewHolder.LAYOUT_ID, parent, false)
        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(addresses[position])
    }

    override fun getItemCount(): Int = addresses.size

}