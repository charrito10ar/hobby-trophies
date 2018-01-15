package com.marbit.hobbytrophies.market.interfaces

import com.marbit.hobbytrophies.market.model.Rate


interface RateUserView {
    fun selectRate(numStar: Float)
    fun setCommentText(comment: String)
    fun sendRateSuccessful(rate: Rate)

}
