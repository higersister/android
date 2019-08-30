package com.cmy.mvp.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cmy.mvp.presenter.LoginPresenter
import com.cmy.test.R

class MvpActivity : BaseActivity<ILoginView, LoginPresenter>(), ILoginView {

    override fun onLoginResult(msg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Log.i("$$$$$$$$$$$$$", "msg:" + msg)
    }

    override fun provideV() = this

    override fun provideP() = LoginPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvp)
    }
}
