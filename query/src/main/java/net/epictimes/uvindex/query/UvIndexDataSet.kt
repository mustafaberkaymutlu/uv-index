package net.epictimes.uvindex.query

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet

class UvIndexDataSet(yVals: List<Entry>, label: String) : LineDataSet(yVals, label) {
    override fun getColor(index: Int): Int
            = mColors[getEntryForIndex(index).y.toInt()]
}