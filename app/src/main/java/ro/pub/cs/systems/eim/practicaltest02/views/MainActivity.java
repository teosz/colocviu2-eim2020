package ro.pub.cs.systems.eim.practicaltest02.views;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ro.pub.cs.systems.eim.practicaltest02.R;
import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.network.ClientAsyncTask;
import ro.pub.cs.systems.eim.practicaltest02.network.ServerThread;

public class MainActivity extends AppCompatActivity {

    private EditText input;
    private TextView serverMessageTextView;
    private ServerThread serverThread;
    private Boolean serverStarted = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(serverStarted == false) {
            serverThread = new ServerThread();
            serverThread.startServer();
            Log.v(Constants.TAG, "Starting server...");
        }
    }
    @Override
    protected  void onStop() {
        if(serverStarted == true) {
            serverThread.stopServer();
            Log.v(Constants.TAG, "Stopping server...");
            serverStarted = false;

        }
        super.onStop();
    }
    public void handleAddClick(View view) {
        Log.v(Constants.TAG, "Add clicked...");
        serverMessageTextView = (TextView) findViewById(R.id.textView2);
        String hour = ((EditText) findViewById(R.id.option_text)).getText().toString();
        String minute = ((EditText) findViewById(R.id.option_text2)).getText().toString();

        ClientAsyncTask clientAsyncTask = new ClientAsyncTask("set", hour, minute, serverMessageTextView);
        clientAsyncTask.execute(Constants.SERVER_HOST, String.valueOf(Constants.SERVER_PORT));
    }
    public void handleStatus(View view) {
        Log.v(Constants.TAG, "Status clicked...");
        serverMessageTextView = (TextView) findViewById(R.id.textView2);
        ClientAsyncTask clientAsyncTask = new ClientAsyncTask("poll", serverMessageTextView);
        clientAsyncTask.execute(Constants.SERVER_HOST, String.valueOf(Constants.SERVER_PORT));
    }
}
