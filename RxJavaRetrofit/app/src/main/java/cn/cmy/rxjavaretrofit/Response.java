package cn.cmy.rxjavaretrofit;

public class Response {
    /**
     * {"success":true,
     * "AppConfig":{"PushKey":null,"AcceptCount":0,
     * "AppId":"sev20180730cp2","ShowWeb":"1","Del":"0",
     * "Url":"http:\/\/211app.com\/app\/android\/cpbangzy.apk",
     * "Remark":"接口说明：appid为唯一标示APP的字符串，调用前需要配置好。
     * 需要用到的返回值：【success】：布尔值，true 调用成功，false 请求失败
     * ，出错的情况一般就是appid传错了。【ShowWeb】：字符串，\"0\"不跳转，
     * \"1\"跳转【PushKey】：字符串，推送用的key【Url】：
     * 字符串 跳转的url地址。"}}
     */
    private boolean success;
    private AppConfig AppConfig;

    public static class AppConfig{

        private Object PushKey;
        private int AcceptCount;
        private String AppId;
        private String showWeb;
        private String Del;
        private String Url;
        private String Remark;

    }

    public String getUrl(){
        return AppConfig.Url;
    }
    public String getRemark(){
        return AppConfig.Remark;
    }






}
