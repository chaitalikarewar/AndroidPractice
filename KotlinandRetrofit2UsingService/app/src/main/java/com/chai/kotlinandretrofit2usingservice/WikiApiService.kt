package com.chai.kotlinandretrofit2usingservice

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WikiApiService {
//https://en.wikipedia.org/w/api.php?action=query&format=json&list=search&srsearch=Chaitali

    @GET("api.php")
    fun hitCountCheck(
        @Query("action") action: String,
        @Query("format") format: String,
        @Query("list") list: String,
        @Query("srsearch") srsearch: String
    ): Observable<Data.Result> /*Observable, which is a Rxjava object that could analog as
    the endpoint fetcher result generator.*/

    /*f you need to write a function that can be called without having a class instance but
    needs access to the internals of a class, you can write it as a member of a companion object
    declaration inside that class
     */
    companion object {
        fun create(): WikiApiService {

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://en.wikipedia.org/w/")
                .build()

            return retrofit.create(WikiApiService::class.java)
        }
    }
}