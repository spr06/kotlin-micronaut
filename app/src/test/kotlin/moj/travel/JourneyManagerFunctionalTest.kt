package moj.travel

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import moj.repo.CacheBasedOysterRepository
import moj.repo.StationsAndZoneCache.getZones
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@MicronautTest
class JourneyManagerFunctionalTest {

    @Inject
    lateinit var journeyManager: JourneyManager

    @ParameterizedTest
    @CsvSource(
        "Any two zones including zone 1 £3.00, Holborn, Hammersmith|EarlsCourt, 27",
        "Anywhere in Zone 1 £2.50, Holborn, Holborn, 27.5",
        "Anywhere in Zone 1 £2.50 if zone 1 and 2 decision in favour of passenger, Holborn, EarlsCourt, 27.5",
        "Any one zone outside zone 1 £2.00, Wimbledon, Wimbledon, 28",
        "Any one zone outside zone 1 £2.00, Hammersmith, Hammersmith, 28",
        "Any two zones including zone 1 £3.00, Holborn, Wimbledon, 27",
        "Any two zones including zone 1 £3.00, Holborn, Wimbledon, 27",
        "Any two zones excluding zone 1 £2.25, Wimbledon, Hammersmith, 27.75",
        "Any two zones excluding zone 1 £2.25, Wimbledon, EarlsCourt, 27.75",
        "Any three zones £3.20, Holborn, Wimbledon|Hammersmith, 26.80"
    )
    internal fun `test journey scenarios`(testName: String, startStation: String, pipeSeparatedStationsList: String, finalBalance: Double ) {
        val oysterCardId = "OysterCardId"

        //Given Oyster card is loaded with 30 GBP
        oysterRepository.updateBalance(oysterCardId, 30.0)

        //When
        val startZones = getZones(startStation)
        journeyManager.startJourney(oysterCardId, startZones)
        val zonesTravelled = pipeSeparatedStationsList.split("|").map { getZones(it) }.flatten()
        journeyManager.endJourney(oysterCardId, zonesTravelled)

        //Then
        assertThat(oysterRepository.getBalance(oysterCardId)).`as`(testName).isEqualTo(finalBalance)
    }


    @Inject
    lateinit var oysterRepository: CacheBasedOysterRepository
}