package cn.cmy.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import cn.cmy.view.view.CheckView;
import cn.cmy.view.view.FloatingMenu;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "$$$$$$$$$$$$$";
    /* private Button mBtnAdd;
         private boolean mBound;
         private ICarManager mManager;
         private final ServiceConnection conn = new ServiceConnection() {
             @Override
             public void onServiceConnected(ComponentName name, IBinder service) {
                 mBound = true;
                 mManager = ICarManager.Stub.asInterface(service);
             }

             @Override
             public void onServiceDisconnected(ComponentName name) {
                 mBound = false;
                 mManager = null;
             }
         };*/
    //  private BezierView mBezier;
    //  private FloatingMenu mMenu;
    private ImageView mImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // init();
        //initView();
        /*mMenu = findViewById(R.id.floating_menu);
        mMenu.setSubMenuListener((v, position) -> {
            Toast.makeText(this, "click:" + position, Toast.LENGTH_SHORT).show();
        });
        CheckView checkView = findViewById(R.id.circle_view);
        *//*circleView.setOnClickListener(v -> {
            circleView.startAnim();
        });*//*
        findViewById(R.id.btn_change).setOnClickListener(v -> {
            checkView.setChecked(!checkView.isChecked());
        });*/
        /*ImageView img = findViewById(R.id.img);
        img.setOnClickListener(view -> {
            Drawable drawable = img.getDrawable();
            if (drawable instanceof Animatable) {
                ((Animatable) drawable).start();
            }
        });*/

        init();
    }

    private void init() {
        mImg = findViewById(R.id.by_img);
        new Thread(() -> {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.imac);
            Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, 768, 500, false);
            runOnUiThread(() -> mImg.setImageBitmap(bitmap1));
        }).start();
    }

    /*private void init() {
        mBezier = findViewById(R.id.bezier_view);
        mBezier.setProgress(10);
        mBezier.setTextChangeListener(new BezierView.OnTextChangeListener() {
            @Override
            public String onTextChange(double percent, float maxValue) {
                return String.format("%.2f%%", percent * maxValue);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 100; i++) {
                    final int finalI = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mBezier.setProgress(finalI);
                        }
                    });
                    SystemClock.sleep(30);
                }
            }
        }).start();

    }*/

    /*private void initView() {
        mBtnAdd = findViewById(R.id.btn_add);

        mBtnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                if (null == mManager) {
                    bind();
                    Toast.makeText(this, "binding.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Car car1 = new Car();
                car1.name = "雷诺1";
                car1.price = 8001;
                Car car2 = new Car();
                car2.name = "雷诺2";
                car2.price = 8002;
                Car car3 = new Car();
                car3.name = "雷诺3";
                car3.price = 8003;
                try {
                    Car car4 = mManager.addCarIn(car1);
                    Car car5 = mManager.addCarOut(car2);
                    Car car6 = mManager.addCarInOut(car3);
                    Log.i("###########", "onClick: car1:" + car1.toString());
                    Log.i("###########", "onClick: car2:" + car2.toString());
                    Log.i("###########", "onClick: car3:" + car3.toString());

                } catch (RemoteException e) {
                    Log.i("$$$$$$$$$4", "onClick: " + e.getMessage());
                }
                break;
        }
    }

    private void bind() {
        Intent intent = new Intent();
        intent.setPackage("cn.cmy.view");
        intent.setAction("cn.intent.action.CARSERVICE");
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(conn);
        }
    }*/
}
