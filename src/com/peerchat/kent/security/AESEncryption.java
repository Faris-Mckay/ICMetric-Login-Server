package com.peerchat.kent.security;

import com.peerchat.kent.Constants;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * @author Faris McKay
 */
public class AESEncryption {
    
    private String key;
    
    public AESEncryption(String key){
        this.key = key;
    }
    
    public AESEncryption(){
        key = null;
    }
    
    public String decrypt(String encrypted) {
        try {
            key = key == null ? Constants.BASIC_AES_KEY : key;
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            byte[] decordedValue = Base64.decode(encrypted, Base64.DEFAULT);
            byte[] decrypted = cipher.doFinal(decordedValue);
            return new String(decrypted);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}