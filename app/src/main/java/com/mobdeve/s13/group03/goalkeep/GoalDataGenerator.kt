package com.mobdeve.s13.group03.goalkeep

class GoalDataGenerator {
    companion object {
        fun generateGoalData(): ArrayList<Goal> {
            var goals = ArrayList<Goal>()
            goals.add(Goal(1, "MOBDEVE MCO2", "XXXX-XX-XX",
                "2024-02-24 23:59:00", "XXXX-XX-XX", "MOBDEVE MP",
                "High", "Incomplete", "Academics"))
            goals.add(Goal(2, "CSARCH2 Exercise 2", "XXXX-XX-XX",
                "2024-02-26 23:59:00", "XXXX-XX-XX", "TinyML and IMC",
                "Medium", "Incomplete", "Academics"))
            goals.add(Goal(3, "STDISCM Exercises", "XXXX-XX-XX",
                "2024-02-24 23:59:00", "XXXX-XX-XX", "Mutual Exclusion",
                "Low", "Incomplete", "Academics"))
            goals.add(Goal(4, "THS-ST2 Presentation", "XXXX-XX-XX",
                "2024-02-24 23:59:00", "XXXX-XX-XX", "ChatGPT Transformers",
                "High", "Incomplete", "Academics"))
            goals.add(Goal(5, "MOBDEVE MCO3 Deadline", "XXXX-XX-XX",
                "2024-04-01 23:59:00", "XXXX-XX-XX", "Final Deliverables",
                "Medium", "Incomplete", "Academics"))
            goals.add(Goal(6, "STELEC2 Activity", "XXXX-XX-XX",
                "2024-03-12 23:59:00", "XXXX-XX-XX", "Activity on Paper Reporting",
                "Low", "Incomplete", "Academics"))
            goals.add(Goal(7, "STELEC3 Quiz", "XXXX-XX-XX",
                "2024-03-13 23:59:00", "XXXX-XX-XX", "French Nouns and Verbs",
                "Low", "Incomplete", "Academics"))
            goals.add(Goal(8, "Finalize Pre-Acts Document", "XXXX-XX-XX",
                "2024-03-20 23:59:00", "XXXX-XX-XX", "Finalize Pre-Acts for MGS with LSCS",
                "Low", "Incomplete", "Organization"))
            goals.add(Goal(9, "Birthday Preparation", "XXXX-XX-XX",
                "2024-03-31 23:59:00", "XXXX-XX-XX", "Birthday mo, Birthday ko?",
                "Low", "Incomplete", "Personal"))
            goals.add(Goal(10, "Grade Consultation Day", "XXXX-XX-XX",
                "2024-04-15 23:59:00", "XXXX-XX-XX", "Consult with MOBDEVE Grade",
                "Low", "Incomplete", "Academics"))
            return goals
        }
    }
}