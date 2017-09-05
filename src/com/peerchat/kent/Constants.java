package com.peerchat.kent;

/**
 *
 * @author Faris McKay
 */
public class Constants {
    
    /**
     * The port to accept connections to the login server
     */
    public static final int SERVER_PORT = 4444;
    
    /**
     * Key used for AES symmetric encryptions
     */
    public static final String BASIC_AES_KEY = "bar12345bar12345";
    
    /**
     * The wrapper message which is used to identify genuine connections 
     */
    public static final String STOCK_ENCRYPTED_MESSAGE = "DOES THIS MESSAGE MAKE SENSE TO YOU?";
    
}
