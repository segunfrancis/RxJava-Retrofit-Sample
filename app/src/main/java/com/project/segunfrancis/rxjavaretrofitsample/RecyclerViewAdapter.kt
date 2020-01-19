package com.project.segunfrancis.rxjavaretrofitsample

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.segunfrancis.rxjavaretrofitsample.pojo.Crypto
import kotlinx.android.synthetic.main.recyclerview_item_layout.view.*
import kotlin.collections.ArrayList

/**
 * Created by SegunFrancis
 */

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewViewHolder>() {

    private var marketList: List<Crypto.Market> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewViewHolder {
        return RecyclerViewViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_item_layout, parent, false)
        )
    }

    override fun getItemCount() = marketList.size

    override fun onBindViewHolder(holder: RecyclerViewViewHolder, position: Int) {
        holder.bind(marketList[position])
        val market = marketList[position]
        if (market.coinName.equals("eth", ignoreCase = true)) {
            holder.itemView.cardView.setBackgroundColor(Color.GRAY)
        } else {
            holder.itemView.cardView.setBackgroundColor(Color.GREEN)
        }
    }

    fun setData(data: List<Crypto.Market>) {
        (this.marketList as ArrayList).addAll(data)
        notifyDataSetChanged()
    }

    inner class RecyclerViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Crypto.Market) = with(itemView) {
            val market = marketList[adapterPosition]
            itemView.txtCoin.text = market.coinName
            itemView.txtMarket.text = market.market
            itemView.txtPrice.text = ("$" + String.format("%.2f", market.price.toDouble()))
        }
    }
}