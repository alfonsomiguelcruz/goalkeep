package com.mobdeve.s13.group03.goalkeep

class GoalDataGenerator {
    companion object {
        fun generateGoalData(): ArrayList<Goal> {
            var goals = ArrayList<Goal>()
            goals.add(Goal(1, "MOBDEVE MCO2", "XXXX-XX-XX",
                "2024-02-24", "XXXX-XX-XX", "MOBDEVE MP",
                "High Priority", "Incomplete", "Academics"))
            goals.add(Goal(2, "CSC612M Exercise 2", "XXXX-XX-XX",
                "2024-02-26", "XXXX-XX-XX", "TinyML and IMC",
                "Medium Priority", "Incomplete", "Academics"))
            goals.add(Goal(3, "CSC611M Exercises", "XXXX-XX-XX",
                "2024-02-24", "XXXX-XX-XX", "Mutual Exclusion",
                "Low Priority", "Incomplete", "Academics"))
            goals.add(Goal(4, "MSLABS1 Presentation", "XXXX-XX-XX",
                "2024-02-24", "XXXX-XX-XX", "ChatGPT Transformers",
                "High Priority", "Incomplete", "Academics"))
            return goals
        }
    }
}