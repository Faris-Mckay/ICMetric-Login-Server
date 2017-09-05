package com.peerchat.kent.user;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Faris McKay
 */
public class Saving {
    
    private static final Logger logger = Logger.getLogger(Saving.class.getName());

    /**
     * Method to store an already authenticated user in the the login server user directory
     * passwords have been pre-encrypted and hashed
     * @param username the username of entity
     * @param password the password of entity
     * @param publicKey
     * @param modulus
     */
    public static void saveCredentials(String username, String password, String publicKey, String modulus) {
        BufferedWriter characterfile;
        try {
            characterfile = new BufferedWriter(new FileWriter("./data/credentials/" + username + ".txt"));
            characterfile.write("[CREDENTIALS]", 0, 13);
            characterfile.newLine();
            characterfile.write("username = ", 0, 11);
            characterfile.write(username, 0, username.length());
            characterfile.newLine();
            characterfile.write("password = ", 0, 11);
            characterfile.write(password, 0, password.length());
            characterfile.newLine();
            characterfile.write("publicKey = ", 0, 12);
            characterfile.write(publicKey, 0, publicKey.length());
            characterfile.newLine();
            characterfile.write("mod = ", 0, 6);
            characterfile.write(modulus, 0, modulus.length());
            characterfile.newLine();
            characterfile.write("[EOF]", 0, 5);
            characterfile.newLine();
            characterfile.newLine();
            characterfile.close();
        } catch (IOException e) {
            logger.log(Level.WARNING, username+" was unable to save.");
        }
    }

}
