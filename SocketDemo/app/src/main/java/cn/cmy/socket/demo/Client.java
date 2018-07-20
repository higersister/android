package cn.cmy.socket.demo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private Socket socket;

    private Handler handler;

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Client(Handler handler) {
        this.handler = handler;
    }

    public Client() {

    }

    public void start() {
           new Thread(()->{
               try {
                   socket = new Socket("192.168.1.3",
                           6666);
                   receive(socket.getInputStream());
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }).start();
            System.out.println("client start...");
            msg = "";


    }

    public void send() {
        try {
            new SendThread(new PrintWriter(
                    socket.getOutputStream(),
                    true)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receive(InputStream in){

            new ReceiveThread(new BufferedReader(
                    new InputStreamReader(in))).start();

    }

    public void shutdown(){
        try {
            socket.shutdownOutput();
            socket.shutdownInput();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class SendThread extends Thread {

        private PrintWriter writer;

        public SendThread(PrintWriter writer) {
            this.writer = writer;
        }

        @Override
        public void run() {

            writer.println(getMsg());
           Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("info",getMsg());
            bundle.putInt("type",Info.TYPE_SEND);
            message.setData(bundle);
            handler.sendMessage(message);

        }
    }

    class ReceiveThread extends Thread {

        private BufferedReader reader;

        public ReceiveThread(BufferedReader reader){
            this.reader = reader;
        }
        @Override
        public void run() {
            String str = null;
            try {
                while ((str = reader.readLine()) != null) {
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("info",str);
                    bundle.putInt("type",Info.TYPE_RECEIVE);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
