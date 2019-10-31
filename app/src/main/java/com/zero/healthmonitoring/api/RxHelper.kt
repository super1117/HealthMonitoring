package com.zero.healthmonitoring.api

import com.zero.healthmonitoring.data.BaseModel
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.ObservableTransformer



class RxHelper {

    companion object {

        fun <T> applySchedulers(): ObservableTransformer<BaseModel<T>, T> = ObservableTransformer { upstream ->
            upstream.flatMap { result ->
                when (result?.code) {
                    1 -> when (result.data) {
                        null -> Observable.error<T>(Exception(result.toString()))
                        else -> createData(result.data)
                    }
                    else -> Observable.error<T>(Exception(result?.msg))
                }
            }.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }

        private fun <T> createData(data: T): Observable<T> = Observable.create { subscriber ->
            try {
                subscriber.onNext(data)
                subscriber.onComplete()
            } catch (e: Exception) {
                subscriber.onError(e)
            }
        }


        fun <T> applySchedulersNoModel(): ObservableTransformer<T, T> = ObservableTransformer { upstream ->
            upstream.flatMap { result -> createData(result) }
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        }
    }

}