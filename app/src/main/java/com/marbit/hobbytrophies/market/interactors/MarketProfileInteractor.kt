package com.marbit.hobbytrophies.market.interactors

import android.content.Context
import com.marbit.hobbytrophies.dao.RateDAO
import com.marbit.hobbytrophies.market.interfaces.MarketProfilePresenterIterface
import com.marbit.hobbytrophies.market.model.Rate

class MarketProfileInteractor(applicationContext: Context, presenterInterface: MarketProfilePresenterIterface) {
    val presesenter = presenterInterface

    fun loadRates(userId: String?) {
        var rateDAO = RateDAO()
        rateDAO.loadRatesByUser(userId, RateDAO.LoadRateListener { mutableList: MutableList<Rate>, reputation: Float ->
            presesenter.loadRatesSuccessful(mutableList, reputation)
        })
    }

}
