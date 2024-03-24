package com.mobdeve.s13.group03.goalkeep

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s13.group03.goalkeep.databinding.EditTaskDetailsLayoutBinding

class EditTaskActivity : AppCompatActivity() {
    private lateinit var editTaskDetailsLayoutBinding: EditTaskDetailsLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        editTaskDetailsLayoutBinding = EditTaskDetailsLayoutBinding.inflate(layoutInflater)
        setContentView(editTaskDetailsLayoutBinding.root)

        val task = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(IntentKeys.TASK_OBJECT_KEY.name, Task::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(IntentKeys.TASK_OBJECT_KEY.name)
        }

        if (task != null) {
            editTaskDetailsLayoutBinding.etEditTaskTitle.setText(task.name)
            editTaskDetailsLayoutBinding.etEditTaskDescription.setText(task.description)
            editTaskDetailsLayoutBinding.etEditTaskPriority.setText(task.priority)
            editTaskDetailsLayoutBinding.etEditTaskState.setText(task.state)
            editTaskDetailsLayoutBinding.etEditTaskTimeExpected.setText(task.timeExpected)
        }

        // TODO: Do Confirm Logic
        editTaskDetailsLayoutBinding.ibEditTaskConfirm.setOnClickListener {
            this.finish()
        }

        // TODO: Do Cancel Logic
        editTaskDetailsLayoutBinding.ibEditTaskCancel.setOnClickListener {
            this.finish()
        }

        // TODO: Do Delete Logic
        editTaskDetailsLayoutBinding.btnDeleteTask.setOnClickListener {
            this.finish()
        }
    }
}