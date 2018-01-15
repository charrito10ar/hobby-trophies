package com.marbit.hobbytrophies.market.interfaces

import com.marbit.hobbytrophies.market.model.UserMarket

interface ItemSoldPresenterInterface {

    fun loadPossiblesBuyers(itemId: String)
    fun loadPossiblesBuyerSuccessful(userMarketList: List<UserMarket>)
    fun soldInHobbySuccess()
}