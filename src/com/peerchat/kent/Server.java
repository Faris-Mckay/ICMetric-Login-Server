package com.peerchat.kent;

import com.peerchat.kent.user.Authentication;
import com.peerchat.kent.user.Verification;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;

/**
 *
 * @author Faris McKay
 */
public class Server extends Thread {

    private boolean active = false;

    private PrintWriter bufferSender;

    private ServerGUI gui;

    private ServerSocket serverSocket;

    private Socket client;
    
    boolean recycle = true;

    public Server(ServerGUI recieved) {
        this.gui = recieved;
    }

    public static void main(String[] args) {
        Screen frame = new Screen();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Close the server
     */
    public void close(boolean announce) {
        active = false;
        recycle = false;
        if (bufferSender != null) {
            bufferSender.flush();
            bufferSender.close();
            bufferSender = null;
        }
        try {
            client.close();
            serverSocket.close();
        } catch (Exception e) {
        }
        serverSocket = null;
        client = null;
        if(announce)
            getGui().info("LS Offline.");

    }

    /**
     * Method to send the messages from server to client
     *
     * @param message the message sent by the server
     */
    public void sendMessage(String message) {
        if (bufferSender != null && !bufferSender.checkError()) {
            bufferSender.println(message);
            bufferSender.flush();
        }
    }

    /**
     * Builds a new server connection
     */
    private void runServer() {
        active = true;
        recycle = true;
        Authentication attempt = null;
        try {
            getGui().info("LS: Awaiting Connection...");
            serverSocket = new ServerSocket(Constants.SERVER_PORT);
            client = serverSocket.accept();
            getGui().info("LS: Receiving Data...");
            try {
                bufferSender = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                while (active && recycle) {
                    String message = null;
                    try {
                        message = in.readLine();
                    } catch (IOException e) {
                        getGui().info("Error reading data: " + e.getMessage());
                    }
                    if (message != null && getGui() != null) {
                        attempt = new Authentication(message, this);
                        attempt.handle();
                        active = false;
                        recycle = true;
                    }
                }
                awaitICMetricString(attempt.getDUsername(), attempt.getHPassword(), attempt.isNewConnection());
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }
    }
    
    /**
     * Handles the acceptance of the ICMetric message which has been encrypted by the client
     * and compares this with previous login attempts to decipher if the user is who they say
     * they are, if its a first time connection the message is stored for future reference
     * @param username of the client
     * @param password of the client
     * @param newConnection if they have connected to the LS before
     */
    public void awaitICMetricString(String username, String password, boolean newConnection){
        active = true;
        recycle = true;
        try {
            getGui().info("LS: Awaiting ICMetric Information...New Connect?"+newConnection);
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                while (active && recycle) {
                    String message = null;
                    try {
                        message = in.readLine();
                    } catch (IOException e) {
                        getGui().info("Error reading data: " + e.getMessage());
                    }
                    if (message != null && getGui() != null) {
                        Verification verify = new Verification(message, username, password, this, newConnection);
                        verify.handle();
                        active = false;
                        recycle = true;
                    }
                }
                if(recycle){
                    close(false);
                    runServer();
                }
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void run() {
        super.run();
        runServer();
    }

    /**
     * @return the gui
     */
    public ServerGUI getGui() {
        return gui;
    }

    public interface ServerGUI {

        /**
         * Update the login server display with new information
         * @param message to update
         */
        public void info(String message);
    }
}
