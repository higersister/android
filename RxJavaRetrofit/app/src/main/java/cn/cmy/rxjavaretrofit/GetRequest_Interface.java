package cn.cmy.rxjavaretrofit;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface GetRequest_Interface {

   @GET("biz/getAppConfig?appid=sev20180730cp2")
    Observable<Response> getApkUrl();



}
