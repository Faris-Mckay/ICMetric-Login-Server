package com.peerchat.kent.security;

import com.peerchat.kent.Constants;
import java.math.BigInteger;

/**
 *
 * @author faris
 */
public class ICMetricSecure {
    
    String publicKey;
    
    String modulus;
    
    String message;
    
    public ICMetricSecure(String publicKey, String modulus, String message){
        this.publicKey = publicKey;
        this.modulus = modulus;
        this.message = message;
    }
    
    public boolean checkICMetricDetails(){
        RSAEncryption rsa = new RSAEncryption(modulus, publicKey);
        return rsa.decrypt(message).equalsIgnoreCase(Constants.STOCK_ENCRYPTED_MESSAGE);
    }
    
}
