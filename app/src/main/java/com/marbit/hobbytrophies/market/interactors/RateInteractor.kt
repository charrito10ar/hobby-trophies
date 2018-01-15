package com.marbit.hobbytrophies.market.interactors

import com.marbit.hobbytrophies.chat.dao.ChatDAO
import com.marbit.hobbytrophies.dao.RateDAO
import com.marbit.hobbytrophies.market.model.Rate
import com.marbit.hobbytrophies.market.presenters.RatePresenterInterface

class RateInteractor() {
    lateinit var ratePresenterInterface: RatePresenterInterface

    constructor(ratePresenterInterface: RatePresenterInterface) : this() {
        this.ratePresenterInterface = ratePresenterInterface
    }

    fun sendRate(chatId: String, rate: Rate) {
        val rateDAO = RateDAO()
        rateDAO.insertRate(rate, {
            val chatDAO = ChatDAO()
            chatDAO.insertItemSoldMessageRated(chatId)
            ratePresenterInterface.insertRateSuccessful(rate)
        })

    }

}