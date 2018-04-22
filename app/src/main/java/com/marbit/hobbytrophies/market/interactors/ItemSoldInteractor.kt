package com.marbit.hobbytrophies.market.interactors

import android.content.Context
import com.marbit.hobbytrophies.dao.ItemDAO
import com.marbit.hobbytrophies.dao.RateDAO
import com.marbit.hobbytrophies.market.interfaces.ItemSoldPresenterInterface
import com.marbit.hobbytrophies.market.model.Rate
import com.marbit.hobbytrophies.market.model.UserMarket
import com.marbit.hobbytrophies.model.market.Item

class ItemSoldInteractor constructor(val context: Context, private val presenterListener: ItemSoldPresenterInterface) {


    fun loadPossiblesBuyerSuccessful(itemId: String) {
        val itemDao = ItemDAO(context)
        itemDao.loadPossiblesBuyers(itemId, { presenterListener.loadPossiblesBuyerSuccessful(it) })
    }

    fun soldInHobby(rate: Rate, item: Item, userMarket: UserMarket) {
        item.status = 1
        val rateDAO = RateDAO()
        rateDAO.insertRate(rate, RateDAO.RateListener {
            val itemDao = ItemDAO(context)
            itemDao.markSold(item, userMarket)
            presenterListener.soldInHobbySuccess()
        })
    }

    fun soldOutHobby(item: Item) {
        item.status = 1
        val itemDao = ItemDAO(context)
        itemDao.markSold(item)
    }
}
