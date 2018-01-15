package com.marbit.hobbytrophies.market

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.marbit.hobbytrophies.R
import com.marbit.hobbytrophies.market.interfaces.MarketProfileView
import com.marbit.hobbytrophies.market.model.Rate
import com.marbit.hobbytrophies.market.presenters.MarketProfilePresenter
import com.marbit.hobbytrophies.utilities.Preferences
import kotlinx.android.synthetic.main.activity_market_profile.*

class MarketProfileActivity : AppCompatActivity(), MarketProfileView {

    override fun loadRatesSuccessful(rateList: List<Rate>, reputation: Float) {
        rating_bar_profile.rating = reputation
    }

    lateinit var presenter: MarketProfilePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market_profile)
        setSupportActionBar(toolbar)
        this.presenter = MarketProfilePresenter(applicationContext, this)
        presenter.loadRates(Preferences.getUserId(applicationContext))
    }
}
