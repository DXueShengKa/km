package kotlinx.datetime


expect class IsoChronology {
    fun isLeapYear(prolepticYear:Long):Boolean
}

expect val isoChronology:IsoChronology