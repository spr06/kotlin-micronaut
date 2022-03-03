package moj.repo

import moj.model.Zone
import moj.repo.FareCalculator.Companion.BUS_FARE
import moj.repo.FareCalculator.Companion.FARE_ANY_ONE_ZONE_OUTSIDE_ZONE_1
import moj.repo.FareCalculator.Companion.FARE_ANY_THREE_ZONES
import moj.repo.FareCalculator.Companion.FARE_ANY_TWO_ZONE_INCLUDING_ZONE_1
import moj.repo.FareCalculator.Companion.FARE_ANY_TWO_ZONE_NOT_ZONE_1
import moj.repo.FareCalculator.Companion.FARE_ZONE_1
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FareCalculatorTest {

    private val fareCalculator = FareCalculator()

    @Test
    fun `anywhere in zone 1`() {
        assertThat(fareCalculator.calculateFare(listOf(Zone.ZONE_1))).isEqualTo(FARE_ZONE_1)
    }

    @Test
    fun `Calculate fare for any one zone outside zone 1`() {
        assertThat(fareCalculator.calculateFare(listOf(Zone.ZONE_2))).isEqualTo(FARE_ANY_ONE_ZONE_OUTSIDE_ZONE_1)
        assertThat(fareCalculator.calculateFare(listOf(Zone.ZONE_3))).isEqualTo(FARE_ANY_ONE_ZONE_OUTSIDE_ZONE_1)
    }

    @Test
    fun `Calculate fare for any outside zone and zone 1`() {
        assertThat(fareCalculator.calculateFare(listOf(Zone.ZONE_1, Zone.ZONE_2))).isEqualTo(FARE_ANY_TWO_ZONE_INCLUDING_ZONE_1)
        assertThat(fareCalculator.calculateFare(listOf(Zone.ZONE_1, Zone.ZONE_3))).isEqualTo(FARE_ANY_TWO_ZONE_INCLUDING_ZONE_1)
    }

    @Test
    fun `Calculate fare for any outside zone excluding zone 1`() {
        assertThat(fareCalculator.calculateFare(listOf(Zone.ZONE_3, Zone.ZONE_2))).isEqualTo(FARE_ANY_TWO_ZONE_NOT_ZONE_1)
    }

    @Test
    fun `Calculate fare for any 3 zones`() {
        assertThat(fareCalculator.calculateFare(listOf(Zone.ZONE_3, Zone.ZONE_2, Zone.ZONE_1))).isEqualTo(FARE_ANY_THREE_ZONES)
    }

    @Test
    fun `Calculate fare for zone 1`() {
        assertThat(fareCalculator.calculateFare(listOf(Zone.ZONE_1, Zone.ZONE_1))).isEqualTo(FARE_ZONE_1)
    }

    @Test
    fun `Calculate fare for zone 1_2 and zone 1`() {
        assertThat(fareCalculator.calculateFare(listOf(Zone.ZONE_1_2, Zone.ZONE_1))).isEqualTo(FARE_ZONE_1)
    }

    @Test
    fun `Calculate fare for zone 1_2 and zone 2`() {
        assertThat(fareCalculator.calculateFare(listOf(Zone.ZONE_1_2, Zone.ZONE_2))).isEqualTo(FARE_ANY_ONE_ZONE_OUTSIDE_ZONE_1)
    }

    @Test
    fun `Calculate fare for any bus journey`() {
        assertThat(fareCalculator.getBusFare()).isEqualTo(BUS_FARE)
    }

}