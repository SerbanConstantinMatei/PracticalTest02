package ro.pub.cs.systems.eim.practical_test02;

import android.text.BoringLayout;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerThread extends Thread {
    private int port;
    private ServerSocket serverSocket;
    private HashMap<String, Alarm> data;

    public ServerThread(int port) {
        this.port = port;
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            Log.e(Constants.TAG, Constants.SERVER_LOG + "could not open server socket");
            e.printStackTrace();
        }

        data = new HashMap<>();
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public synchronized void setData(String client, Alarm alarm) {
        this.data.put(client, alarm);
    }

    public synchronized void removeData(String client) {
        this.data.remove(client);
    }

    public synchronized HashMap<String, Alarm> getData() {
        return data;
    }

    public synchronized Boolean checkData(String client) {
        return data.containsKey(client);
    }


    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Log.i(Constants.TAG, Constants.SERVER_LOG + "waiting for connections");
                Socket socket = serverSocket.accept();
                Log.i(Constants.TAG, Constants.SERVER_LOG + "received connection request from " + socket.getInetAddress());
                Communication communication = new Communication(this, socket);
                communication.start();
            }
        } catch (IOException e) {
            Log.e(Constants.TAG, Constants.SERVER_LOG + "could not open new socket");
            e.printStackTrace();
        }
    }

    public void stopThread() {
        interrupt();
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                Log.e(Constants.TAG, Constants.SERVER_LOG + "error when closing server socket");
                e.printStackTrace();
            }
        }
    }

}