package com.example.dictionary

import android.content.res.Resources
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecordEntriesDecoration(private val resources: Resources) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        val topBottomOffset = 20.px()
        val bottomMenuOffset = 80.px()

        outRect.top = if (position == 0) topBottomOffset else topBottomOffset / 2
        outRect.bottom =
            if (position == itemCount - 1) topBottomOffset + bottomMenuOffset else topBottomOffset / 2

        outRect.left = 16.px()
        outRect.right = 16.px()
    }

    private fun Int.px(): Int {
        val floatPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            resources.displayMetrics
        )

        return floatPx.toInt()
    }
}