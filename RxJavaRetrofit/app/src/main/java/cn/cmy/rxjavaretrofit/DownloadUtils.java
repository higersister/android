package cn.cmy.rxjavaretrofit;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DownloadUtils {

    public static final String TAG = "$$$$$$$$$";

    private Retrofit retrofit;

    private DownLoadListener downLoadListener;

    private String url;

    private String filePath;

    public DownloadUtils(String url,String filePath, DownLoadListener downLoadListener) {
        this.url = url + "/";
        this.filePath = filePath;
        this.downLoadListener = downLoadListener;
        retrofit = new Retrofit.Builder().baseUrl(this.url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public void downLoad() {
        downLoadListener.onStart();
        // subscribeOn()改变调用它之前代码的线程
        // observeOn()改变调用它之后代码的线程
        retrofit.create(DownloadService.class)
                .download(this.url).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, InputStream>() {
                    @Override
                    public InputStream apply(ResponseBody responseBody) throws Exception {
                        return responseBody.byteStream();
                    }
                }).observeOn(Schedulers.computation())//用于计算任务
                .doOnNext(new Consumer<InputStream>() {
                    @Override
                    public void accept(InputStream inputStream) throws Exception {
                        writeFile(inputStream);
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }


    private void writeFile(InputStream in) {
        File file = new File(filePath + "/cpbangzy.apk");
        if (!file.exists()) {
            file.mkdirs();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = in.read(b)) != -1) {

                fos.write(b,0,len);
            }
            in.close();
            fos.close();


        } catch (FileNotFoundException e) {
            downLoadListener.onFailue(e.getMessage());
        } catch (IOException e) {
            downLoadListener.onFailue(e.getMessage());
        }
    }


}
