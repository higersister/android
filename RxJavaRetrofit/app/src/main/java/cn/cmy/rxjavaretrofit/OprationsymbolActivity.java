package cn.cmy.rxjavaretrofit;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OprationsymbolActivity extends AppCompatActivity {

    public final static String TAG = "$$$$$$$$$";
    //http://211app.com/app/android/cpbangzy.apk

    private Button mBtnStartDownLoad;

    private IRequest iRequest;

    private Call<ResponseBody> call;

    private Retrofit retrofit;

    private TextView mTvKuaiDiInfo;

    private Button mBtnTestEventBus;

    private ProgressBar mProgressBar;

    private Button mBtnTestRxJava;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oprationsymbol);
        // init();
        //requestPermissions();
        // init1();
        //  retrofit();
        //  retrofitAndRxjava();
        EventBus.getDefault().register(this);
        retrofitAndRxjava1();
    }

    private void retrofitAndRxjava1() {

        mBtnTestRxJava = findViewById(R.id.btn_test_rxjava);
        mBtnTestRxJava.setOnClickListener(view -> {
            Observable.create(new ObservableOnSubscribe<Integer>() {
                @Override
                public void subscribe(ObservableEmitter<Integer> e) throws Exception {

                    int i = 0;
                    while (i <= 100) {
                        e.onNext(i++);
                        SystemClock.sleep(200);
                    }
                    e.onComplete();

                }
            }).subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<Integer>() {
                @Override
                public void onSubscribe(Disposable d) {
                    Log.i(TAG, "onSubscribe: subscribe");
                }

                @Override
                public void onNext(Integer value) {
                    mProgressBar.setProgress(value);
                }

                @Override
                public void onError(Throwable e) {
                    Log.i(TAG, "onError: " + e.getMessage());
                }

                @Override
                public void onComplete() {
                    Log.i(TAG, "onComplete: ");
                }
            });
        });


        mBtnTestEventBus = findViewById(R.id.btn_test_eventbus);
        mProgressBar = findViewById(R.id.progress_bar);
        mBtnTestEventBus.setOnClickListener(v -> {

            new Thread(() -> {

                int i = 0;
                while (i <= 100) {
                    EventBus.getDefault().post(i++);
                    SystemClock.sleep(200);
                }


            }).start();
        });

        mTvKuaiDiInfo = findViewById(R.id.tv_kuaidi_info);
        mBtnStartDownLoad = findViewById(R.id.btn_start_download1);
        mBtnStartDownLoad.setOnClickListener(view -> {
            new Thread(() -> {
                EventBus.getDefault().post(666666);
                finish();
            }).start();
        });
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.kuaidi100.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        IKuaiDi iKuaiDi = retrofit.create(IKuaiDi.class);
        iKuaiDi.getKuaiDi("yuantong", "11111111111")
                //工作线程
                .subscribeOn(Schedulers.io())
                //切换到主线程进行ui操作
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<KuaiDi>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "onSubscribe: subscribe");
                    }

                    @Override
                    public void onNext(KuaiDi value) {
                        mTvKuaiDiInfo.setText(value.getKuaiDiInfo());
                        value.show();
                        List<KuaiDi.data> list = value.getData();
                        for (KuaiDi.data data : list) {
                            Log.i(TAG, "onNext: data.time:" + data.time);
                            Log.i(TAG, "onNext: data.ftime:" + data.ftime);
                            Log.i(TAG, "onNext: data.context:" + data.context);
                            Log.i(TAG, "onNext: data.location:" + data.location);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError: " + e.getMessage());

                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete: ");
                    }
                });


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventBus(Integer i) {
        mProgressBar.setProgress(i);
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void retrofitAndRxjava() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.douban.com/v2/movie/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        IApiService iApiService = retrofit.create(IApiService.class);
        Observable<Movie> observable = iApiService.getTopMovie(0, 10);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Movie>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "onSubscribe: subscribe");
                    }

                    @Override
                    public void onNext(Movie value) {

                        value.show();

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete: ");
                    }
                });

    }

    private void retrofit() {

        //创建retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fy.iciba.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //创建网络接口的实例
        GetRequestInterface request = retrofit.create(GetRequestInterface.class);
        Call<Translation> call = request.getCall();
        //发送网络请求(异步)
        call.enqueue(new Callback<Translation>() {
            @Override
            public void onResponse(Call<Translation> call, Response<Translation> response) {

                Log.e(TAG, "onResponse: " + response.body());
                Translation translation = response.body();
                translation.show();

            }

            @Override
            public void onFailure(Call<Translation> call, Throwable t) {

            }
        });


    }

    private void init1() {
        mBtnStartDownLoad = findViewById(R.id.btn_start_download1);
        retrofit = new Retrofit.Builder().
                baseUrl("http://211app.com/").
                addConverterFactory(GsonConverterFactory.create()).
                addCallAdapterFactory(RxJava2CallAdapterFactory.create()).
                build();
        iRequest = retrofit.create(IRequest.class);

        mBtnStartDownLoad.setOnClickListener(view -> {

            call = iRequest.download();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.isSuccessful()) {
                        Log.i(TAG, "onResponse: server contacted and has file");
                        Log.i(TAG, "onResponse: file download is success?" +
                                writeResponseBodyOnDisk(response.body()));
                    } else {
                        Log.d(TAG, "server contact failed");
                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.i(TAG, "onFailure: ");
                }
            });


        });

    }

    private boolean writeResponseBodyOnDisk(ResponseBody responseBody) {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/cpbangzy.apk");
        if (!file.exists()) {
            file.mkdirs();
        }
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            byte[] bytes = new byte[4096];
            long fileSize = responseBody.contentLength();
            long fileDownloaded = 0;
            inputStream = responseBody.byteStream();
            outputStream = new FileOutputStream(file);
            while (true) {
                int read = inputStream.read(bytes);
                if (read == -1) {
                    break;
                }
                outputStream.write(bytes, 0, read);
                fileDownloaded += read;
                Log.i(TAG, "writeResponseBodyOnDisk: down:" + fileDownloaded + " of " +
                        fileSize);
            }
            outputStream.flush();


        } catch (IOException e) {
            return false;
        } finally {
            try {
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                return false;
            }

        }
        return true;
    }


    private void init() {
        // merge（）：组合多个被观察者（＜4个）一起发送数据
        // 注：合并后按照时间线并行执行
        Observable.merge(
                // 从0开始发送、共发送3个数据、第1次事件延迟发送时间 = 1s、间隔时间 = 1s
                Observable.intervalRange(0,
                        3, 1, 1, TimeUnit.SECONDS),
                // 从2开始发送、共发送3个数据、第1次事件延迟发送时间 = 1s、间隔时间 = 1s
                Observable.intervalRange(2,
                        3, 1, 1, TimeUnit.SECONDS)).
                subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "onSubscribe: subscribe");
                    }

                    @Override
                    public void onNext(Long value) {
                        Log.i(TAG, "onNext: " + value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete: ");
                    }
                });


        // concat（）：组合多个被观察者（≤4个）一起发送数据
        // 注：串行执行
        // concatArray（）：组合多个被观察者一起发送数据（可＞4个）
        /*Observable.concat(Observable.just(1,2,3),Observable.just(4,5,6),
                Observable.just(7,8,9),Observable.just(10,11,12))
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "onSubscribe: subscribe");
                    }

                    @Override
                    public void onNext(Integer value) {
                        Log.i(TAG, "onNext: " + value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete: ");
                    }
                });


*/


        /*Observable.create(e -> {
            e.onNext(1);
            e.onNext(2);
            e.onNext(3);
            e.onComplete();
        }).flatMap(interger -> {
            final List<String> list = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                list.add("我是事件：" + interger + "拆分后的子事件：" + i);
            }
            return Observable.fromIterable(list);
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "onSubscribe: subscribe");
            }

            @Override
            public void onNext(String value) {
                Log.i(TAG, "onNext: " + value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: ");
            }
        });
*/





        /*Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
            }
        }).map(new Function<Integer, String>() {

            @Override
            public String apply(Integer integer) throws Exception {
                return integer + "转换为字符串:" + integer;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "onSubscribe: subscribe");
            }

            @Override
            public void onNext(String value) {
                Log.i(TAG, "onNext: " + value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: ");
            }
        });
        
        */


    }

    private void requestPermissions() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        } else {
            init1();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "拒绝将无法使用", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    init1();
                }


                break;
        }


    }


}
