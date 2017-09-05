package com.peerchat.kent.user;

import com.peerchat.kent.security.ICMetricSecure;
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
public class Loading {
    
    //Return codes for clarity
    public static final int MATCHED_DETAILS = 1;
    public static final int INCORRECT_DETAILS = 2;
    public static final int NEW_CONNECTION = 3;
    
    
    /**
     * Check the submitted client information against the user database and return int for action to 
     * execute next
     * @param username typed in by client
     * @param password typed in by client
     * @return 1 if Exists and Correct Details Match
     *          2 if Exists and Incorrect Details
     *            3 if User not exists register account
     */
    public static int checkConnection(String username, String password) {
        BufferedReader characterfile = null;
        try {
            characterfile = new BufferedReader(new FileReader("./data/credentials/" + username + ".txt"));
        } catch (FileNotFoundException ex) {
            return NEW_CONNECTION;
        }

        try {
            if (checkCredentials(password, characterfile)) {
                return MATCHED_DETAILS;
            }
        } catch (IOException ex) {
            Logger.getLogger(Loading.class.getName()).log(Level.SEVERE, null, ex);
        }
        return INCORRECT_DETAILS;
    }
    
    /**
     * Check the submitted password with the password in the found character file 
     * @param password attempted to log in with
     * @param characterfile the file found matching username
     * @return true/false depending if password match
     * @throws IOException if can not be read
     */
    private static boolean checkCredentials(String password,
            BufferedReader characterfile) throws IOException {
        String line = "";
        String token = "";
        String token2 = "";
        boolean EndOfFile = false;
        try {
            line = characterfile.readLine();
            line = characterfile.readLine();
        } catch (IOException ioexception) {
            return false;
        }
        while (EndOfFile == false && line != null) {
            line = line.trim();
            int spot = line.indexOf("=");
            if (spot > -1) {
                token = line.substring(0, spot).trim();
                token2 = line.substring(spot + 1).trim();
                if (token.equals("password")) {
                    if (password.equalsIgnoreCase(token2)) {
                        EndOfFile = true;
                        characterfile.close();
                        return true;
                    }
                }
            }
            if (line.equals("[EOF]")) {
                EndOfFile = true;
                characterfile.close();
                return false;
            }
            line = characterfile.readLine();
        }
        return false;
    }
    
    /**
     * Check the stored verification message with the message in the found character file 
     * @param password attempted to log in with
     * @param characterfile the file found matching username
     * @return true/false depending if password match
     * @throws IOException if can not be read
     */
    public static boolean checkVerification(String message, BufferedReader characterfile) throws IOException {
        String line = "";
        String token = "";
        String token2 = "";
        String modulus = null;
        String publicKey = null;
        boolean EndOfFile = false;
        try {
            line = characterfile.readLine();
            line = characterfile.readLine();
        } catch (IOException ioexception) {
            return false;
        }
        boolean canCalc = false;
        while (EndOfFile == false && line != null) {
            line = line.trim();
            int spot = line.indexOf("=");
            if (spot > -1) {
                token = line.substring(0, spot).trim();
                token2 = line.substring(spot + 1).trim();
                if (token.equals("publicKey")) {
                    publicKey = token2;
                }
                if (token.equals("mod")) {
                    modulus = token2;
                    canCalc = true;
                }
                if(canCalc){
                    EndOfFile = true;
                    characterfile.close();
                    
                    boolean value = new ICMetricSecure(publicKey, modulus, message).checkICMetricDetails();
                    return value;
                }
            }
            if (line.equals("[EOF]")) {
                EndOfFile = true;
                characterfile.close();
                return false;
            }
            line = characterfile.readLine();
        }
        return false;
    }
}

