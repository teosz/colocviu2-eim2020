package ro.pub.cs.systems.eim.practicaltest02.network;

import android.util.Log;
import android.widget.EditText;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import ro.pub.cs.systems.eim.practicaltest02.general.Constants;

public class ServerThread extends Thread {

    private boolean isRunning;

    private ServerSocket serverSocket;
    private Integer hour;
    private Integer minute;

    public ServerThread() {
    }

    public void startServer() {
        isRunning = true;
        start();
        Log.v(Constants.TAG, "startServer() method was invoked");
    }

    public void stopServer() {
        isRunning = false;
        try {
            serverSocket.close();
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
        Log.v(Constants.TAG, "stopServer() method was invoked");
    }
    private String handleCommand(String command) {
        Log.v(Constants.TAG, command);
        return "";
    }
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(Constants.SERVER_PORT, 50, InetAddress.getByName("0.0.0.0"));
            while (isRunning) {
                Socket socket = serverSocket.accept();
                Log.v(Constants.TAG, "accept()-ed: " + socket.getInetAddress());
                if (socket != null) {

                    CommunicationThread communicationThread = new CommunicationThread(socket,
                            (command) -> handleCommand(command)
                    );
                    communicationThread.start();
                }
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }
}
