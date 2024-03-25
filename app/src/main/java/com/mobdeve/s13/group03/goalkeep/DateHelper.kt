package com.mobdeve.s13.group03.goalkeep

class DateHelper {
    companion object {
         fun getMonthName(monthOfYear: Int) : String{
            return when(monthOfYear) {
                1 -> "January"
                2 -> "February"
                3 -> "March"
                4 -> "April"
                5 -> "May"
                6 -> "June"
                7 -> "July"
                8 -> "August"
                9 -> "September"
                10 -> "October"
                11 -> "November"
                12 -> "December"
                else -> "Error"
            }
        }

         fun getMonthName(monthOfYear: String) : String {
            return when(monthOfYear) {
                "01" -> "January"
                "02" -> "February"
                "03" -> "March"
                "04" -> "April"
                "05" -> "May"
                "06" -> "June"
                "07" -> "July"
                "08" -> "August"
                "09" -> "September"
                "10" -> "October"
                "11" -> "November"
                "12" -> "December"
                else -> "Error"
            }
        }

         fun getAMPM(hourOfDay : Int) : String {
            return if(hourOfDay < 12)
                "AM"
            else
                "PM"
        }

         fun getDateFormat(timeExpected: String) : String {
            val year = timeExpected.substring(0, 4)
            val month = timeExpected.substring(5,7)
            val day = timeExpected.substring(8,10)

            return "${getMonthName(month)} $day, $year"
        }

         fun getTimeFormat(timeExpected : String) : String {
            val hour = timeExpected.substring(11,13)
            val minute = timeExpected.substring(14,16)
            val strHour : String = when(hour.toInt()) {
                0 -> "12"
                12 -> "12"
                else -> {
                    if(hour.toInt() % 12 < 10)
                        "0${(hour.toInt() % 12)}"
                    else
                        (hour.toInt() % 12).toString()
                }
            }

            return "${strHour}:$minute ${getAMPM(hour.toInt())}"
        }

        fun getMinuteFormat(minute: Int) : String {
            return if(minute < 10)
                "0$minute"
            else
                minute.toString()
        }

         fun getNonMilitaryHour(hour : Int) : Int {
            var nmHour : Int = hour

            when(hour) {
                0 -> nmHour = 12
                else -> {
                    if(hour > 12)
                        nmHour = hour % 12
                }
            }

            return nmHour
        }
    }
}