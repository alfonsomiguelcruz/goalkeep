package com.mobdeve.s13.group03.goalkeep

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s13.group03.goalkeep.databinding.ViewGoalLayoutBinding

class ViewGoalActivity : AppCompatActivity() {
    companion object {
        const val TITLE_KEY = "TITLE_KEY"
        const val TIME_KEY = "TIME_KEY"
        const val PRIORITY_KEY = "PRIORITY_KEY"
        const val TAG_KEY = "TAG_KEY"
        const val DESCRIPTION_KEY = "DESCRIPTION_KEY"
        const val PROGRESS_KEY = "PROGRESS_KEY"
        const val STATE_KEY = "STATE_KEY"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vb: ViewGoalLayoutBinding = ViewGoalLayoutBinding.inflate(layoutInflater)
        setContentView(vb.root)

        /* TODO: Continue Other Activities */
    }
}