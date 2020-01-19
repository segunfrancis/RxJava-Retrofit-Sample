package com.project.segunfrancis.rxjavaretrofitsample.pojo

import com.google.gson.annotations.SerializedName

/**
 * Created by SegunFrancis
 */

data class Crypto(
    @SerializedName("ticker") var ticker: Ticker,
    @SerializedName("timestamp") var timestamp: Int,
    @SerializedName("success") var success: Boolean,
    @SerializedName("error") var error: String
) {
    data class Market(
        @SerializedName("market") var market: String,
        @SerializedName("price") var price: String,
        @SerializedName("volume") var volume: Float,
        var coinName: String
    )

    data class Ticker(
        @SerializedName("base") var base: String,
        @SerializedName("target") var target: String,
        @SerializedName("price") var price: String,
        @SerializedName("volume") var volume: String,
        @SerializedName("change") var change: String,
        @SerializedName("markets") var markets: List<Market>? = null
    )
}