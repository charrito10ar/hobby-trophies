package com.marbit.hobbytrophies.market.interfaces

import com.marbit.hobbytrophies.market.model.Rate


interface MarketProfileView {
    fun loadRatesSuccessful(rateList: List<Rate>, reputation: Float)
    fun refreshRateList(rateList: List<Rate>)
    fun showProgressBar()
    fun hideProgressBar()
    fun showEmptyView()
    fun hideEmptyView()
}