package org.firewolf8385.playerpasswords.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.firewolf8385.playerpasswords.SettingsManager;

public class StringUtils
{
    static SettingsManager settings = SettingsManager.getInstance();
  
    /**
     * Method to hash a String. Used for password protection.
     * @param s The string to hash.
     * @return The hashed string.
     */
    public static String hash(String s)
    {
        if(settings.getConfig().getString("EncryptMethod").toLowerCase().equals("aes"))
        {
            return AESencrypt(s);
        }
        else if(settings.getConfig().getString("EncryptMethod").toLowerCase().equals("sha256"))
        {
            return getSHA256Encrypt(s);
        }
        else
        {
            return String.valueOf(s.hashCode());
        }
    }
    
    private static String getSHA256Encrypt(String clearTextPassword)
    {  
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(clearTextPassword.getBytes());
            return new sun.misc.BASE64Encoder().encode(md.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static String AESencrypt(String clearTextPassword) 
    {
        SecretKeySpec secretKey;
        byte[] key;
        String myKey = "dRgUkXp2s5u8x/A?";

        try 
        {
            MessageDigest sha = null;
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); 
            secretKey = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(clearTextPassword.getBytes("UTF-8")));
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        } 
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return null;
    }
} 