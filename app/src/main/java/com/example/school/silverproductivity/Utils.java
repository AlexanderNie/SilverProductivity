package com.example.school.silverproductivity;

import java.io.InputStream;
import java.io.OutputStream;
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

/**
 * Created by WorkStation on 18/8/2015.
 */
public class Utils {

    private static String	digits = "0123456789abcdef";

    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }

    public static String toHex(byte[] data, int length)
    {
        StringBuffer	buf = new StringBuffer();

        for (int i = 0; i != length; i++)
        {
            int	v = data[i] & 0xff;

            buf.append(digits.charAt(v >> 4));
            buf.append(digits.charAt(v & 0xf));
        }

        return buf.toString();
    }

    public static String toHex(byte[] data)
    {
        return toHex(data, data.length);
    }

    public static byte[] toByteArray(
            String string)
    {
        byte[]	bytes = new byte[string.length()];
        char[]  chars = string.toCharArray();

        for (int i = 0; i != chars.length; i++)
        {
            bytes[i] = (byte)chars[i];
        }

        return bytes;
    }

    public static String encryptPassword(String password){
        KeySpec keySpec = new PBEKeySpec(Configure.password.toCharArray(), Configure.salt, Configure.iterations);

        SecretKey key = null;
        try {
            key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(Configure.salt, Configure.iterations);


        Cipher encoder = null;
        try {
            encoder = Cipher.getInstance(key.getAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            encoder.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        byte[] enc = new byte[0];
        try {
            enc = encoder.doFinal(password.getBytes());
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }


        return Utils.toHex(enc);
    }

    public static String decryptPassword(String password) {

        KeySpec keySpec = new PBEKeySpec(Configure.password.toCharArray(), Configure.salt, Configure.iterations);

        SecretKey key = null;
        try {
            key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(Configure.salt, Configure.iterations);

        Cipher encoder = null;
        try {
            encoder = Cipher.getInstance(key.getAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            encoder.init(Cipher.DECRYPT_MODE, key, paramSpec);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        byte[] dec = new byte[0];
        try {
            dec = encoder.doFinal(Utils.toByteArray(convertHexToString(password)));
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        System.out.println("decrypted = " + convertHexToString(Utils.toHex(dec)));
        return convertHexToString(Utils.toHex(dec));
    }


    public static String convertHexToString(String hex){

        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for( int i=0; i<hex.length()-1; i+=2 ){

            //grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            //convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            //convert the decimal to character
            sb.append((char)decimal);

            temp.append(decimal);
        }
        System.out.println("Decimal : " + temp.toString());

        return sb.toString();
    }

}
