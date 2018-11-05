package net.epictimes.uvindex.autocomplete

import android.location.Address
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_place.view.*

class PlaceViewHolder(private val rootView: View) : RecyclerView.ViewHolder(rootView), View.OnClickListener {
    var clickListener: ((pos: Int) -> Unit)? = null

    companion object {
        val LAYOUT_ID = R.layout.list_item_place
    }

    fun bind(address: Address) {
        val placeAddress = (0..address.maxAddressLineIndex).joinToString(separator = "\n") { address.getAddressLine(it) }

        with(rootView) {
            textViewPlace.text = placeAddress
            setOnClickListener(this@PlaceViewHolder)
        }
    }

    override fun onClick(v: View?) {
        if (adapterPosition != RecyclerView.NO_POSITION) {
            clickListener?.invoke(adapterPosition)
        }
    }
}