package com.marbit.hobbytrophies.market.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marbit.hobbytrophies.R
import com.marbit.hobbytrophies.market.model.*
import kotlinx.android.synthetic.main.list_possible_buyer.view.*

class PossibleBuyersAdapter(val items: List<UserMarket>, val listener: (UserMarket) -> Unit) : RecyclerView.Adapter<PossibleBuyersAdapter.UserMarketViewHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserMarketViewHolder(parent.inflate(R.layout.list_possible_buyer))

    override fun onBindViewHolder(holder: UserMarketViewHolder, position: Int) = holder.bind(items[position], listener)

    fun ViewGroup.inflate(layoutRes: Int): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, false)
    }

    class UserMarketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(userMarket: UserMarket, listener: (UserMarket) -> Unit) = with(itemView) {
            possible_buyer_name.text = userMarket.name
            setOnClickListener { listener(userMarket) }
        }
    }
}



