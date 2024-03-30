package com.mobdeve.s13.group03.goalkeep

import java.util.Calendar

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

        fun getAppendZero(minute: Int) : String {
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

        fun getDatabaseTimeFormat(year : Int, month : Int,
                                  day : Int, hour : Int, minute : Int) : String {
            return "${getAppendZero(year)}-${getAppendZero(month)}-${getAppendZero(day)} ${getAppendZero(hour)}:${getAppendZero(minute)}:00"
        }

        fun getCurrentTime() : String {
            val yearInput : Int = Calendar.getInstance().get(Calendar.YEAR)
            val monthInput : Int = Calendar.getInstance().get(Calendar.MONTH) + 1
            val dayInput : Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            val hourInput : Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            val minuteInput : Int = Calendar.getInstance().get(Calendar.MINUTE)

            return "$yearInput-${getAppendZero(monthInput)}-${getAppendZero(dayInput)} ${getAppendZero(hourInput)}:${getAppendZero(minuteInput)}:00"
        }

        // Check if time1 is later than time 2
        fun isLaterTime(y1 : Int, m1 : Int, d1 : Int, h1 : Int, mn1 : Int,
                        y2 : Int, m2 : Int, d2 : Int, h2 : Int, mn2 : Int) : Boolean {
            if(y1 > y2)
                return true
            else if(y1 == y2)
                if(m1 > m2)
                    return true
                else if(m1 == m2)
                    if(d1 > d2)
                        return true
                    else if(d1 == d2)
                        if(h1 > h2)
                            return true
                        else if (h1 == h2)
                            return mn1 >= mn2
                        else
                            return false
                    else
                        return false
                else
                    return false
            else
                return false
        }
    }
}