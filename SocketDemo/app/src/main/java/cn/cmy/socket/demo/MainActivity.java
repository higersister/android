package cn.cmy.socket.demo;

import android.app.NotificationManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button mBtnSend;

    private EditText mEtMsg;

    private TextView mTvCurrentNumInfo;

    private Client mClient;

    private RecyclerView mRecyclerView;

    private MyAdpter mAdpter;

    private List<Info> mLists;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            Bundle bundle = msg.getData();
            Info info = new Info(bundle.getString("info"),
                    bundle.getInt("type"));
            Log.i("********", "handleMessage: info:" + bundle.getString("info"));
            mLists.add(info);
//            mTvCurrentNumInfo.setText(msg.obj.toString());
            mEtMsg.setText("");
            mAdpter.notifyDataSetChanged();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        mLists = new ArrayList<>();
        mClient = new Client(mHandler);
        mClient.start();
        mBtnSend = findViewById(R.id.btn_send);
        mEtMsg = findViewById(R.id.et_msg);
        mTvCurrentNumInfo = findViewById(R.id.tv_current_num);
        mRecyclerView = findViewById(R.id.recycler_view);
        mAdpter = new MyAdpter(mLists,getApplicationContext());
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(MainActivity.this,
                        LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdpter);
        mBtnSend.setOnClickListener(view -> {
            String msg = mEtMsg.getText().toString();
            if (msg == null || msg.length() == 0) {
                return;
            }
            mClient.setMsg(msg);
            mClient.send();


        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mClient.shutdown();
    }
}
