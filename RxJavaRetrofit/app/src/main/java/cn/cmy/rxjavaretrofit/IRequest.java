package cn.cmy.rxjavaretrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface IRequest {

    @GET("app/android/cpbangzy.apk")
    Call<ResponseBody> download();

}
