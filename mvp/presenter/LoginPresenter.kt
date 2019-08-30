package com.cmy.mvp.presenter

import com.cmy.mvp.model.LoginCallback
import com.cmy.mvp.model.LoginModel
import com.cmy.mvp.view.ILoginView
import java.lang.Exception

class LoginPresenter : BasePresenter<ILoginView>() {

    var model: LoginModel? = null

    init {
        model = LoginModel()
    }

    fun login(name: String, pwd: String) {
        model?.login(name, pwd, object : LoginCallback {
            override fun onSuccess(msg: String) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                v?.onLoginResult(msg)
            }

            override fun onError(e: Exception) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                v?.onLoginResult(e.message.toString())
            }
        })
    }


}