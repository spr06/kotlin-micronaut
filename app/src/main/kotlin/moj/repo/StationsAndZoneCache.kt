package moj.repo

import moj.model.Zone
import moj.model.Zone.ZONE_1
import moj.model.Zone.ZONE_1_2
import moj.model.Zone.ZONE_2
import moj.model.Zone.ZONE_3

object StationsAndZoneCache {

    private var stationZoneCache = mapOf (
        "Holborn" to listOf(ZONE_1),
        "Earl's Court" to listOf(ZONE_1_2),
        "Wimbledon" to listOf(ZONE_3),
        "Hammersmith" to listOf(ZONE_2),
        "EarlsCourt" to listOf(ZONE_1_2),
    )

    fun getZones(stationName: String): List<Zone> {
        return stationZoneCache.get(stationName) ?: throw RuntimeException("Station not found")
    }


}