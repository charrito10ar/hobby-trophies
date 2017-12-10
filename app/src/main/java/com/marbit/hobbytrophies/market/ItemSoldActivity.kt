package com.marbit.hobbytrophies.market

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.marbit.hobbytrophies.R
import com.marbit.hobbytrophies.market.adapters.PossibleBuyersAdapter
import com.marbit.hobbytrophies.market.interfaces.ItemSoldView
import com.marbit.hobbytrophies.market.model.UserMarket
import com.marbit.hobbytrophies.market.presenters.ItemSoldPresenter
import com.marbit.hobbytrophies.model.market.Item
import kotlinx.android.synthetic.main.activity_item_sold.*

class ItemSoldActivity: AppCompatActivity(), ItemSoldView {

    override fun soldSuccess() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    lateinit var item : Item
    lateinit var presenter: ItemSoldPresenter

    override fun soldOutHobby(item: Item) {
        presenter.soldOutHobby(item)
    }

    override fun soldInHobby(item: Item, userMarket: UserMarket) {
        presenter.soldInHobby(item, userMarket)
    }

    override fun loadPossiblesBuyerSuccessful(userMarketList: List<UserMarket>) {
        recycler_view_possibles_buyers.adapter = PossibleBuyersAdapter(userMarketList){
            soldInHobby(item, it)
        }
        recycler_view_possibles_buyers.adapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_sold)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        item = intent.getParcelableExtra("ITEM")
        recycler_view_possibles_buyers.layoutManager = LinearLayoutManager(this)
        presenter = ItemSoldPresenter(applicationContext, this)
        presenter.loadPossiblesBuyers(item.id)
        text_view_sold_out_hobby.setOnClickListener({ soldOutHobby(this.item) })
    }

    override fun showMessageToast(message: String) {
        Toast.makeText(applicationContext, message,Toast.LENGTH_LONG).show()
    }
}
