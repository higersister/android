package cn.cmy.rxjavaretrofit;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IKuaiDi {

    @GET("query")
    Observable<KuaiDi> getKuaiDi(@Query("type")String type,@Query("postid")String postid);
}
