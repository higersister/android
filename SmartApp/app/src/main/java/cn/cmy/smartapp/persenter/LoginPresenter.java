package cn.cmy.smartapp.persenter;

import cn.cmy.smartapp.model.LoginModel;
import cn.cmy.smartapp.model.LoginResultCallBack;
import cn.cmy.smartapp.view.LoginView;

//p层
//特点1：持有m层的引用
//特点2：持有v层的引用
//特点3：对m层和v层进行关联
public class LoginPresenter extends BasePresenter<LoginView> {

    private LoginModel loginModel;

    public LoginPresenter() {
        loginModel = new LoginModel();
    }

    public void login(String userName, String password) {
        loginModel.login(userName, password, new LoginResultCallBack() {
            @Override
            public void loginResult(String result) {
                if (getV() != null) {
                    getV().onResult(result);
                }
            }
        });
    }


}
