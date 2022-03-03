package moj.repo

interface OysterRepository {

    fun getBalance(oysterCardId: String): Double

    fun updateBalance(oysterCardId: String, topUpAmount: Double)

}