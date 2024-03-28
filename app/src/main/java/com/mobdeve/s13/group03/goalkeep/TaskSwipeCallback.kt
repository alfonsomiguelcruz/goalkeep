package com.mobdeve.s13.group03.goalkeep

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.group03.goalkeep.adapter.TaskAdapter
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class TaskSwipeCallback (dragDirs: Int, swipeDirs: Int, context: Context) :
    ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {
    var taskAdapter: TaskAdapter? = null
    var background: ColorDrawable = ColorDrawable(Color.BLACK)
    var activityContext: Context = context

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.absoluteAdapterPosition
        //goal!!.removePlayListMediaItem(position)
    }

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        RecyclerViewSwipeDecorator.Builder(activityContext, canvas, recyclerView,
            viewHolder, dX, dY, actionState, isCurrentlyActive)
            .addSwipeLeftBackgroundColor(Color.BLACK)
            .addSwipeLeftActionIcon(R.drawable.trash_icon)
            .addSwipeRightBackgroundColor(Color.BLACK)
            .addSwipeRightActionIcon(R.drawable.trash_icon)
            .create()
            .decorate()

        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}