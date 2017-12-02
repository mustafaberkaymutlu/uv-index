package net.epictimes.uvindex.autocomplete

import android.location.Address
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.list_item_place.view.*

class PlaceViewHolder(private val rootView: View) : RecyclerView.ViewHolder(rootView) {

    companion object {
        val LAYOUT_ID = R.layout.list_item_place
    }

    fun bind(address: Address) {
        with(rootView) {
            textViewPlace.text = address.adminArea
        }
    }
}