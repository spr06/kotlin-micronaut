package moj.repo

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class OysterRepositoryTest {

    val oysterRepository = CacheBasedOysterRepository()
    var oysterCardId = "test-oyester-card"

    @Test
    fun `should be able to fetch current amount`() {

        assertThat(oysterRepository.getBalance(oysterCardId)).isEqualTo(0.0)

    }

    @Test
    fun `should be able to top up oyster card`() {

        val initialAmount = oysterRepository.getBalance(oysterCardId)

        oysterRepository.updateBalance(oysterCardId, 30.0)

        assertThat(oysterRepository.getBalance(oysterCardId)).isEqualTo(initialAmount + 30.0)

    }
}