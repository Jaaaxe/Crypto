package Cryptography.PasswordAttacks;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class Server {

    private ServerSocket SSocket;
    private boolean Halted;
    private int Port;
    private String Password;

    public boolean ServerRunning;

    public Server(int port, String Password) {
        try {
            this.Port = port;
            SSocket = new ServerSocket(port);
            this.Password = Password;
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void StartServer() {
        ServerRunning = true;
        Debug(String.format("Starting server on port %d\n", this.Port));
        CompletableFuture.runAsync(() -> {
            AcceptNewConnections();
        });
    }

    public void StopServer() {

        try {
            this.SSocket.close();
            this.Halted = true;
            this.ServerRunning = false;
        } catch (IOException e) {

        }
    }


    private void AcceptNewConnections() {
        try {
            while (!Halted) {
                var nsocket = SSocket.accept();
//                System.out.println("[SERVER] New Connection...");
                CompletableFuture.runAsync(() -> {
                   HandleClient(nsocket);
                });
            }
        } catch (Exception e) {
            Debug("New Connection Handler Loop Broken\n");
            ServerRunning = false;
            return;
        }
    }

    private void HandleClient(Socket s) {
        try {
            var input_stream = s.getInputStream();
            var output_stream = s.getOutputStream();
            var input_stream_reader = new BufferedReader(new InputStreamReader(input_stream));
            var output_stream_writer = new PrintWriter(output_stream, true);

            WriteStream(output_stream,"Welcome to the secret server\nPlease provide password to access secret files\nPassword : ");
            var response = input_stream_reader.readLine();

//            System.out.println("[SERVER] Incoming Password : " + response);

            if (!response.equals(this.Password)) {
                output_stream_writer.println("Access Denied");
            } else {
                output_stream_writer.println("Access Granted");
            }
            s.close();

        } catch (IOException e) {
            ServerRunning = false;
            e.printStackTrace();
        }
    }

    private String ReadStream(InputStream is) throws IOException {
        var msg_b = new byte[256];
        var l = is.read(msg_b, 0, msg_b.length);
        return new String(msg_b, 0, l, StandardCharsets.UTF_8);
    }

    private void WriteStream(OutputStream os, String msg) throws IOException {
        var msg_b = msg.getBytes(StandardCharsets.UTF_8);
        os.write(msg_b, 0, msg_b.length);
    }


    //region Subscriptions
    private ArrayList<DebugListener> DebugListeners = new ArrayList<>();
    public void SubscribeToDebug(DebugListener d) {
        this.DebugListeners.add(d);
    }
    private void Debug(String msg) {
        System.out.println();
        for (var d : this.DebugListeners) {
            d.OnDebug(this, msg);
        }
    }
    //endregion
}
