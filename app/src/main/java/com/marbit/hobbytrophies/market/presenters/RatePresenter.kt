package com.marbit.hobbytrophies.market.presenters

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.RatingBar
import com.marbit.hobbytrophies.market.interactors.RateInteractor
import com.marbit.hobbytrophies.market.interfaces.RateUserView
import com.marbit.hobbytrophies.market.model.Rate

class RatePresenter() : TextWatcher, RatePresenterInterface, RatingBar.OnRatingBarChangeListener {
    override fun onRatingChanged(p0: RatingBar?, p1: Float, p2: Boolean) {
        rateView.selectRate(p1)
    }

    lateinit var rateView: RateUserView

    override fun insertRateSuccessful(rate: Rate) {
        rateView.sendRateSuccessful(rate)
    }

    constructor(applicationContext: Context, rateUserView: RateUserView) : this() {
        this.rateView = rateUserView
        this.interactor = RateInteractor(this)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(comment: CharSequence?, p1: Int, p2: Int, p3: Int) {
        rateView.setCommentText(comment.toString())
    }

    override fun afterTextChanged(p0: Editable?) {}

    private lateinit var interactor: RateInteractor

    fun sendRate(chatId: String, rate: Rate) {
        interactor.sendRate(chatId, rate)
    }

}

interface RatePresenterInterface {
    fun insertRateSuccessful(rate: Rate)
}
