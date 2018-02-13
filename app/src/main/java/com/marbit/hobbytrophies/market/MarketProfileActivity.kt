package com.marbit.hobbytrophies.market

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.marbit.hobbytrophies.R
import com.marbit.hobbytrophies.market.adapters.RatesAdapter
import com.marbit.hobbytrophies.market.interfaces.MarketProfileView
import com.marbit.hobbytrophies.market.model.Rate
import com.marbit.hobbytrophies.market.presenters.MarketProfilePresenter
import com.marbit.hobbytrophies.utilities.Preferences
import kotlinx.android.synthetic.main.activity_market_profile.*

class MarketProfileActivity : AppCompatActivity(), MarketProfileView {
    override fun hideEmptyView() {
        text_empty_list.visibility = View.INVISIBLE
    }

    override fun showEmptyView() {
        text_empty_list.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progress_bar.visibility = View.INVISIBLE
    }

    override fun showProgressBar() {
        progress_bar.visibility = View.VISIBLE
    }

    override fun refreshRateList(rateList: List<Rate>) {
        rateAdapter.clearAll()
        rateAdapter.setList(rateList)
        rateAdapter.notifyDataSetChanged()
    }

    lateinit var presenter: MarketProfilePresenter
    lateinit var rateAdapter: RatesAdapter

    override fun loadRatesSuccessful(rateList: List<Rate>, reputation: Float) {
        rating_bar_profile.rating = reputation
        market_profile_title.setText("Reputación: " + reputation + "/5")
        refreshRateList(rateList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market_profile)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);
        this.presenter = MarketProfilePresenter(applicationContext, this)
        presenter.loadRates(Preferences.getUserId(applicationContext))
        this.rateAdapter = RatesAdapter(applicationContext)
        val mLayoutManagerRates = LinearLayoutManager(applicationContext)
        recycler_view_rates.layoutManager = mLayoutManagerRates
        recycler_view_rates.adapter = this.rateAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }
}
