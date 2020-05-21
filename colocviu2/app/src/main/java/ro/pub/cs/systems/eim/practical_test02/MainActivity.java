package ro.pub.cs.systems.eim.practical_test02;

import android.os.Bundle;
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
