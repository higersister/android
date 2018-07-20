package cn.cmy.socket.demo;

public class Info {

    public static final int TYPE_SEND = 0;

    public static final int TYPE_RECEIVE = 1;

    public String info;

    public int type;

    public Info(String info, int type) {
        this.info = info;
        this.type = type;
    }

}
