package com.jx.demo.business.login.service;

import com.jx.demo.business.login.service.entity.LoginResult;
import com.jx.demo.business.login.service.entity.UserInfoResult;
import com.jx.demo.network.Api;
import com.jx.demo.network.BaseResult;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * 登录服务，包含登录业务相关接口定义，默认情况下，回调发生在计算线程
 */
public interface LoginService {

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST(Api.LOGIN)
    Observable<BaseResult<LoginResult>> login(@FieldMap() Map<String, Object> params);

    @GET(Api.GET_USER_INFO)
    Observable<BaseResult<UserInfoResult>> getUserInfo(@QueryMap Map<String, Object> params);
}
