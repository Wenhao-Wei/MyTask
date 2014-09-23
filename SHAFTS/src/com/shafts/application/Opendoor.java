/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shafts.application;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;

/**
 *
 * @author Little-Kitty
 * @date 2014-9-15 12:48:53
 */
public class Opendoor {

    /**
     * 解密
     *
     * @param key 解密的密钥
     * @param raw 已经加密的数据
     * @return 解密后的明文
     * @throws Exception
     */
    public byte[] decrypt(Key key, byte[] raw) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
            cipher.init(cipher.DECRYPT_MODE, key);
            int blockSize = cipher.getBlockSize();
            ByteArrayOutputStream bout = new ByteArrayOutputStream(64);
            int j = 0;
            while (raw.length - j * blockSize > 0) {
                bout.write(cipher.doFinal(raw, j * blockSize, blockSize));
                j++;
            }
            return bout.toByteArray();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public static final byte[] toBytes(String s) {    
        byte[] bytes;    
      bytes = new byte[s.length() / 2];    
       for (int i = 0; i < bytes.length; i++) {    
           bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2),    
                   16);    
       }    
        return bytes;    
   }    

    public static PrivateKey getPrivateKey(byte[] keyBytes) throws
            NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    public String getBar() {
        String filePath = path + "shafts.pem";
        File file = new File(filePath);
        BufferedReader bf = null;
        String prikey = null;
        String content = null;
        String bar = null;
        try {
            bf = new BufferedReader(new FileReader(file));
            String temp = null;
            while ((temp = bf.readLine()) != null) {
                if (temp.equals("<@Pipespace@>")) {
                    prikey = bf.readLine();
                } else if (temp.equals("<@Deepcrack@>")) {
                    if (content == null) {
                        content = bf.readLine();
                    } else {
                        content += bf.readLine();
                    }
                }
            }
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
         byte[] key = toBytes(prikey);
        PrivateKey Prikey;
        try {
            Prikey = getPrivateKey(key);
            RSAPrivateKey priKey = (RSAPrivateKey) Prikey;
        bar = new String(decrypt(priKey, toBytes(content)));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Opendoor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(Opendoor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Opendoor.class.getName()).log(Level.SEVERE, null, ex);
        }
     return bar;           
    }
    public static void main(String args[]) throws Exception {
        
    }
    private final String path = System.getProperty("user.dir") + "\\configuration\\"; 
}
