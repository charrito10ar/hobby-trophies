package com.marbit.hobbytrophies.market.presenters

import android.content.Context
import com.marbit.hobbytrophies.market.interactors.MarketProfileInteractor
import com.marbit.hobbytrophies.market.interfaces.MarketProfilePresenterIterface
import com.marbit.hobbytrophies.market.interfaces.MarketProfileView
import com.marbit.hobbytrophies.market.model.Rate

class MarketProfilePresenter(applicationContext: Context, marketProfileView: MarketProfileView): MarketProfilePresenterIterface {
    val view = marketProfileView
    val interactor: MarketProfileInteractor = MarketProfileInteractor(applicationContext, this)

    override fun loadRatesSuccessful(rateList: List<Rate>, reputation: Float) {
        view.loadRatesSuccessful(rateList, reputation)
    }

    fun loadRates(userId: String?) {
        interactor.loadRates(userId)
    }
}
