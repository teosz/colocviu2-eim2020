package ro.pub.cs.systems.eim.practicaltest02.network;

import android.util.Log;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.general.Utilities;

public class ServerThread extends Thread {

    private boolean isRunning;

    private ServerSocket serverSocket;
    private Integer hour = -1;
    private Integer minute = -1;

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

    private String checkActive() {
        try {
            Socket socket = new Socket("utcnist.colorado.edu", 13);
            BufferedReader bufferedReader = Utilities.getReader(socket);
            String time = bufferedReader.readLine();
            time = bufferedReader.readLine();
            Integer h = Integer.parseInt(time.substring(15,17));
            Integer m = Integer.parseInt(time.substring(18,20));

            if(hour > h) {
                return "active";
            }
            if(hour == h && minute > m) {
                return "active";
            }
            return "inactive";
        } catch (UnknownHostException e) {
            return "none";
        } catch (IOException e) {
            return "none";
        }


    }
    private String handleCommand(String command) {
        String[] args = command.split(",");
        if(args[0].equals("set")) {
            this.hour = Integer.parseInt(args[1]);
            this.minute = Integer.parseInt(args[2]);
            return "";
        }
        if(args[0].equals("reset")) {
            this.hour = -1;
            this.minute = -1;
            return "";
        }
        if(args[0].equals("poll")) {
            Log.v(Constants.TAG, Boolean.toString(this.hour == -1 || this.minute == -1));
            return (this.hour == -1 || this.minute == -1) ? "none" : checkActive();

        }
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
