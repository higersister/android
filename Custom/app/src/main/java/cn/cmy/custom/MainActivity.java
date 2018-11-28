package cn.cmy.custom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.cmy.custom.dao.UserDao;
import cn.cmy.custom.model.UserModel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView tv;
    private Button btn_insert;
    private Button btn_query;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        userDao = new UserDao(this);
        tv = (TextView) findViewById(R.id.tv);
        btn_insert = (Button) findViewById(R.id.btn_insert);
        btn_query = (Button) findViewById(R.id.btn_query);

        btn_insert.setOnClickListener(this);
        btn_query.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_insert:
                for (int i = 0; i < 10; i++) {
                    userDao.insert(new UserModel("name:" + i, "pwd:" + i, "email:" + i));
                }
                break;
            case R.id.btn_query:
                EventBus.getDefault().post(userDao.query());
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(List<UserModel> list) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i).toString());
            builder.append("\n");
        }
        tv.setText(builder.toString());
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
