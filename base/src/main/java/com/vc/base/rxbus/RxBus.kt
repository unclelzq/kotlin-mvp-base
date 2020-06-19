package com.vc.base.rxbus

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

/**
 * @author Donkor
 */
class RxBus private constructor() {

    private val mBus: Subject<Any> = PublishSubject.create<Any>().toSerialized()

    init {
        // toSerialized method made bus thread safe
    }

    fun post(obj: Any) {
        mBus.onNext(obj)
    }

    fun <T> toObservable(tClass: Class<T>): Observable<T> {
        return mBus.ofType(tClass)
    }

    fun toObservable(): Observable<Any> {
        return mBus
    }

    fun hasObservers(): Boolean {
        return mBus.hasObservers()
    }

    private object Holder {
        val BUS = RxBus()
    }

    companion object {
        fun get(): RxBus {
            return Holder.BUS
        }
    }
}