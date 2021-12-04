package kotlinx.datetime

actual fun Month.length(leapYear:Boolean) = length(leapYear)

actual fun Month(month:Int):Month = Month.of(month)
