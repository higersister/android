package cn.cmy.rxjavaretrofit;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class CmyDownloadInterceptor implements Interceptor {

    private DownLoadListener downLoadListener;

    public CmyDownloadInterceptor(DownLoadListener downLoadListener){
        this.downLoadListener = downLoadListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        return response.newBuilder().body(new CmyResponseBody(response.body(),
                downLoadListener)).build();
    }
}
