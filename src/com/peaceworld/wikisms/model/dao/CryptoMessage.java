package com.peaceworld.wikisms.model.dao;
 
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
 
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import com.peaceworld.wikisms.controller.utility.Settings;

import android.content.Context;
import android.util.Base64;
 
/**
 *  
 * @author Giulio
 */
public class CryptoMessage {
 
    private Key key;
    private AlgorithmParameterSpec paramSpec;
    private Context context;
    private String memo,m1, nameOfALG;
 
    /**
     * Generates the encryption key. using "des" algorithm
     * 
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException 
     * @throws InvalidKeySpecException 
     */
    private void generateKey() throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
    
    	//String passPhrase = "PBEWithMD5AndDES";
    	m1=context.getSharedPreferences(Settings.APPLICATION_SETTING_PREFERENCES, Context.MODE_PRIVATE).getString("memo1", "");
    	nameOfALG=context.getSharedPreferences(Settings.APPLICATION_SETTING_PREFERENCES, Context.MODE_PRIVATE).getString("nameOfALG", "");
    	int iterationCount = 19;
        
        KeySpec keySpec = new PBEKeySpec((m1+memo).toCharArray(), Settings.getSalt(), iterationCount);
        SecretKey key = SecretKeyFactory.getInstance(nameOfALG).generateSecret(keySpec);
        this.key=key;
        paramSpec = new PBEParameterSpec(Settings.getSalt(), iterationCount);

    }
 
    public String encrypt(String message) throws IllegalBlockSizeException,
	    BadPaddingException, NoSuchAlgorithmException,
	    NoSuchPaddingException, InvalidKeyException,
	    UnsupportedEncodingException, InvalidAlgorithmParameterException {
    	
	Cipher cipher = Cipher.getInstance(nameOfALG);
	cipher.init(Cipher.ENCRYPT_MODE, key,paramSpec);
 
	// Gets the raw bytes to encrypt, UTF8 is needed for
	// having a standard character set
	byte[] stringBytes = message.getBytes("UTF8");
 
	// encrypt using the cypher
	byte[] raw = cipher.doFinal(stringBytes);
 
	// converts to base64 for easier display.
	String base64 = Base64.encodeToString(raw, Base64.DEFAULT); 
 
	return base64;
    }
 
      public String decrypt(String encrypted) throws InvalidKeyException,
	    NoSuchAlgorithmException, NoSuchPaddingException,
	    IllegalBlockSizeException, BadPaddingException, IOException, InvalidAlgorithmParameterException {
 
		Cipher cipher = Cipher.getInstance(nameOfALG);
		cipher.init(Cipher.DECRYPT_MODE, key,paramSpec);
 
		//decode the BASE64 coded message
		byte[] raw =  Base64.decode(encrypted, Base64.DEFAULT);
 
		//decode the message
		byte[] stringBytes = cipher.doFinal(raw);
 
		//converts the decoded message to a String
		String clear = new String(stringBytes, "UTF8");
		return clear;
    }
 
    public CryptoMessage(Context context, String memo) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
    	this.context=context;
    	this.memo=memo;
    	generateKey();
    	
    }
 
}