package com.marbit.hobbytrophies.market.interactors

import android.content.Context
import com.marbit.hobbytrophies.dao.ItemDAO
import com.marbit.hobbytrophies.market.interfaces.ItemSoldPresenterInterface
import com.marbit.hobbytrophies.market.model.UserMarket
import com.marbit.hobbytrophies.model.market.Item

class ItemSoldInteractor constructor(context: Context, private val presenterListener: ItemSoldPresenterInterface) {


    fun loadPossiblesBuyerSuccessful(itemId: String) {
        val itemDao = ItemDAO()
        itemDao.loadPossiblesBuyers(itemId, { presenterListener.loadPossiblesBuyerSuccessful(it) })
    }

    fun soldInHobby(item: Item, userMarket: UserMarket) {
        item.status = 1
        val itemDao = ItemDAO()
        itemDao.markSold(item, userMarket)
    }

    fun soldOutHobby(item: Item) {
        item.status = 1
        val itemDao = ItemDAO()
        itemDao.markSold(item)
    }
}
