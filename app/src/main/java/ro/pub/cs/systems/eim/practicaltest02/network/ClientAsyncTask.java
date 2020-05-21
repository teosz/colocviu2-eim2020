package ro.pub.cs.systems.eim.practicaltest02.network;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.general.Utilities;

public class ClientAsyncTask extends AsyncTask<String, String, Void> {

    private TextView serverMessageTextView;
    private String hour;
    private String minute;
    private String command;
    public ClientAsyncTask(String command, String hour, String minute, TextView serverMessageTextView) {
        this.serverMessageTextView = serverMessageTextView;
        this.hour = hour;
        this.minute = minute;
        this.command = command;

    }
    public ClientAsyncTask(String command, TextView serverMessageTextView) {
        this.serverMessageTextView = serverMessageTextView;
        this.hour = hour;
        this.minute = minute;
        this.command = command;

    }


    @Override
    protected Void doInBackground(String... params) {
        Socket socket = null;
        try {
            String serverAddress = params[0];
            int serverPort = Integer.parseInt(params[1]);
            socket = new Socket(serverAddress, serverPort);
            if (socket == null) {
                return null;
            }
            Log.v(Constants.TAG, "Connection opened with " + socket.getInetAddress() + ":" + socket.getLocalPort());

            PrintWriter printWriter = Utilities.getWriter(socket);
            if(this.command == "set") {
                printWriter.println(this.command + ','+ this.hour+','+this.minute);
            } else {
                printWriter.println(this.command);
                BufferedReader bufferedReader = Utilities.getReader(socket);
                String currentLine;
                while ((currentLine = bufferedReader.readLine()) != null) {
                    publishProgress(currentLine);
                }
            }

        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
                Log.v(Constants.TAG, "Connection closed");
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        serverMessageTextView.setText("");
    }

    @Override
    protected void onProgressUpdate(String... progress) {
        serverMessageTextView.append(progress[0] + "\n");
    }

    @Override
    protected void onPostExecute(Void result) {}

}
