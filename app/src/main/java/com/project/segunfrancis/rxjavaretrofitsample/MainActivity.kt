package com.project.segunfrancis.rxjavaretrofitsample

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.project.segunfrancis.rxjavaretrofitsample.pojo.Crypto
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    lateinit var retrofit: Retrofit

    companion object {
        const val BASE_URL = "https://api.cryptonator.com/api/full/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerViewAdapter = RecyclerViewAdapter()
        recyclerView.adapter = recyclerViewAdapter

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        callEndPoints()
    }

    @SuppressLint("CheckResult")
    private fun callEndPoints() {
        val cryptocurrencyService = retrofit.create(CryptocurrencyService::class.java)

        //Single call
        /*Observable<Crypto> cryptoObservable = cryptocurrencyService.getCoinData("btc");
        cryptoObservable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).map(result -> result.ticker).*/

        val btcObservable = cryptocurrencyService.getCoinData("btc")
            .map { result -> Observable.fromIterable(result.ticker.markets) }
            .flatMap { x -> x }
            .filter { y ->
                y.coinName = "btc"
                return@filter true
            }.toList().toObservable()

        val ethObservable = cryptocurrencyService.getCoinData("eth")
            .map { result -> Observable.fromIterable(result.ticker.markets) }
            .flatMap { x -> x }
            .filter { y ->
                y.coinName = "eth"
                return@filter true
            }.toList().toObservable()

        Observable.merge(btcObservable, ethObservable)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleResults, this::handleError)
    }

    private fun handleResults(marketList: List<Crypto.Market>) {
        if (marketList != null && marketList.isNotEmpty()) {
            recyclerViewAdapter.setData(marketList)
        } else {
            Toast.makeText(this, "No results found", Toast.LENGTH_LONG).show()
        }
    }

    private fun handleError(t: Throwable) {
        Log.d("MainActivity", "Error: " + t.localizedMessage)
        Toast.makeText(this, "Error in fetching API response.\nTry again", Toast.LENGTH_LONG).show()
    }
}
