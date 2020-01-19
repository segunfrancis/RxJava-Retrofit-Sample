package com.project.segunfrancis.rxjavaretrofitsample

import com.project.segunfrancis.rxjavaretrofitsample.pojo.Crypto
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by SegunFrancis
 */

interface CryptocurrencyService {

    @GET("{coin}-usd")
    fun getCoinData(@Path("coin") coin: String): Observable<Crypto>
}