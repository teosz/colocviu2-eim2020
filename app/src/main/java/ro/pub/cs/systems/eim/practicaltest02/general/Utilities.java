package ro.pub.cs.systems.eim.practicaltest02.general;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;

public class Utilities {

    public static BufferedReader getReader(Socket socket) throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public static PrintWriter getWriter(Socket socket) throws IOException {
        return new PrintWriter(socket.getOutputStream(), true);
    }
    public static BufferedReader getReader(HttpURLConnection httpURLConnection) throws IOException {
        return new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
    }

    public static PrintWriter getWriter(HttpURLConnection httpURLConnection) throws IOException {
        return new PrintWriter(new BufferedOutputStream(httpURLConnection.getOutputStream()));
    }

}
