package com.jx.demo.network;

import com.jx.demo.network.exception.ReLoginException;
import com.jx.demo.network.exception.BusinessException;
import com.jx.demo.network.exception.ServerDataInvalidException;

import rx.Observable;
import rx.functions.Func1;

/**
 * 处理服务器请求成功时的返回结果，将结果去壳并将非成功情况抛出特定异常。利用单例减少重复对象创建<br/>
 * 参考http://www.jianshu.com/p/f3f0eccbcd6f
 */
public class RxResultHelper {

    public static <T> Observable.Transformer<BaseResult<T>, T> handleResult() {
        return new Observable.Transformer<BaseResult<T>, T>() {
            @Override
            public Observable<T> call(Observable<BaseResult<T>> tObservable) {
                return tObservable.flatMap(
                        new Func1<BaseResult<T>, Observable<T>>() {
                            @Override
                            public Observable<T> call(BaseResult<T> result) {
                                // TODO 换成自己平台的公共结果处理
                                int code = result.getCode();
                                if (code == ResultCode.CODE_SUCCESS) {
                                    T realResult = result.getResult();
                                    if (realResult instanceof ResultValidator) {
                                        // 校验结果有效性
                                        String invalidMsg = ((ResultValidator) realResult).checkIsValid();
                                        if (invalidMsg != null) {
                                            return Observable.error(new ServerDataInvalidException(realResult.getClass().getName(), invalidMsg));
                                        }
                                    }
                                    return Observable.just(result.getResult());
                                } else if (code == ResultCode.TOKEN_INVALID) {
                                    // 处理token无效情况
                                    return Observable.error(new ReLoginException(result.getMsg()));
                                } else {
                                    return Observable.error(new BusinessException(code, result.getMsg()));
                                }
                            }
                        }
                );
            }
        };
    }
}
