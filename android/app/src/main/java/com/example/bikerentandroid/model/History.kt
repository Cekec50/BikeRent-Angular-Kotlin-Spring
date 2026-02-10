package com.example.bikerentandroid.model

/**
 * Matches backend History: id, bike, startTime, endTime, totalPrice, duration.
 * startTime/endTime are ISO-8601 strings from backend (e.g. "2026-01-25T11:55:00").
 */
data class History(
    val id: Long,
    val bike: Bike? = null,
    val startTime: String? = null,
    val endTime: String? = null,
    val totalPrice: Double? = null,
    val duration: Long? = null
) {
    /** Bike type for display (e.g. "Electric bike", "City bike"). */
    fun getBikeTypeDisplay(): String = bike?.type ?: "—"

    /** Total price formatted with RSD. */
    fun getPriceDisplay(): String {
        val price = totalPrice ?: return "—"
        return "%,d RSD".format(price.toLong())
    }

    /** startTime formatted as "DD.MM.YYYY at HH:mm". */
    fun getDateDisplay(): String {
        val raw = startTime ?: return "—"
        return try {
            // Backend sends LocalDateTime as "2026-01-25T11:55:00" or "2026-01-25T11:55"
            val dt = java.time.LocalDateTime.parse(raw.take(19).replace(" ", "T"))
            "%02d.%02d.%d at %02d:%02d".format(
                dt.dayOfMonth,
                dt.monthValue,
                dt.year,
                dt.hour,
                dt.minute
            )
        } catch (_: Exception) {
            raw
        }
    }
}
