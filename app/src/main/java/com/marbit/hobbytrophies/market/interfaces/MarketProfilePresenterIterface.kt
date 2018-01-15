package com.marbit.hobbytrophies.market.interfaces

import com.marbit.hobbytrophies.market.model.Rate

interface MarketProfilePresenterIterface{
    fun loadRatesSuccessful(rateList: List<Rate>, reputation: Float)
}