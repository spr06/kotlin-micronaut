package moj.repo

import jakarta.inject.Singleton
import moj.model.Zone
import moj.model.Zone.ZONE_1
import moj.model.Zone.ZONE_1_2

@Singleton
class FareCalculator {

    companion object {
        const val BUS_FARE: Double = 1.80
        const val FARE_ZONE_1: Double = 2.5
        const val FARE_ANY_ONE_ZONE_OUTSIDE_ZONE_1: Double = 2.0
        const val FARE_ANY_TWO_ZONE_INCLUDING_ZONE_1: Double = 3.0
        const val FARE_ANY_TWO_ZONE_NOT_ZONE_1: Double = 2.25
        const val FARE_ANY_THREE_ZONES: Double = 3.2
        const val MAX_FARE: Double = FARE_ANY_THREE_ZONES
    }

    fun calculateFare(listOf: List<Zone>): Double {

        val zonesTravelled = getEffectiveZones(listOf.toSet())

        return when {
            zonesTravelled.size >= 3 -> FARE_ANY_THREE_ZONES
            zonesTravelled.contains(ZONE_1) -> {
                when (zonesTravelled.size) {
                    1 -> FARE_ZONE_1
                    else -> FARE_ANY_TWO_ZONE_INCLUDING_ZONE_1
                }
            }
            zonesTravelled.size > 1 -> FARE_ANY_TWO_ZONE_NOT_ZONE_1
            else -> FARE_ANY_ONE_ZONE_OUTSIDE_ZONE_1
        }

    }

    private fun getEffectiveZones(travelledZoneSet: Set<Zone>): Set<Zone> {
        val zones = mutableSetOf<Zone>()
        travelledZoneSet.forEach{
            when {
                it == ZONE_1_2  && travelledZoneSet.contains(ZONE_1) -> {
                    zones.add(ZONE_1)
                }
                it == ZONE_1_2 && travelledZoneSet.contains(Zone.ZONE_2) -> {
                    zones.add(Zone.ZONE_2)
                }
                else -> {
                    zones.add(it)
                }
            }
        }
        return zones
    }

    fun getBusFare(): Double {
        return BUS_FARE
    }

}