package com.smilehacker.iocast.util

import rx.Observable
import rx.subjects.PublishSubject
import rx.subjects.SerializedSubject

/**
 * Created by kleist on 16/3/18.
 */
object RxBus {

    private val bus = SerializedSubject(PublishSubject.create<Any>())

    fun post(o: Any) {
        bus.onNext(o)
    }

    fun <T : Any> toObservable(eventType : Class<T>) : Observable<T> {
        return bus.filter { t -> eventType.isInstance(t) }
                .cast(eventType)
    }
}
