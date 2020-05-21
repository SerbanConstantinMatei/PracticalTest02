package ro.pub.cs.systems.eim.practical_test02;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private EditText clientPort, hour, minute, address;
    private Button start, set, reset, poll;
    private ServerThread serverThread;
    private ClientThread clientThread;

    private ClientListener clientListener = new ClientListener();
    private class ClientListener implements Button.OnClickListener {

        @Override
        public void onClick(View v) {
            serverThread = new ServerThread(Constants.SERVER_PORT);
            serverThread.start();
        }
    }

    private ServerListener serverListener = new ServerListener();
    private class ServerListener implements Button.OnClickListener {

        @Override
        public void onClick(View v) {
            String addr = address.getText().toString();
            String portNum = clientPort.getText().toString();

            if (addr.isEmpty() || portNum.isEmpty()) {
                Log.e(Constants.TAG, Constants.CLIENT_LOG + "address and port required");
            }

            String h = hour.getText().toString();
            String m = minute.getText().toString();

            if (h.isEmpty() || m.isEmpty()) {
                Log.e(Constants.TAG, Constants.CLIENT_LOG + "hour and minute required");
            }

            String act = null;
            switch (v.getId()) {
                case R.id.set:
                    act = Constants.SET;
                    break;

                case R.id.reset:
                    act = Constants.RESET;

                case R.id.poll:
                    act = Constants.POLL;
            }

            clientThread = new ClientThread(Integer.parseInt(portNum), addr, h, m, act);
            clientThread.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clientPort = (EditText)findViewById(R.id.port);
        hour = (EditText)findViewById(R.id.hour);
        minute = (EditText)findViewById(R.id.minute);
        address = (EditText)findViewById(R.id.address);

        start = (Button)findViewById(R.id.serverButton);
        set = (Button)findViewById(R.id.set);
        reset = (Button)findViewById(R.id.reset);
        poll = (Button)findViewById(R.id.poll);

        start.setOnClickListener(serverListener);
        set.setOnClickListener(clientListener);
        reset.setOnClickListener(clientListener);
        poll.setOnClickListener(clientListener);
    }

    @Override
    protected void onDestroy() {
        if (serverThread != null) {
            serverThread.stopThread();
        }

        super.onDestroy();
    }
}
