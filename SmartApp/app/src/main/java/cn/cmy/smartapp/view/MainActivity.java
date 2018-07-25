package cn.cmy.smartapp.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cn.cmy.smartapp.R;
import cn.cmy.smartapp.persenter.LoginPresenter;

public class MainActivity extends BaseActivity<LoginView,LoginPresenter> implements LoginView {

    private Button mBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {

        mBtnLogin = findViewById(R.id.btn_login);
       mBtnLogin.setOnClickListener(view->{

           getP().login("admin","admin");
       });

    }

    @Override
    public void onResult(String result) {
     //   Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
        Log.i("---------login", "onResult: result:" + result);
    }

    @Override
    public LoginPresenter createP() {
        return new LoginPresenter();
    }

    @Override
    public LoginView createV() {
        return this;
    }
}
