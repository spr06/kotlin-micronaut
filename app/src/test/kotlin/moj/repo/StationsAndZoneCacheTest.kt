package moj.repo

import moj.model.Zone.valueOf
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class StationsAndZoneCacheTest {


    @ParameterizedTest
    @CsvSource(
        "Holborn, ZONE_1",
        "Earl's Court, ZONE_1_2",
        "Wimbledon, ZONE_3",
        "Hammersmith, ZONE_2",
    )
    fun `Get zones for station`(stationName: String, zone: String) {
        assertThat(StationsAndZoneCache.getZones(stationName)).isEqualTo(getZones(zone))
    }

    private fun getZones(zone: String) = if (zone.contains("|")) {
        zone.split("|").map { valueOf(it) }.toList()
    } else listOf(valueOf(zone))
}