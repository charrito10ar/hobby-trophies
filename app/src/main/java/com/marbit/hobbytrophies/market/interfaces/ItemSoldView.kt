package com.marbit.hobbytrophies.market.interfaces

import com.marbit.hobbytrophies.market.model.UserMarket

import com.marbit.hobbytrophies.model.market.Item

interface ItemSoldView {
    fun soldInHobby(item: Item, userMarket: UserMarket)
    fun soldOutHobby(item: Item)
    fun loadPossiblesBuyerSuccessful(userMarketList: List<UserMarket>)
    fun showMessageToast(message: String)
    fun soldSuccess()
}