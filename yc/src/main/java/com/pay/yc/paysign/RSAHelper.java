package com.pay.yc.paysign;


import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


/**
 * 采用RSA/ECB/PKCS1Padding 密钥算法
 * 
 * 使用2048位密钥，公钥作为加密, 私钥进行解密获取原始数据
 * 
 * @author loven_11
 * 
 */
public class RSAHelper {
    // 我方私钥
    // private static String PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC8dxdGlOhYCbYUi8MVHyiQQNGvphwmJyRkHnx1QZYU4EuRCYmijYL9M9+EbQsIhG+O6Iy7lEukXKzUdJKr2KGe+PNGInxRz7cMR5X+/QJxQNmxYT1vDxjz8YcKZ/30QHfv2SS8efD+/8brjsQROz45RhZwXudts5Uzs8HMlB7sFas5+XTioHYcUEWdzQxUXxErFSAt4wktUfrEM5FUGA+M6ALDRtJjAbnlNLcj80UNP5lMj4C2teBvE/ccedG60q5vNJYQXR9rDYc4Ocz7uszAAY0pA/Zk/spceV/uvU5NN4Vt/liVIxBYO4xWl0DBqWsYoTbOFimPbFvO9aqzNuT5AgMBAAECggEAcTguIHXWewUigIdR4/968+Ys0hO9d2IuZ4M1C5AiJFp2qyQKrj/LQGeIUNrdNPmytyyXVx0IsC4Imf2KG3ih54nxQ+H1w4RAmDztLGk8rWLjJL+hE3YDbJFdqe+9TZ3pL4rImf27vMVV8C8tNJjdkbS8SDeLWoj3Cj9kHo2hYfu+KbaUYt3YTE68h8PfZGi+PF6n5CjWfY4kN3abIQBM4T2uogNNWKlsKFbD63H9G61MxJnov9wTFolF/LccfzaPmAPRJFHNBOlo1wInlOz3eGhWGn7QVD3yPO4vUP8ferVnNEDyrrw5Xz2PthTaIMoWgtudZEXobIlRatwDZRLKgQKBgQDo96hGVGCd/BRyAx2kIY3pO/OpSXjAOxqvnh7z8FA9JJXq2BU7FA7dp/gtUm072hp6FwC40dNRIVmiZDkqg7cnAay4t/P87/rCSU60GOLDUUKVWOzMBOM0aEl9WDX9idM/bOyFkihfbDtE85bYs+CUPV74d5JMkZTPR65nDszWiQKBgQDPGRgzty32RPOgbaQ43ec9bF1eMQAbAwAs7CCMkINi8u6TzTFZHwJUkJowRKPVByXs4D5sfKsJlLeQZ2jSm/GrNmU+4nrTOgqi9nsB88CvSHLwvvokALsigdrzfh9utrTh5CwyosDLXQ5c7//JkgTzWy7kJWAq+gU1QQ0h6h3+8QKBgChFj4hA08bbqakf0sLprf9whqlv8vi//tf27IDcvPIk3emP2vOwfvVgMO2cvxdGl6cdTNmmI/J1YmpJCvzHciTDDFmOKxZBYcauJ3XIi4KaDHmGusB149TOZk3Nrt+aMLNZH/XjEymTqxc7SItDZQIbLcyj1t13mRLP4F0eWNiJAoGAKA89nDtRu7Jowd6ThqB962a7xM2LrK3u6cGfJjGKCKjfN13YrtmNtNfny3BrlHh2gJR73rZay1amD1vBbgPWQn0aJojeo1810YIGF0GUQxOrkCAQ+MffavRdav45jEvxP0TIu965llzgu199Rftl08EOl/kW2hlUypFTvfywYrECgYBFlf2CD2mkufym9/yHGCSulNAZ3o2qpSLuS9s5jeBVCrF1OpaDkoGMNqTMy0Ufgx4m2oNXD4iJbF5kzLd7sbNVfUhiRfvMq/ufKJQs3jdlM9oFp04w/GZIsT9oagaoGuAM+Htoyn5T5ik+TPVAmMn07bmIMVcoh36aiCv5rs2UjQ==";

    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";
    private static final String ENCRYPT_ALGORITHMS = "RSA/ECB/PKCS1Padding";
    
    private static int blocksize = 256;

    /**
     * 
     * @return
     * @throws Exception
     */
    public static byte[] sign(PrivateKey privateKey, byte[] contentBytes) throws Exception {
        java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
        signature.initSign(privateKey);
        signature.update(contentBytes);
        return signature.sign();
    }

    /**
     * 
     * @param contentBytes
     * @param publicKey
     * @param signBytes
     * @return
     */
    public static boolean verify(byte[] contentBytes, PublicKey publicKey, byte[] signBytes) throws Exception{
        java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
        signature.initVerify(publicKey);
        signature.update(contentBytes);
        boolean bverify = signature.verify(signBytes);
        return bverify;
    }
    
    /**
     * 
     * @param publicKey
     * @param content
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(PublicKey publicKey, byte[] contentBytes) throws Exception {
        int nblocksize = blocksize - 11;
        int n = contentBytes.length / nblocksize + (contentBytes.length % nblocksize == 0 ? 0 : 1);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for(int i = 0; i < n; i++) {
            int begin = i * nblocksize;
            int end = (i + 1) * nblocksize;
            end = Math.min(end, contentBytes.length);
            
            byte[] bytes = copyOfRange(contentBytes, begin, end);
            // System.out.println(String.format("begin=%d, end=%d, size=%d", begin, end, end-begin));
            
            Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHMS);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            bytes = cipher.doFinal(bytes);

            // System.out.println(String.format("begin=%d, end=%d, size=%d, encodeLen=%d", begin, end, end-begin, bytes.length));
            baos.write(bytes);
        }
        return baos.toByteArray();
    }
    
    /**
     * 
     * @param encdata
     * @return
     * @throws Exception
     */
    public static byte[] decrypt2Bytes(PrivateKey privateKey, byte[] encdata) throws Exception {
        int n = encdata.length / blocksize + (encdata.length % blocksize == 0 ? 0 : 1);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for(int i = 0; i < n; i++) {
            int begin = i * blocksize;
            int end = (i + 1)* blocksize;
            end = Math.min(end, encdata.length);
            
            byte[] encbytes = copyOfRange(encdata, begin, end);
            
            Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHMS);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            baos.write(cipher.doFinal(encbytes));
        }
        return baos.toByteArray();
    }
    
    /**
     * 
     * @param original
     * @param from
     * @param to
     * @return
     */
    public static byte[] copyOfRange(byte[] original, int from, int to) {
        int newLength = to - from;
        if (newLength < 0) {
            throw new IllegalArgumentException(from + " > " + to);
        }
        newLength = Math.min(original.length - from, newLength);
        byte[] copy = new byte[newLength];
        System.arraycopy(original, from, copy, 0, newLength);
        return copy;
    }

    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = Base64.decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }
    
    public static PublicKey getPublicKey(String key) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decode(key));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }
     /**
      * 
      * @param args
      * @throws Exception
      */
    public static void main(String[] args) throws Exception {
        // 公钥
        String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw/Z2airKp9MKL+gHVwUoGp46BNodkftoUAeIikmkFY7v56mK1KhJVDHz5KBHDMehzWExJVHt1lhabilsImWv0Vd92UaBo7r3I3XDGGnBkumV/i3QfV510xR25TgRVqESG8r2uGVbA8FtEoIq4IVm4Fcjf5fKwrIlqH+A0YoLOHy6AFZKK1VU/ShgRj4WCzRJqFY6HcLsiJsxUqD4FAyRoSDcFNtqP9Of2j6vspCxJmkeeBQc0/y5SCHqmzTjYRooIObuBmwydB1A4+oR2MEswl15OLPkEry/iB8RzyMimAHU+sE8OczqPOxJ8YDGuAUTODhTKA3G+ZaJLFNideFqYwIDAQAB";
        // 私钥
        String PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDD9nZqKsqn0wov6AdXBSganjoE2h2R+2hQB4iKSaQVju/nqYrUqElUMfPkoEcMx6HNYTElUe3WWFpuKWwiZa/RV33ZRoGjuvcjdcMYacGS6ZX+LdB9XnXTFHblOBFWoRIbyva4ZVsDwW0SgirghWbgVyN/l8rCsiWof4DRigs4fLoAVkorVVT9KGBGPhYLNEmoVjodwuyImzFSoPgUDJGhINwU22o/05/aPq+ykLEmaR54FBzT/LlIIeqbNONhGigg5u4GbDJ0HUDj6hHYwSzCXXk4s+QSvL+IHxHPIyKYAdT6wTw5zOo87EnxgMa4BRM4OFMoDcb5loksU2J14WpjAgMBAAECggEAO7ZkSuGTMyRpnEN1yRx3JfVE9KmKDSuwk4JYJttdtnwBunO9LCPDhHZcPTRhshE6ypxtH1QhxwjRNXRF0Q1vT7j7TjPf2QZlvFZsBopjQ+MNo5BOUYVxHcrEmABHuEAIhpOILpQj8bcFB3jGm/fKa13iIUUdscobE4cWt8L6vFEsqpuvQLYz0vv/AYWsnBJxrJooUFCqb8YdygRwnQzDZw6bTMvYDhnEQdvWSKl/QCyKZobR/5SFuoDmHTDzId+DeBJcrSFY4VjuO1tn8GzE0fv1kNXxCZECqqrILAbe1AM5YrGn+lUjQ4hXc92Tc/81IKyj14Sr7G7rWVakx55cYQKBgQDrLUkUfvpwwH9awVkjHXuRrONmsxF7Jrlrv/BIlDykMjitdNrtULml///rXqs0J8u1iQclzTKX9HSr6g74HnKvtxGgaNIxbfVT2DuEKJAY4haxVjUE6glXSCWbW3YzY2RQpeFWGqBISGQS17t//CDMtjFw+gj68lOhfxxYGRNf+QKBgQDVUFENAW4wwKY6IqoohCeOlp1ZKqK+D7Qdgt5JzEy6zLE7/YQujtTTEPtpsHrOyRWfCvsdJVF8BcdwgaBdNne2378kWB79lF/6HhhLpU/afNCecVwBSkLM1W2YGtMtD0+ITnouuuNWY9v9NNTmG6sjdi9VmiEpB9CI3L+xIyesOwKBgHYf+vMWjIiRx+Y386PVrTmQk3AERaWL39qlP/XUr1jx+NH3Pfexcm5U1aDNnkTGMGXwe+ya0uzMVsX6/KXX9VcZJvthayJ0zqLcm5hYH3jAJxHP8TWUsDhs/qa4G/jX1Y1EpJhxK0zjP5DPV1iDtZ3dbZwHmvchPKyDgiMmBTtRAoGBAJ3yAHGO05ZJr0twi2nAoMGViCCTzzjHy+fmFM2z9kSCd+EhkBQhMjiuZFoXkHc4sUe4C8PC9BjO9qO5d5JNWEsSVSeGEDxW9tHuIrMwTnZK4PXQnf1ejSO6DJIJmr/MjGkzIokjXF2FR0ivK8zOQ+PYCKhrnY4R+Ti9qzpv+1otAoGBANdJEJxl+oSuMPhYNcWJTYbCYqx+IOD5jLjcHBpDYm2O+d0AiAITOFqgTy+Uhdhm0J1aeNuTcD/5nZRfSDxCpgdWp1zp1ggN8VA1ZlODzYWtUAQova/3De4fvG/DHuqGQXZEx2//oaD3+gHCNbqoJVFsVNkWS2mYNAhzEIQgA2j2";

        PrivateKey _PRIVATE_KEY = getPrivateKey(PRIVATE_KEY);
        PublicKey _PUBLIC_KEY = getPublicKey(PUBLIC_KEY);
        
        String plainData = "哦不错哦";
        
        byte[] plainBytes = plainData.getBytes(StandardCharsets.UTF_8);
        System.out.println("原始值Base64数据： " + Base64.encode(plainBytes)); 
        byte[] encPlainBytes = RSAHelper.encrypt(_PUBLIC_KEY, plainBytes);
        System.out.println("加密后Base64数据： " + Base64.encode(encPlainBytes)); 
        byte[] decPlainBytes = RSAHelper.decrypt2Bytes(_PRIVATE_KEY, encPlainBytes);
        System.out.println("解密后Base64数据： " + Base64.encode(decPlainBytes)); 
        
        String convertPlainData = new String(decPlainBytes, StandardCharsets.UTF_8);
        
        System.out.println("加密转化后的结果是否一直： " + convertPlainData.equals(plainData)); 
    }
}