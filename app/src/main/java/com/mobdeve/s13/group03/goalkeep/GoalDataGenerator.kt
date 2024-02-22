package com.mobdeve.s13.group03.goalkeep

class GoalDataGenerator {
    companion object {
        fun generateGoalData(): ArrayList<Goal> {
            var goals = ArrayList<Goal>()
            goals.add(Goal(1, "MOBDEVE MCO2", "XXXX-XX-XX",
                "2024-02-24", "XXXX-XX-XX", "MOBDEVE MP",
                "High", "Incomplete", "Academics"))
            goals.add(Goal(2, "CSARCH2 Exercise 2", "XXXX-XX-XX",
                "2024-02-26", "XXXX-XX-XX", "TinyML and IMC",
                "Medium", "Incomplete", "Academics"))
            goals.add(Goal(3, "STDISCM Exercises", "XXXX-XX-XX",
                "2024-02-24", "XXXX-XX-XX", "Mutual Exclusion",
                "Low", "Incomplete", "Academics"))
            goals.add(Goal(4, "THS-ST2 Presentation", "XXXX-XX-XX",
                "2024-02-24", "XXXX-XX-XX", "ChatGPT Transformers",
                "High", "Incomplete", "Academics"))
            return goals
        }
    }
}