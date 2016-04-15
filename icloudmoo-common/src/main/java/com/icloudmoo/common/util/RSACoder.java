package com.icloudmoo.common.util;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

import javax.crypto.Cipher;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.RSAPrivateKeyStructure;
import org.bouncycastle.asn1.x509.RSAPublicKeyStructure;

import com.icloudmoo.common.vo.Constants;

/**
 * RSA安全编码组件
 * 
 * @author Leoly
 * @version 1.0
 * @since 1.0
 */
public class RSACoder {
    public static final String KEY_ALGORITHM = "RSA/ECB/PKCS1Padding";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    public static final String PUBLIC_KEY = "RSAPublicKey";
    public static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * 私钥
     */
    private static RSAPrivateKey privateKey;

    /**
     * 公钥
     */
    private static RSAPublicKey publicKey;

    /**
     * 用私钥对信息生成数字签名
     * 
     * @param data
     *            加密数据
     * @param privateKey
     *            私钥
     * 
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data) throws Exception {
        // 用私钥对信息生成数字签名
        // Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM, new
        // BouncyCastleProvider());
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateKey);
        signature.update(data);
        return CodingUtil.base64Encode(signature.sign());
    }

    /**
     * 校验数字签名
     * 
     * @param data
     *            加密数据
     * @param publicKey
     *            公钥
     * @param sign
     *            数字签名
     * 
     * @return 校验成功返回true 失败返回false
     * @throws Exception
     * 
     */
    public static boolean verify(byte[] data, String sign) throws Exception {
        // Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM, new
        // BouncyCastleProvider());
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicKey);
        signature.update(data);
        // 验证签名是否正常
        return signature.verify(CodingUtil.base64Decode(sign));
    }

    /**
     * 解密<br>
     * 用私钥解密
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data) throws Exception {
        // 对数据解密
        // Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm(), new
        // BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 解密<br>
     * 用私钥解密
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String data) throws Exception {
        byte[] temp = decryptByPrivateKey(CodingUtil.base64Decode(data));
        return IOUtils.toString(temp, Constants.DEFAULT_CHARSET);
    }

    /**
     * 解密<br>
     * 用私钥解密
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String data, String privateKeyStr) throws Exception {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        RSAPrivateKey priKey = getPrivateKey(privateKeyStr);
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        byte[] temp = cipher.doFinal(CodingUtil.base64Decode(data));
        return IOUtils.toString(temp, Constants.DEFAULT_CHARSET);
    }

    /**
     * 解密<br>
     * 用公钥解密
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data) throws Exception {
        // Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm(), new
        // BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * 解密<br>
     * 用公钥解密
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptByPublicKey(String data) throws Exception {
        byte[] temp = decryptByPublicKey(CodingUtil.base64Decode(data));
        return IOUtils.toString(temp, Constants.DEFAULT_CHARSET);
    }

    /**
     * 解密<br>
     * 用公钥解密
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptByPublicKey(String data, String publicKeyStr) throws Exception {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        RSAPublicKey pubKey = getPublicKey(publicKeyStr);
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        byte[] temp = cipher.doFinal(CodingUtil.base64Decode(data));
        return IOUtils.toString(temp, Constants.DEFAULT_CHARSET);
    }

    /**
     * 加密<br>
     * 用公钥加密
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data) throws Exception {
        // 对数据加密
        // Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm(), new
        // BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * 加密<br>
     * 用公钥加密
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String data) throws Exception {
        byte[] temp = encryptByPublicKey(data.getBytes(Constants.DEFAULT_CHARSET));
        return CodingUtil.base64Encode(temp);
    }

    /**
     * 加密<br>
     * 用公钥加密
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String data, String publicKeyStr) throws Exception {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        RSAPublicKey pubKey = getPublicKey(publicKeyStr);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] temp = cipher.doFinal(data.getBytes(Constants.DEFAULT_CHARSET));
        return CodingUtil.base64Encode(temp);
    }

    /**
     * 加密<br>
     * 用私钥加密
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data) throws Exception {
        // 对数据加密
        // Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm(), new
        // BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 加密<br>
     * 用私钥加密
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptByPrivateKey(String data) throws Exception {
        byte[] temp = encryptByPrivateKey(data.getBytes(Constants.DEFAULT_CHARSET));
        return CodingUtil.base64Encode(temp);
    }

    /**
     * 加密<br>
     * 用私钥加密
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptByPrivateKey(String data, String privateKeyStr) throws Exception {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        RSAPrivateKey priKey = getPrivateKey(privateKeyStr);
        cipher.init(Cipher.ENCRYPT_MODE, priKey);
        byte[] temp = cipher.doFinal(data.getBytes(Constants.DEFAULT_CHARSET));
        return CodingUtil.base64Encode(temp);
    }

    /**
     * 初始化密钥
     * 
     * @return
     * @throws Exception
     */
    public static void initKey() throws Exception {
        String privateKeyStr = loadRsaKey("private_key.pem");
        byte[] buffer = CodingUtil.base64Decode(privateKeyStr);
        // PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
        // // KeyFactory keyFactory = KeyFactory.getInstance("RSA", new
        // // BouncyCastleProvider());
        // KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        // privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);

        RSAPrivateKeyStructure asn1PrivKey = new RSAPrivateKeyStructure(
                (ASN1Sequence) ASN1Sequence.fromByteArray(buffer));
        RSAPrivateKeySpec rsaPrivKeySpec = new RSAPrivateKeySpec(asn1PrivKey.getModulus(),
                asn1PrivKey.getPrivateExponent());
        KeyFactory kf = KeyFactory.getInstance("RSA");
        privateKey = (RSAPrivateKey) kf.generatePrivate(rsaPrivKeySpec);

        String publicKeyStr = loadRsaKey("public_key.pem");
        buffer = CodingUtil.base64Decode(publicKeyStr);
        RSAPublicKeyStructure asn1PublicKey = new RSAPublicKeyStructure(
                (ASN1Sequence) ASN1Sequence.fromByteArray(buffer));
        RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(asn1PublicKey.getModulus(),
                asn1PublicKey.getPublicExponent());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        publicKey = (RSAPublicKey) keyFactory.generatePublic(rsaPublicKeySpec);
    }

    /**
     * TODO: 由公钥字符串获取公钥对象
     * 
     * @param publicKeyStr
     * @return
     * @throws Exception
     */
    private static RSAPublicKey getPublicKey(String publicKeyStr) throws Exception {
        byte[] buffer = CodingUtil.base64Decode(publicKeyStr);
        // RSAPublicKeyStructure asn1PublicKey = new RSAPublicKeyStructure(
        // (ASN1Sequence) ASN1Sequence.fromByteArray(buffer));
        // RSAPublicKeySpec rsaPublicKeySpec = new
        // RSAPublicKeySpec(asn1PublicKey.getModulus(),
        // asn1PublicKey.getPublicExponent());
        // KeyFactory keyFactory = KeyFactory.getInstance("RSA", new
        // BouncyCastleProvider());
        // return (RSAPublicKey) keyFactory.generatePublic(rsaPublicKeySpec);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(buffer);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) kf.generatePublic(spec);
    }

    /**
     * TODO: 由公钥字符串获取私钥对象
     * 
     * @param privateKeyStr
     * @return
     * @throws Exception
     */
    private static RSAPrivateKey getPrivateKey(String privateKeyStr) throws Exception {
        byte[] buffer = CodingUtil.base64Decode(privateKeyStr);
        // RSAPrivateKeyStructure asn1PrivKey = new RSAPrivateKeyStructure(
        // (ASN1Sequence) ASN1Sequence.fromByteArray(buffer));
        // RSAPrivateKeySpec rsaPrivKeySpec = new
        // RSAPrivateKeySpec(asn1PrivKey.getModulus(),
        // asn1PrivKey.getPrivateExponent());
        // KeyFactory kf = KeyFactory.getInstance("RSA");
        // return (RSAPrivateKey) kf.generatePrivate(rsaPrivKeySpec);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
        // KeyFactory keyFactory = KeyFactory.getInstance("RSA", new
        // BouncyCastleProvider());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    /**
     * @description 加载密钥字符串
     * 
     * @return 密钥字符串
     * @throws Exception
     */
    private static String loadRsaKey(String keyFile) throws Exception {
        InputStream br = RSACoder.class.getResourceAsStream(keyFile);
        try {
            List<String> lines = IOUtils.readLines(br, Constants.DEFAULT_CHARSET);
            StringBuilder sb = new StringBuilder();
            for (String line : lines) {
                if (line.startsWith("-----")) {
                    continue;
                }

                sb.append(line).append(IOUtils.LINE_SEPARATOR);
            }

            return sb.toString();
        }
        catch (Exception e) {
            throw new Exception("私钥数据读取错误");
        }
        finally {
            IOUtils.closeQuietly(br);
        }
    }

    public static void main(String[] args) throws Exception {
        String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAPIlraI/Mdhw2WQG\r\ntR3yYytoqddKntDKUoBU+311s9XLHR7tfyAKsALbzyXF5VyNRoiBV6mbjViTP28n\r\n9ymPFPo2kU6sv2qkOJdNWEOGFTVi9g3bRyz+vSH9YIMRMc/m6NVus/hYECY+IXqP\r\n1+y6mPmWQyUXTufJL5d08J9Yhq/lAgMBAAECgYAVF/0pLhIchbAaS49JfYzwAh0R\r\neRmenJFVRpHl2vQAgsIVqKCzXNr6VMWVx8h0KIZla8cEKl3Ewob7GuMoBqPQAMGp\r\niNtuiJ5/kdrqN49wnBhupeXj1KHKIcqutqPfU7jIuasENE3Gk6xPJnR9I6QlLKSy\r\nMy1cb6b3/POH+/4FfQJBAPtA/zAH8slKdL2FMTTEZ/eYMOabt783B9eBYejMNb2X\r\nW5m8qDK27kg/SIyXE+UiF6OCDNyT1aYI8HRBVN36AdcCQQD2uKTAm7Gb1jndEIvF\r\nmt93bVhMwlbd9z9uEx979INx6WD4LZ8v4xyKzqcDw0Oj8lbngT9WPP5ziJoufUuq\r\nfByjAkEAnw8xviV6BQZ2yzgTw5UHQI5/fq5b79iVxU9qPWdyKhk06ymszZZTWTd+\r\n5UoLVUZ08pU1MTsByT/lgaOOZ4yLNwJBAJa5RIUaWI2weRcRNaJZs+080rXJ/9GI\r\na6quagyK2wZrIb9b96UEpPoztxp4Xsk4kljJv8zKZFRmfnKqPuB/A+sCQF/8aDmi\r\nfTjhKlEiaovfrVqXkCtXQkcU+a8aWe63TqXTVojYzl++4WMhiLTrBXInPd5cqiZl\r\nQEOP4BRLaXDHfuM=\r\n";
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDyJa2iPzHYcNlkBrUd8mMraKnX\r\nSp7QylKAVPt9dbPVyx0e7X8gCrAC288lxeVcjUaIgVepm41Ykz9vJ/cpjxT6NpFO\r\nrL9qpDiXTVhDhhU1YvYN20cs/r0h/WCDETHP5ujVbrP4WBAmPiF6j9fsupj5lkMl\r\nF07nyS+XdPCfWIav5QIDAQAB";
        System.out.println(encryptByPrivateKey(
                "{accessCode:'Test',password:'6661e5178e8d61f244011ec5a6ad2707',accessIp:'10.243.18.83'}", privateKey));
        System.out.println(decryptByPrivateKey(
                "DQ5G4cS94df_9CrXMT13DvV_aSRRUGz5-cubDkexLdOAmIcUdKXwI_QyH5dV jYPBNxesK_mzsoHs9pphLJoOLiD_4on4igFd8tkVJUfqCSWPVDYIkXZVlYJC dFkXEI2AFhfPpMnNWDzywPfzozOB1c91wVuPOwImkwGNQWMk6J4=",
                privateKey));
    }
}
