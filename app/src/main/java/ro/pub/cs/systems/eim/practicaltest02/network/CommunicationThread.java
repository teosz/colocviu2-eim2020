package ro.pub.cs.systems.eim.practicaltest02.network;

import android.util.Log;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Function;

import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.general.Utilities;

public class CommunicationThread extends Thread {

    private Socket socket;
    private Function<String, String> handleData;

    public CommunicationThread(Socket socket, Function<String, String> handleData) {
        this.socket = socket;
        this.handleData = handleData;
    }

    @Override
    public void run() {
        try {
            Log.v(Constants.TAG, "Connection opened to " + socket.getLocalAddress() + ":" + socket.getLocalPort()+ " from " + socket.getInetAddress());


            BufferedReader bufferedReader = Utilities.getReader(socket);
            String currentLine = bufferedReader.readLine();
            Log.v(Constants.TAG, currentLine);

            String response = this.handleData.apply(currentLine);
            if(response.length() != 0) {
                PrintWriter printWriter = Utilities.getWriter(socket);
                printWriter.println(this.handleData.apply(currentLine));
            }
            socket.close();
            Log.v(Constants.TAG, "Connection closed");
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }

}
