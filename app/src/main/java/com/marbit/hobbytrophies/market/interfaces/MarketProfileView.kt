package com.marbit.hobbytrophies.market.interfaces

import com.marbit.hobbytrophies.market.model.Rate


interface MarketProfileView {
    fun loadRatesSuccessful(rateList: List<Rate>, reputation: Float)
}