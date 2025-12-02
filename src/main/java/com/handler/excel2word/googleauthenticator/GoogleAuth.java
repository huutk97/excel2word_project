package com.handler.excel2word.googleauthenticator;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


public class GoogleAuth {
    int window_size = 1;

    public void setWindowSize(int s) {
        if ((s >= 1) && (s <= 17)) {
            window_size = s;
        }
    }

    public static Boolean authcode(String codes, String savedSecret, int windowSize) {
        long code = 0L;
        try {
            code = Long.parseLong(codes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long t = System.currentTimeMillis();
        GoogleAuth ga = new GoogleAuth();
        ga.setWindowSize(windowSize);
        boolean r = ga.check_code(savedSecret, code, t);
        return Boolean.valueOf(r);
    }

    public static Boolean authcode(String codes, String savedSecret) {
        return authcode(codes, savedSecret, 1);
    }

    public static String genSecret(String user, String host) {
        String secret = generateSecretKey();
        getQRBarcodeURL(user, host, secret);
        return secret;
    }

    private static String generateSecretKey() {
        SecureRandom sr = null;
        try {
            sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed(Base64.decodeBase64("g8GjEvTbW5oVSV7avLBdwIHqGlUYNzKFI7izOF8GwLDVKs2m0QN7vxRs2im5MDaNCWGmcD2rvcZx"));
            byte[] buffer = sr.generateSeed(10);
            Base32 codec = new Base32();
            byte[] bEncodedKey = codec.encode(buffer);
            return new String(bEncodedKey);
        } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
        }
        return null;
    }


    public static String getQRBarcodeURL(String user, String host, String secret) {
        String format = "/api/google/qrcode?content=otpauth://totp/%s%s%%3Fsecret%%3D%s";
        return String.format(format, new Object[]{user, host, secret});
    }

    private boolean check_code(String secret, long code, long timeMsec) {
        Base32 codec = new Base32();
        byte[] decodedKey = codec.decode(secret);
        long t = timeMsec / 1000L / 30L;
        for (int i = -window_size; i <= window_size; i++) {
            long hash = 0;
            try {
                hash = verify_code(decodedKey, t + i);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
            if (hash == code) {
                return true;
            }
        }
        return false;
    }

    private static int verify_code(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] data = new byte[8];
        long value = t;
        for (int i = 8; i-- > 0; value >>>= 8) {
            data[i] = ((byte) (int) value);
        }
        SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signKey);
        byte[] hash = mac.doFinal(data);
        int offset = hash[19] & 0xF;
        long truncatedHash = 0L;
        for (int i = 0; i < 4; i++) {
            truncatedHash <<= 8;
            truncatedHash |= hash[(offset + i)] & 0xFF;
        }
        truncatedHash &= 0x7FFFFFFF;
        truncatedHash %= 1000000L;
        return (int) truncatedHash;
    }
}
