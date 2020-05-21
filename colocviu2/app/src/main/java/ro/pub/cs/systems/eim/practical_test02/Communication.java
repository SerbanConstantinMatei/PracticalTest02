package ro.pub.cs.systems.eim.practical_test02;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Communication extends Thread{
    private ServerThread serverThread;
    private Socket socket;

    public Communication(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            Log.e(Constants.TAG, Constants.COMM_LOG + "Null socket");
            return;
        }

        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);

            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, Constants.COMM_LOG + "null buffered reader / print writer");
            }

            Log.i(Constants.TAG, Constants.COMM_LOG + "waiting for client data");
            String hour = bufferedReader.readLine();
            String minute = bufferedReader.readLine();
            String action = bufferedReader.readLine();
            Log.i(Constants.TAG, Constants.COMM_LOG + "acquired data");

            String client = socket.toString();

            switch (action) {
                case Constants.SET:
                    Alarm alarm = new Alarm(hour, minute);
                    serverThread.setData(client, alarm);
                    Log.i(Constants.TAG, Constants.COMM_LOG + alarm.toString());
                    printWriter.write(alarm.toString());
                    printWriter.flush();
                    break;

                case Constants.RESET:
                    serverThread.removeData(client);
                    Log.i(Constants.TAG, Constants.COMM_LOG + "alarm reset");
                    printWriter.write("alarm reset");
                    printWriter.flush();
                    break;

                case Constants.POLL:
                    if (!serverThread.checkData(client)) {
                        Log.i(Constants.TAG, Constants.COMM_LOG + "no alarm set");
                    }

                    Socket site_socket = new Socket(Constants.SITE_ADDRESS, Constants.SITE_PORT);
                    BufferedReader reader = Utilities.getReader(site_socket);
                    String line = reader.readLine();
                    while (line == null || line.isEmpty()) {
                        line = reader.readLine();
                    }

                    Date date = new SimpleDateFormat("yy-MM-dd HH:mm:ss").parse(line.substring(5, 22));
                    break;
            }



        } catch (IOException | ParseException e) {
            Log.e(Constants.TAG, Constants.COMM_LOG + "Exception occurred");
            e.printStackTrace();
        }
    }
}
