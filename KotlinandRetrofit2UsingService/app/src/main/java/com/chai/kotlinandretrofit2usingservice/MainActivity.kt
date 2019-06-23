package com.chai.kotlinandretrofit2usingservice

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    /*  global variables*/
    /*The lazy Kotlin feature comes in really handy without
    the need of you writes a function explicitly to do the lazy initialization.
     */
    private var disposable: Disposable? = null

    private val wikiApiServe by lazy {
        WikiApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_search.setOnClickListener {
            if (edit_search.text.toString().isNotEmpty()) {
                beginSearch(edit_search.text.toString())
            }
        }
    }

   /* 1.wikiApiServe is the singleton service, where hitCountCheck will return an Observable.
    2. The Observable, since it’s like a endpoint fetcher result generator,
    we tell it to fetch the data on background by subscribeOn(Schedulers.io())
    3.However, we like the fetched data to be displayed on the MainTread (UI) so
    we set observeOn(AndroidSchedulers.mainThread())
    4.In term of what we do with the result,
    we use subscribe to define our action on the result where by the result is the fetched data result,
    where we could access the totalhits data accordingly.
    In the event of error occurs, it will return error instead to be handled.*/
    private fun beginSearch(searchString: String) {
        disposable = wikiApiServe.hitCountCheck("query", "json", "search", searchString)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> txt_search_result.text = "${result.query.searchinfo.totalhits} result found" },
                { error -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show() }
            )
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }
}
