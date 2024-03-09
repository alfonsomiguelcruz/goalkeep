package com.mobdeve.s13.group03.goalkeep

class TaskDataGenerator {
    companion object {
        fun generateTaskData(): ArrayList<Task> {
            var tasks = ArrayList<Task>()
            tasks.add(Task(10, "MOBDEVE MCO2", "XXXX-XX-XX",
                "2024-02-24", "XXXX-XX-XX", "MOBDEVE MP",
                "High", "Incomplete", 1))
            tasks.add(Task(20, "CSC612M Exercise 2", "XXXX-XX-XX",
                "2024-02-26", "XXXX-XX-XX", "TinyML and IMC",
                "Medium", "Incomplete", 2))
            tasks.add(Task(30, "CSC611M Exercises", "XXXX-XX-XX",
                "2024-02-24", "XXXX-XX-XX", "Mutual Exclusion",
                "Low", "Incomplete", 3))
            tasks.add(Task(40, "MSLABS1 Presentation", "XXXX-XX-XX",
                "2024-02-24", "XXXX-XX-XX", "ChatGPT Transformers",
                "High", "Incomplete", 4))
            return tasks
        }
    }
}