package kotlinx.datetime

actual typealias IsoChronology = java.time.chrono.IsoChronology

actual val isoChronology:IsoChronology = IsoChronology.INSTANCE
