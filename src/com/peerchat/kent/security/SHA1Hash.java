/**
 * TODO: SHA1 to 256
 */
package com.peerchat.kent.security;

import java.security.MessageDigest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * A class which contains all relevant knowledge to hash the user's input using SHA1, this is a one way
 * system and subsequent entries of passwords should also be hashed and then checked for likeness to the original
 *
 * @author Faris McKay
 * @version 09/02/2017
 */
public class SHA1Hash {
    
     /**
     * Contains the valid characters in Hexadecimal format
     */
    final private static char[] hexArray = "0123456789ABCDEF".toCharArray();
 
    /**
     * SHA1 Hash the inputted string by breaking it down into its subsequent binary format and complete this
     * hash function with the MessageDigest and then by using this to also pad the value. The binary figure is then
     * hexed and returned
     * @param toHash value to convert
     * @return the new hashed string
     */
    public static String applySHA1Hash(String toHash) {
        String hash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = toHash.getBytes("UTF-8");
            digest.update(bytes, 0, bytes.length);
            bytes = digest.digest();
            hash = bytesToHex(bytes);
        } catch( NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch( UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return hash;
    }
 
    /**
     * Converts from binary to hexadecimal
     * @param bytes inputted blocks of 8 bits
     * @return hexed value in String format
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for(int j = 0; j < bytes.length; j++) {
            int v = bytes[ j ] & 0xFF;
            hexChars[ j * 2 ] = hexArray[v >>> 4];
            hexChars[ j * 2 + 1 ] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    
}
