package com.peerchat.kent.user;

import com.peerchat.kent.Server;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Faris McKay
 */
public class Verification {
    
    /**
     * Separates the prefix from suffix in unprocessed data
     */
    private static final String SEPERATOR = "/4/4/4/";
    
    /**
     * The instance of the login server
     */
    private Server server;
    
    /**
     * Stored public key
     */
    private String publicKey;
    
    /**
     * received modulus
     */
    private String modulus;
    
    /**
     * The encrypted stock message from client
     */
    private String message;
    
    /**
     * Stored username 
     */
    private String username;
    
    /**
     * Stored password 
     */
    private String password;
    
    /**
     * Is this a first time login?
     */
    private boolean first;
    
    /**
     * The character file to check the vMessage in
     */
    private BufferedReader charFile; 
    
    public Verification(String data, String username, String password, Server server, boolean first){
        String[] bundledData = data.split(SEPERATOR, 2);
        if (first){
            publicKey = bundledData[0];
            modulus = bundledData[1];
        } else {
            message = bundledData[1];
        }
        this.username = username;
        this.password = password;
        this.server = server;
        this.first = first;
    }
    
    /**
     * Query the user file to see if the ICMetric messages match, if it is a first time 
     * connection save the message and accept the connection
     */
    public void handle(){
        boolean integrety = false;
        try {
            charFile = new BufferedReader(new FileReader("./data/credentials/" + username + ".txt"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Verification.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(first == true){
            server.getGui().info("LS: Collaborated verification for: "+username);
            Saving.saveCredentials(username, password, publicKey, modulus);
            integrety = true;
        } else {
            try {
                integrety = Loading.checkVerification(message, charFile);
            } catch (IOException ex) {
                Logger.getLogger(Verification.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        sendResponse(integrety);
    }
    
    /**
     * Respond to the Client to either accept the connection or 
     * refuse based on if the ICMetric messages match
     * @param integrety is if they have matched or not
     */
    private void sendResponse(boolean integrety) {
        String message = integrety == true ? "accepted" : "declined";
        server.getGui().info("LS: "+message+" verification for: "+username);
        server.sendMessage(message);
    }
    
}
