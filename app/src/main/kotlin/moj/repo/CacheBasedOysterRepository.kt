package moj.repo

import jakarta.inject.Singleton
import java.util.concurrent.ConcurrentHashMap

@Singleton
class CacheBasedOysterRepository : OysterRepository {

    private val oysterCardCurrentBalanceCache: ConcurrentHashMap<String, Double> = ConcurrentHashMap()

    override fun getBalance(oysterCardId: String): Double {
        return oysterCardCurrentBalanceCache.get(oysterCardId) ?: 0.0
    }

    override fun updateBalance(oysterCardId: String, balance: Double) {
        oysterCardCurrentBalanceCache.put(oysterCardId, balance)
    }

}