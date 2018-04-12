package com.jx.demo.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Looper;

import com.jx.framework.log.XLogManager;
import com.tencent.bugly.crashreport.CrashReport;

import rx.Emitter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Cancellable;
import rx.schedulers.Schedulers;

/**
 * rxjava的辅助工具类，将一些常用的需要线程变换或注册监听的操作，使用rx封装
 */
public final class RxConvertHelper {
    private static final String TAG = RxConvertHelper.class.getName();

    public interface IntentDataFilter {
        /**
         * 判断是否接受该数据，算法必需简单，否则会影响性能
         */
        boolean accept(Intent intent);
    }

    /**
     * 返回一个处理广播接收器的Observable，在调用时会自动注册BroadcastReceiver，unSubscribe的时候会取消注册。<br/>
     * 参考：http://www.jianshu.com/p/8e29c2696396
     *
     * @param context      上下文对象
     * @param intentFilter 广播对应的IntentFilter
     * @param dataFilter   数据过滤器对象，只返回接收的数据。如果不需要过滤，传空即可。
     * @return 可接收广播事件的Observable
     */
    public static Observable<Intent> receiverObservable(final Context context, final IntentFilter intentFilter, IntentDataFilter dataFilter) {
        return Observable.create(new Action1<Emitter<Intent>>() {

            private BroadcastReceiver receiver;

            @Override
            public void call(final Emitter<Intent> intentEmitter) {
                context.registerReceiver(receiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        try {
                            if (dataFilter == null || dataFilter.accept(intent)) {
                                intentEmitter.onNext(intent);
                            }
                        } catch (Exception e) {
                            XLogManager.e(TAG, "处理广播结果失败", e);
                        }
                    }
                }, intentFilter);

                intentEmitter.setCancellation(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        context.unregisterReceiver(receiver);
                        XLogManager.d(TAG, "反注册广播监听器");
                    }
                });
                XLogManager.d(TAG, "注册广播监听器");
            }
        }, Emitter.BackpressureMode.BUFFER);
    }

    /**
     * 调用Presenter层的辅助方法
     *
     * @param runnable 需要在Presenter层执行的方法
     */
    public static void callPresenter(Runnable runnable) {
        Observable.just(runnable).observeOn(Schedulers.computation()).subscribe(new Action1<Runnable>() {
            @Override
            public void call(Runnable runnable) {
                try {
                    runnable.run();
                } catch (Throwable e) {
                    XLogManager.e(TAG, "调用Presenter层执行出错");
                    CrashReport.postCatchedException(e);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                XLogManager.e(TAG, "调用Presenter层框架出错");
                CrashReport.postCatchedException(throwable);
            }
        });
    }

    /**
     * 调用View层的辅助方法，如果当前线程就是主线程，直接执行
     *
     * @param runnable 需要在View层执行的方法
     */
    public static void callView(Runnable runnable) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            runnable.run();
            return;
        }

        Observable.just(runnable).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Runnable>() {
            @Override
            public void call(Runnable runnable) {
                try {
                    runnable.run();
                } catch (Throwable e) {
                    XLogManager.e(TAG, "调用View层执行出错");
                    CrashReport.postCatchedException(e);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                XLogManager.e(TAG, "调用View层框架出错");
                CrashReport.postCatchedException(throwable);
            }
        });
    }
}
