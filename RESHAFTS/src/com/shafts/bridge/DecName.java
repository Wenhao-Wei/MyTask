package com.shafts.bridge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.*;
/**
 * username
 * @author Little-Kitty
 */
public class DecName {

    private static final String DES_ALGORITHM = "DES";
    private static final String KEY = "2014ChinaECUST075522628888FComputerScience075561869839";
    private String path = System.getProperty("user.dir");

    /**
     * DES Encrypt
     *
     * @param plainData
     * @return
     * @throws Exception
     */
    public static String encryption(String plainData) throws Exception {

        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(DES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, generateKey(KEY));

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
        }

        try {
            // 为了防止解密时报javax.crypto.IllegalBlockSizeException: Input length must be multiple of 8 when decrypting with padded cipher异常，
            // 不能把加密后的字节数组直接转换成字符串
            byte[] buf = cipher.doFinal(plainData.getBytes());

            return Base64Utils.encode(buf);

        } catch (IllegalBlockSizeException e) {
            throw new Exception("IllegalBlockSizeException", e);
        } catch (BadPaddingException e) {
            throw new Exception("BadPaddingException", e);
        }

    }

    /**
     * DES Decrypt
     *
     * @param secretData
     * @return
     * @throws Exception
     */
    public static String decryption(String secretData) throws Exception {

        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(DES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, generateKey(KEY));

        } catch (NoSuchAlgorithmException e) {
            throw new Exception("NoSuchAlgorithmException", e);
        } catch (NoSuchPaddingException e) {
            throw new Exception("NoSuchPaddingException", e);
        } catch (InvalidKeyException e) {
            throw new Exception("InvalidKeyException", e);

        }

        try {

            byte[] buf = cipher.doFinal(Base64Utils.decode(secretData.toCharArray()));

            return new String(buf);

        } catch (IllegalBlockSizeException e) {
            throw new Exception("IllegalBlockSizeException", e);
        } catch (BadPaddingException e) {
            throw new Exception("BadPaddingException", e);
        }
    }

    /**
     * Get decrypt key
     *
     * @param secretKey
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static SecretKey generateKey(String secretKey) throws NoSuchAlgorithmException {
        //SecureRandom secureRandom = new SecureRandom(secretKey.getBytes());
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(secretKey.getBytes());
        // 为我们选择的DES算法生成一个KeyGenerator对象
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance(DES_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
        }
        kg.init(secureRandom);
		//kg.init(56, secureRandom);

        // 生成密钥
        return kg.generateKey();
    }

    static class Base64Utils {

        static private char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();
        static private byte[] codes = new byte[256];

        static {
            for (int i = 0; i < 256; i++) {
                codes[i] = -1;
            }
            for (int i = 'A'; i <= 'Z'; i++) {
                codes[i] = (byte) (i - 'A');
            }
            for (int i = 'a'; i <= 'z'; i++) {
                codes[i] = (byte) (26 + i - 'a');
            }
            for (int i = '0'; i <= '9'; i++) {
                codes[i] = (byte) (52 + i - '0');
            }
            codes['+'] = 62;
            codes['/'] = 63;
        }

        /**
         * 将原始数据编码为base64编码
         */
        static public String encode(byte[] data) {
            char[] out = new char[((data.length + 2) / 3) * 4];
            for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
                boolean quad = false;
                boolean trip = false;
                int val = (0xFF & (int) data[i]);
                val <<= 8;
                if ((i + 1) < data.length) {
                    val |= (0xFF & (int) data[i + 1]);
                    trip = true;
                }
                val <<= 8;
                if ((i + 2) < data.length) {
                    val |= (0xFF & (int) data[i + 2]);
                    quad = true;
                }
                out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];
                val >>= 6;
                out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];
                val >>= 6;
                out[index + 1] = alphabet[val & 0x3F];
                val >>= 6;
                out[index + 0] = alphabet[val & 0x3F];
            }

            return new String(out);
        }

        /**
         * 将base64编码的数据解码成原始数据
         */
        static public byte[] decode(char[] data) {
            int len = ((data.length + 3) / 4) * 3;
            if (data.length > 0 && data[data.length - 1] == '=') {
                --len;
            }
            if (data.length > 1 && data[data.length - 2] == '=') {
                --len;
            }
            byte[] out = new byte[len];
            int shift = 0;
            int accum = 0;
            int index = 0;
            for (int ix = 0; ix < data.length; ix++) {
                int value = codes[data[ix] & 0xFF];
                if (value >= 0) {
                    accum <<= 6;
                    shift += 6;
                    accum |= value;
                    if (shift >= 8) {
                        shift -= 8;
                        out[index++] = (byte) ((accum >> shift) & 0xff);
                    }
                }
            }
            if (index != out.length) {
                throw new Error("miscalculated data length!");
            }
            return out;
        }
    }

    /**
     * write file to local
     *
     * @param str
     * @throws Exception
     */
    public void accountRecord(String str) throws Exception {
        String result = encryption(str);
        byte b[] = new byte[1024];
        String filePath1 = path + "\\configuration\\";
        File file1 = new File(filePath1);
        if (!file1.exists()) {
            file1.mkdir();
        }
        String filePath = filePath1 + ".namespaces.5";
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        b = result.getBytes();
        FileOutputStream out = new FileOutputStream(file);
        out.write(b);
    }

    public String getAccount() {
        String filePath = path + "\\configuration\\.namespaces.5";
        String s = null;
        String account = null;
        File file = new File(filePath);
        if (file.exists()) {
            InputStreamReader in;
            try {
                in = new InputStreamReader(new FileInputStream(file));
                BufferedReader bufferedReader = new BufferedReader(in);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    s = lineTxt;
                }
                bufferedReader.close();
                account = decryption(s);
            }catch (Exception ex) {
                Logger.getLogger(DecName.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return account;
    }

    public static void main(String[] a) throws Exception {
        String s = "sajkhda_asdjkakw";
        //new SetorGet().writefile(s);
        String s1 = new DecName().getAccount();
        System.out.println(s1);
    }
}
