package cn.cmy.smartapp.model;

//m层
public class LoginModel {

    private final static String BASE_ADDRESS = "http://192.168.1.5:8080/SmartApp/smartServlet?";


    public void login(String userName, String password, final LoginResultCallBack resultCallBack) {
        new HttpTask(BASE_ADDRESS + getparamter(userName, password),
                "GET", new HttpCallBack() {
            @Override
            public void onSuccess(String data) {
                //json解析，...
                if (resultCallBack != null) {
                    resultCallBack.loginResult(data);
                }
            }

            @Override
            public void onFailure(Exception e) {
                if (resultCallBack != null) {
                    resultCallBack.loginResult(e.getMessage());
                }
            }
        }).start();

    }

    private String getparamter(String userName, String password) {

        return "userName=" + userName + "&password=" + password;
    }

}
