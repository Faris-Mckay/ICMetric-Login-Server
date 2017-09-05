package com.peerchat.kent.user;

import com.peerchat.kent.Constants;
import com.peerchat.kent.Server;
import com.peerchat.kent.security.SHA1Hash;
import com.peerchat.kent.security.AESEncryption;

/**
 *
 * @author Faris McKay
 */
public class Authentication {
    
    /**
     * Separates the username from password in unprocessed data
     */
    private static final String SEPERATOR = "/4/4/4/";
    
    /**
     * Stored username attempt
     */
    private String username;
    
    /**
     * Stored password attempt
     */
    private String password;
    
    /**
     * Stored decrypted username attempt
     */
    private String dUsername;
    
    /**
     * Stored hashed password attempt
     */
    private String hPassword;
    
    /**
     * Stores information whether this is a new connection or not
     */
    private boolean newConnection = false;
    
    /**
     * The instance of the login server
     */
    private Server server;
    
    public Authentication(String data, Server server){
        String[] bundledData = data.split(SEPERATOR, 2);
        username = bundledData[0];
        password = bundledData[1];
        this.server = server;
    }
    
    /**
     * Respond to the clients username password request
     */
    public void handle(){
        int result = 4;
        AESEncryption aes = new AESEncryption(Constants.BASIC_AES_KEY);
        String dUsername = username, dPassword = aes.decrypt(password);
        String hPassword = SHA1Hash.applySHA1Hash(dPassword);
        result = Loading.checkConnection(dUsername, hPassword);
        this.dUsername = dUsername;
        this.hPassword = hPassword;
        switch(result){
            case Loading.MATCHED_DETAILS:
                server.sendMessage("verification");
                server.getGui().info("LS: Accepted authentication for: "+dUsername);
                break;
            case Loading.INCORRECT_DETAILS:
                server.sendMessage("declined");
                server.getGui().info("LS: Declined authentication for: "+dUsername);
                break;
            case Loading.NEW_CONNECTION:
                setNewConnection(true);
                server.sendMessage("synchronization");
                server.getGui().info("LS: New connection saving details for: "+dUsername);
                Saving.saveCredentials(dUsername, hPassword, "unset", "unset");
                break;
            default:
                break;
        }
    }

    /**
     * @return the username
     */
    public String getDUsername() {
        return dUsername;
    }

    /**
     * @return the password
     */
    public String getHPassword() {
        return hPassword;
    }

    /**
     * @return the newConnection
     */
    public boolean isNewConnection() {
        return newConnection;
    }

    /**
     * @param newConnection the newConnection to set
     */
    public void setNewConnection(boolean newConnection) {
        this.newConnection = newConnection;
    }
    
}
