package cn.cmy.socket.demo;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {

    private Vector<Socket> sockets;


    public Server() {
        sockets = new Vector<>();
    }

    public void start() {
        try {

            ServerSocket serverSocket = new ServerSocket(6666);
            System.out.println("server start...");
            while (true) {
                Socket socket = serverSocket.accept();
                String info = socket.getInetAddress().
                        getHostAddress() + ":is connected...";
                System.out.println(info);
                /*Log.i("************", "start: " +
                        socket.getInetAddress().getHostAddress() +
                        "is connected...");*/
                sockets.add(socket);
                new MyThread(socket).start();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class MyThread extends Thread {

        private Socket socket;

        private PrintWriter writer;

        private BufferedReader reader;

        public MyThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            try {
                reader = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                String msg = null;
                while ((msg = reader.readLine()) != null) {
                    send(socket, msg);
                    System.out.println(msg);
                }


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                System.out.println(
                        socket.getInetAddress().getHostAddress() +
                                ":is exited...");
                sockets.remove(socket);
            }


        }

        private void send(Socket socket, String msg) {
            try {
                for (Socket s : sockets) {
                    if (s == socket) {
                        continue;
                    }
                    writer = new PrintWriter(
                            s.getOutputStream(), true);
                    writer.println(msg);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



}
