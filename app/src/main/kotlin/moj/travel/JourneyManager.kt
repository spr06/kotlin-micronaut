package moj.travel

import jakarta.inject.Singleton
import moj.model.Zone
import moj.repo.FareCalculator
import moj.repo.FareCalculator.Companion.MAX_FARE
import moj.repo.OysterRepository

@Singleton
class JourneyManager(
    private val oysterRepository: OysterRepository,
    private val fareCalculator: FareCalculator
) {

    private val dailyTravelHistory = mutableMapOf<String, List<Zone>>()

    fun startBusJourney(oysterCard: String) {
        val initialBalance = oysterRepository.getBalance(oysterCard)
        oysterRepository.updateBalance(oysterCard, initialBalance.minus(fareCalculator.getBusFare()))
    }

    fun startJourney(oysterCard: String, zone: List<Zone>) {
        val initialBalance = oysterRepository.getBalance(oysterCard)
        oysterRepository.updateBalance(oysterCard, initialBalance.minus(MAX_FARE))
        dailyTravelHistory[oysterCard] = getPresentHistory(oysterCard).plus(zone)
    }

    private fun getPresentHistory(oysterCard: String) = dailyTravelHistory[oysterCard] ?: mutableSetOf()

    fun endJourney(oysterCard: String, zone: List<Zone>) {
        val initialBalance = oysterRepository.getBalance(oysterCard)
        val fare = initialBalance.plus(MAX_FARE).minus(fareCalculator.calculateFare(zone + getPresentHistory(oysterCard)))
        oysterRepository.updateBalance(oysterCard, fare)
        dailyTravelHistory.remove(oysterCard)
    }


    fun getJourneyHistory(oysterCard: String): List<Zone>? {
        return dailyTravelHistory[oysterCard]
    }

}