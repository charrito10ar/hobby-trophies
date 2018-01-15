package com.marbit.hobbytrophies.market.presenters

import android.content.Context
import com.marbit.hobbytrophies.market.interactors.ItemSoldInteractor
import com.marbit.hobbytrophies.market.interfaces.ItemSoldPresenterInterface
import com.marbit.hobbytrophies.market.interfaces.ItemSoldView
import com.marbit.hobbytrophies.market.model.Rate
import com.marbit.hobbytrophies.market.model.UserMarket
import com.marbit.hobbytrophies.model.market.Item

class ItemSoldPresenter constructor(context: Context, var view: ItemSoldView): ItemSoldPresenterInterface {

    val interactor: ItemSoldInteractor by lazy { ItemSoldInteractor(context, this) }

    override fun loadPossiblesBuyers(itemId: String) {
        interactor.loadPossiblesBuyerSuccessful(itemId)
    }

    override fun loadPossiblesBuyerSuccessful(userMarketList: List<UserMarket>) {
        view.loadPossiblesBuyerSuccessful(userMarketList)
    }

    fun soldOutHobby(item: Item) {
        interactor.soldOutHobby(item)
        view.soldSuccess()
    }

    fun rateBuyerUser(item: Item, userMarket: UserMarket) {
        view.openRateUser(item, userMarket)
    }

    fun soldInHobby(rate: Rate, item: Item, userMarket: UserMarket) {
        interactor.soldInHobby(rate, item, userMarket)
    }

    override fun soldInHobbySuccess() {
        view.soldSuccess()
    }
}
