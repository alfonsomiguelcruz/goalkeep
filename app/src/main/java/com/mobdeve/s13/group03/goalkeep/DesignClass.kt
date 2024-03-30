package com.mobdeve.s13.group03.goalkeep

class DesignClass {
    companion object {
        fun getRegularColor(priority: String): Int {
            val color: Int = when(priority) {
                "High" -> R.drawable.regular_high
                "Medium" -> R.drawable.regular_medium
                "Low" -> R.drawable.regular_low
                else -> {
                    R.drawable.corners_gt
                }
            }

            return color
        }

        fun getCorneredColor(priority: String): Int {
            val color: Int = when(priority) {
                "High" -> R.drawable.corners_high
                "Medium" -> R.drawable.corners_medium
                "Low" -> R.drawable.corners_low
                else -> {
                    R.drawable.corners_gt
                }
            }

            return color
        }

        fun getSubColor(priority: String): Int {
            val color: Int = when(priority) {
                "High" -> R.drawable.corners_sub_high
                "Medium" -> R.drawable.corners_sub_medium
                "Low" -> R.drawable.corners_sub_low
                else -> {
                    R.drawable.corners_gt
                }
            }

            return color
        }

        fun getTextSubColor(priority: String): Int {
            val color: Int = when(priority) {
                "High" -> R.color.high_sub
                "Medium" -> R.color.medium_sub
                "Low" -> R.color.low_sub
                else -> {
                    R.color.black
                }
            }

            return color
        }

        fun getStateColor(state: String): Int {
            val color: Int = when(state) {
                "Complete" -> R.drawable.corners_complete
                "Incomplete" -> R.drawable.corners_incomplete
                else -> {
                    R.color.white
                }
            }

            return color
        }

        fun getDarkGray() : Int {
            return R.color.dark_grey
        }

        fun getWhite() : Int {
            return R.color.white
        }
    }
}