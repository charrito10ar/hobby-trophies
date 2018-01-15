package com.marbit.hobbytrophies.market

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.marbit.hobbytrophies.R
import com.marbit.hobbytrophies.market.interfaces.RateUserView
import com.marbit.hobbytrophies.market.model.Rate
import com.marbit.hobbytrophies.market.presenters.RatePresenter
import com.marbit.hobbytrophies.utilities.Preferences
import kotlinx.android.synthetic.main.activity_rate_user.*

class RateUserActivity : AppCompatActivity(), RateUserView {
    override fun sendRateSuccessful(rate: Rate) {
        finish()
    }

    override fun setCommentText(comment: String) {
        this.rate.comment = comment
    }

    override fun selectRate(numStar: Float) {
        this.rate.value = numStar.toInt()
    }

    private lateinit var rate: Rate
    private lateinit var presenter: RatePresenter

    private lateinit var chatId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate_user)
        val rateType = intent.getStringExtra("RATE-TYPE")
        val receiverName = intent.getStringExtra("RATE-RECEIVER-NAME")
        val receiverId = intent.getStringExtra("RATE-RECEIVER-ID")
        val itemId = intent.getStringExtra("ITEM-ID")
        chatId = intent.getStringExtra("CHAT-ID")
        rate_title.text = "Valora tu experiencia con " + receiverName

        rate = Rate(receiverId, rateType, Preferences.getUserId(applicationContext), itemId)
        presenter = RatePresenter(applicationContext, this)
        rating_bar.onRatingBarChangeListener = presenter
        edit_text_comment.addTextChangedListener(presenter)
    }

    fun sendRate(view: View){
        if(rating_bar.numStars < 1)
            Toast.makeText(applicationContext, "Por favor califique con estrellas al vendedor", Toast.LENGTH_SHORT).show()
        else
            presenter.sendRate(chatId, rate)
    }
}
