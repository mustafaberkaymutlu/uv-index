package net.epictimes.uvindex.query

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet

class UvIndexDataSet(yValues: List<Entry>, label: String) : LineDataSet(yValues, label) {
    override fun getColor(index: Int): Int
            = mColors[getEntryForIndex(index).y.toInt()]
}