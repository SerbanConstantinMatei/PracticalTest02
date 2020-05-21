package ro.pub.cs.systems.eim.practical_test02;

import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread{
    private int port;
    private String address;
    private String hour;
    private String minute;
    private String action;
    private TextView textView;

    private Socket socket;

    public ClientThread(int port, String address, String hour, String minute, String action) {
        this.port = port;
        this.address = address;
        this.hour = hour;
        this.minute = minute;
        this.action = action;
    }

    private BufferedReader getReader(Socket socket) throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    private PrintWriter getWriter(Socket socket) throws IOException {
        return new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            socket = new Socket(this.address, this.port);
            if (socket == null) {
                return;
            }

            BufferedReader bufferedReader = getReader(socket);
            PrintWriter printWriter = getWriter(socket);

            if (bufferedReader == null || printWriter == null) {
                return;
            }

            printWriter.println(hour);
            printWriter.flush();
            printWriter.println(minute);
            printWriter.flush();
            printWriter.println(action);
            printWriter.flush();

            String alarmInfo;
            while ((alarmInfo = bufferedReader.readLine()) != null) {
                final String finalInfo = alarmInfo;
                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(finalInfo);
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

