package com.mobdeve.s13.group03.goalkeep

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s13.group03.goalkeep.databinding.AddGoalLayoutBinding

class AddGoalActivity : AppCompatActivity() {
    private lateinit var addGoalLayoutBinding : AddGoalLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addGoalLayoutBinding = AddGoalLayoutBinding.inflate(layoutInflater)
        setContentView(addGoalLayoutBinding.root)
    }
}