package com.mobdeve.s13.group03.goalkeep

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s13.group03.goalkeep.databinding.AddTaskLayoutBinding

class AddTaskActivity : AppCompatActivity() {
    private lateinit var addTaskLayoutBinding: AddTaskLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addTaskLayoutBinding = AddTaskLayoutBinding.inflate(layoutInflater)
        setContentView(addTaskLayoutBinding.root)
    }
}