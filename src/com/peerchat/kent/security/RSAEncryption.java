package com.peerchat.kent.security;

import java.math.BigInteger;
 
/**
 * 
 * @author Faris McKay
 */
 
public class RSAEncryption {
 
    private String mod, pubKey;
 
    public RSAEncryption(String mod, String pubKey) {
        this.mod = mod;
        this.pubKey = pubKey;
    }
 
    /**
     * Decrypt the given ciphertext message.
     * */
    public synchronized String decrypt(String message) {
        return new String((new BigInteger(message)).modPow(getPubKey(), getMod()).toByteArray());
    }
 
    /**
     * Return the modulus.
     * */
    public synchronized BigInteger getMod() {
        return new BigInteger(mod);
    }
 
 
    /**
     * Return the public key.
     * */
    public synchronized BigInteger getPubKey() {
        return new BigInteger(pubKey);
    }
   
}
