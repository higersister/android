package com.cmy.mvp.model

import android.os.SystemClock
import java.lang.Exception
import kotlin.random.Random

class LoginModel {


    fun login(name: String, pwd: String, callback: LoginCallback) {
        object : Thread() {
            override fun run() {
                super.run()
                SystemClock.sleep(Random.Default.nextLong(2000))
                if (null == name) {
                    callback?.onError(Exception("name is null"))
                    return
                }
                if (null == pwd) {
                    callback?.onError(Exception("password is null"))
                    return
                }
                try {
                    callback?.onSuccess("ok.")
                } catch (e: Exception) {
                    callback?.onError(e)
                }
            }
        }.start()
    }



}