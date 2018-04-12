package com.jx.demo.network;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * 线程调度工具类，在io线程上请求网络，computation线程上处理结果
 */
public class RxSchedulersHelper {

    public static <T> Observable.Transformer<T, T> ioComputation() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.computation());
            }
        };
    }

}
