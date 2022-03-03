package moj.travel

import junit.framework.Assert.assertNull
import moj.model.Zone.ZONE_1
import moj.repo.CacheBasedOysterRepository
import moj.repo.FareCalculator
import moj.repo.FareCalculator.Companion.BUS_FARE
import moj.repo.FareCalculator.Companion.FARE_ZONE_1
import moj.repo.FareCalculator.Companion.MAX_FARE
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class JourneyManagerTest {

    private val INITIAL_BALANCE: Double = 50.0

    private val oysterRepository = CacheBasedOysterRepository()
    private val fareCalculator = FareCalculator()
    private val journeyManager = JourneyManager(oysterRepository, fareCalculator)
    private val oysterCard = "oysterCard"

    @BeforeEach
    internal fun setUp() {
        oysterRepository.updateBalance(oysterCard, INITIAL_BALANCE)
    }

    @AfterEach
    internal fun tearDown() {
        oysterRepository.updateBalance(oysterCard, 0.0)
    }

    @Test
    internal fun `Enter bus, start journey`() {

        journeyManager.startBusJourney(oysterCard)

        assertThat(oysterRepository.getBalance(oysterCard)).isEqualTo(INITIAL_BALANCE - BUS_FARE)
        assertNull(journeyManager.getJourneyHistory(oysterCard))
    }

    @Test
    fun `Enter station, start journey, max fare is deducted`() {

        journeyManager.startJourney(oysterCard, listOf(ZONE_1))

        assertThat(oysterRepository.getBalance(oysterCard)).isEqualTo(INITIAL_BALANCE - MAX_FARE)
        assertThat(journeyManager.getJourneyHistory(oysterCard)).isEqualTo(listOf(ZONE_1))

    }

    @Test
    fun `Exit station by touching out`() {

        journeyManager.startJourney(oysterCard, listOf(ZONE_1))
        assertThat(journeyManager.getJourneyHistory(oysterCard)).isEqualTo(listOf(ZONE_1))

        journeyManager.endJourney(oysterCard, listOf(ZONE_1))

        assertThat(oysterRepository.getBalance(oysterCard)).isEqualTo(INITIAL_BALANCE - FARE_ZONE_1)
        assertNull(journeyManager.getJourneyHistory(oysterCard))
    }

    @Test
    fun `Exit station without a valid touch out`() {

        journeyManager.startJourney(oysterCard, listOf(ZONE_1))

        assertThat(oysterRepository.getBalance(oysterCard)).isEqualTo(INITIAL_BALANCE - MAX_FARE)
        assertThat(journeyManager.getJourneyHistory(oysterCard)).isEqualTo(listOf(ZONE_1))
    }


}