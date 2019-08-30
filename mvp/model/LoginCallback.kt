package com.cmy.mvp.model

import java.lang.Exception

interface LoginCallback {
    fun onSuccess(msg:String)
    fun onError(e:Exception)
}