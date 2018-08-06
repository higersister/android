package cn.cmy.rxjavaretrofit;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "$$$$$$$$$$";

    Observable<Response> observable;

    private Button mBtnStartDownLoad;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //   init();
        init1();
    }

    private void init1() {
        mBtnStartDownLoad = findViewById(R.id.btn_start_download);
        mBtnStartDownLoad.setOnClickListener(view -> {
            //首先要在你要接受EventBus的界面注册，这一步很重要
//            EventBus.getDefault().register(this);
            startActivity(new Intent(MainActivity.this, OprationsymbolActivity.class));
        });
        Observable.just(new TestType(null, "a", 1),
                new TestType(true, "b", 2),
                new TestType(0.3, "c", 3)).
                subscribe(new Observer<TestType>() {

                    private Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "onSubscribe: 建立连接");
                        disposable = d;
                    }

                    @Override
                    public void onNext(TestType value) {
                        Log.i(TAG, "onNext: " + value.test1);
                        Log.i(TAG, "onNext: " + value.test2);
                        Log.i(TAG, "onNext: " + value.test3);
                        disposable.dispose();

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete: ");
                    }
                });



       /* Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                Log.i(TAG, "subscribe: send a");
                e.onNext("a");
                Log.i(TAG, "subscribe: send b");

                e.onNext("b");
                Log.i(TAG, "subscribe: send c");

                e.onNext("c");
                Log.i(TAG, "subscribe: send d");
                e.onError(new NullPointerException("null "));
                e.onNext("d");
                e.onComplete();
            }

        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "onSubscribe: 建立连接");
            }

            @Override
            public void onNext(String value) {
                Log.i(TAG, "onNext: 处理事件:" + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: ");
            }
        });*/


    }
    //任意写一个方法，给这个方法一个@Subscribe注解，
    // 参数类型可以自定义，但是一定要与你发出的类型相同
   /* @Subscribe(threadMode = ThreadMode.MAIN)//指定线程模式
    public void getEventBus(Integer i) {
        if (i != null) {
            Toast.makeText(this, "" + i, Toast.LENGTH_SHORT).show();
        }
    }*/


    @Override
    protected void onDestroy() {
        //在界面销毁的地方要解绑
     //   EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void init() {
        mBtnStartDownLoad = findViewById(R.id.btn_start_download);
        runOnUiThread(() -> {
            Loading.show(this);
        });
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl("http://201888888888.com:8080/").
                addConverterFactory(GsonConverterFactory.create()).
                addCallAdapterFactory(RxJava2CallAdapterFactory.create()).
                build();
        GetRequest_Interface requset = retrofit.create(GetRequest_Interface.class);
        observable = requset.getApkUrl();
        observable.subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<Response>() {
                    @Override
                    public void accept(Response response) throws Exception {
                        url = response.getUrl();
                        Log.i("$$$$$$$$", "accept: url:" + response.getUrl());
                        Log.i("$$$$$$$$", "accept: remark:" + response.getRemark());
                        Loading.hide();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("$$$$$$$$", "accept: failue...");
                    }
                });
    }
}
