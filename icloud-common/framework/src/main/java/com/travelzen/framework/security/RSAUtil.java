/**
 * 
 */
package com.travelzen.framework.security;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import com.travelzen.framework.dict.DataDict;

/**
 * @author shuiren
 *
 */
public class RSAUtil {
    private static final String KEY_ALGORITHM = "RSA";   
    private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";   
    /**  
     * 用私钥对信息生成数字签名  
     *   
     * @param plain  
     *            待签名明文  
     * @param key  
     *            私钥串
     *   
     * @return  签名后的密文
     * @throws Exception  
     */  
    public static byte[] sign(byte[] plain, String key) throws Exception {   
    	PrivateKey privateKey = deSerializationPrivateKey(key);   
        // 用私钥对信息生成数字签名   
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);   
        signature.initSign(privateKey);   
        signature.update(plain);   
  
        return new Base64().encode(signature.sign());   
    }   

	/**  
     * 校验数字签名  
     *   
     * @param plain 参与验签的明文  
     *              
     * @param key  公钥串  
     * @param sign  参与验签的密文 
     *   
     * @return 校验成功返回true 失败返回false  
     * @throws Exception  
     *   
     */  
    public static boolean verify(byte[] plain, String key, byte[] sign)   

            throws Exception {   

        // 取公钥匙对象   
    	PublicKey publicKey = deSerializationPublicKey(key);   
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);   
        signature.initVerify(publicKey);   
        signature.update(plain);   
  
        // 验证签名是否正常   
        return signature.verify(new Base64().decode(sign));   
    }   
  
    /**  
     * 解密<br>  
     * 用私钥解密  
     *   
     * @param sign  密文
     * @param key  私钥串
     * @return  
     * @throws Exception  
     */  
    public static byte[] decryptByPrivateKey(byte[] sign, String key)   
            throws Exception {   
    	Key privateKey = deSerializationPrivateKey(key);   
        // 对数据加密   
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);   
        cipher.init(Cipher.DECRYPT_MODE, privateKey);   
  
        return cipher.doFinal(new Base64().decode(sign));   
    }   
  
    /**  
     * 解密<br>  
     * 用私钥解密  
     *   
     * @param sign  密文
     * @param key  公钥串
     * @return  
     * @throws Exception  
     */  
    public static byte[] decryptByPublicKey(byte[] sign, String key)   
            throws Exception {   
    	Key publicKey = deSerializationPublicKey(key);   
        // 对数据加密   
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);    
        cipher.init(Cipher.DECRYPT_MODE, publicKey);   
  
        return cipher.doFinal(new Base64().decode(sign));   
    }   
  
    /**  
     * 加密<br>  
     * 用公钥加密  
     *   
     * @param plain  明文
     * @param key  公钥串
     * @return  密文
     * @throws Exception  
     */  
    public static byte[] encryptByPublicKey(byte[] plain, String key)   
            throws Exception {   
    	Key publicKey = deSerializationPublicKey(key);   
        // 对数据加密   
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);   
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);   
  
        return new Base64().encode(cipher.doFinal(plain));   
    }   
  
    /**  
     * 加密<br>  
     * 用私钥加密  
     *   
     * @param plain  明文
     * @param key  私钥串
     * @return  密文
     * @throws Exception  
     */  
    public static byte[] encryptByPrivateKey(byte[] plain, String key)   
            throws Exception {   
        Key privateKey = deSerializationPrivateKey(key);   
        // 对数据加密   
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);   
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);   
  
        return new Base64().encode(cipher.doFinal(plain));   
    }

    /**
     *  从字符串中反序列化出公钥对象
     * @param key 公钥串
     * @return 公钥对象
     * @throws Exception
     */
    private static RSAPublicKey deSerializationPublicKey(String publicKeyStr) throws Exception{
        // Read key files back and decode them from BASE64
        byte[] publicKeyBytes = new Base64().decode(publicKeyStr.getBytes());

        // Convert back to public and private key objects
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        return (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);

    }
    /**
     * 从私钥串中反序列化出私钥对象
     * @param key
     * @return
     * @throws Exception
     */
    private static RSAPrivateKey deSerializationPrivateKey(String privateKeyStr) throws Exception{
    	// Read key files back and decode them from BASE64
        byte[] privateKeyBytes = new Base64().decode(privateKeyStr);

        // Convert back to public and private key objects
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
                privateKeyBytes);
        return (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);
    }
    /**  
     * 生成一对新的公私钥
     *   
     * @return  
     * @throws Exception  
     */  
    public static RSAKeyPair generateKeyPair() throws Exception {   
        java.security.KeyPairGenerator keyPairGen = KeyPairGenerator   
                .getInstance(KEY_ALGORITHM);   
        keyPairGen.initialize(1024);   
  
        KeyPair keyPair = keyPairGen.generateKeyPair();   
  
        // 公钥   
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();   
  
        // 私钥   
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();    
        
		byte[] privateKeyBytes = privateKey.getEncoded();
		byte[] publicKeyBytes = publicKey.getEncoded();
		String publicKeyJava = new String(new Base64().encode(publicKeyBytes));
		String privateKeyJava = new String(new Base64().encode(privateKeyBytes));
		String privateKeyNet = getRSAPrivateKeyAsNetFormat(privateKeyBytes);
		String publicKeyNet = getRSAPublicKeyAsNetFormat(privateKeyBytes);
        return new RSAKeyPair(publicKeyJava,privateKeyJava,publicKeyNet,privateKeyNet);   
    }
    /**
	 * @param privateKeyBytes
	 * @return
	 */
	private static String getRSAPublicKeyAsNetFormat(byte[] encodedPrivkey) throws Exception{
        StringBuffer buff = new StringBuffer(1024);

        PKCS8EncodedKeySpec pvkKeySpec = new PKCS8EncodedKeySpec(
                encodedPrivkey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPrivateCrtKey pvkKey = (RSAPrivateCrtKey) keyFactory
                .generatePrivate(pvkKeySpec);
        buff.append("<RSAKeyValue>");
        buff.append("<Modulus>"
                     + new String(new Base64().encode(removeMSZero(pvkKey.getModulus().toByteArray())))
                     + "</Modulus>");
        buff.append("<Exponent>"
                     + new String(new Base64().encode(removeMSZero(pvkKey.getPublicExponent().toByteArray()))) 
                     + "</Exponent>");
        buff.append("</RSAKeyValue>");
        return buff.toString().replaceAll("[ \t\n\r]", "");
	}

	private static String getRSAPrivateKeyAsNetFormat(byte[] encodedPrivkey) throws Exception{
		 StringBuffer buff = new StringBuffer(1024); 
		 PKCS8EncodedKeySpec pvkKeySpec = new PKCS8EncodedKeySpec(encodedPrivkey);
		 KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		 RSAPrivateCrtKey pvkKey = (RSAPrivateCrtKey) keyFactory.generatePrivate(pvkKeySpec);
		 buff.append("<RSAKeyValue>"); 
		 buff.append("<Modulus>" +  new String(new Base64().encode((removeMSZero(pvkKey.getModulus().toByteArray())))) + "</Modulus>");
		 buff.append("<Exponent>" +  new String(new Base64().encode((removeMSZero(pvkKey.getPublicExponent().toByteArray())))) + "</Exponent>");
		 buff.append("<P>" +  new String(new Base64().encode((removeMSZero(pvkKey.getPrimeP().toByteArray())))) + "</P>");
		 buff.append("<Q>" +  new String(new Base64().encode((removeMSZero(pvkKey.getPrimeQ().toByteArray())))) + "</Q>");
		 buff.append("<DP>" +  new String(new Base64().encode((removeMSZero(pvkKey.getPrimeExponentP().toByteArray())))) + "</DP>");
		 buff.append("<DQ>" +  new String(new Base64().encode((removeMSZero(pvkKey.getPrimeExponentQ().toByteArray())))) + "</DQ>");
		 buff.append("<InverseQ>" +  new String(new Base64().encode((removeMSZero(pvkKey.getCrtCoefficient().toByteArray())))) + "</InverseQ>");
		 buff.append("<D>" +  new String(new Base64().encode((removeMSZero(pvkKey.getPrivateExponent().toByteArray())))) + "</D>");
		 buff.append("</RSAKeyValue>"); 
		 return buff.toString().replaceAll("[ \t\n\r]", "");
    }
    private static byte[] removeMSZero(byte[] data) {
    	byte[] data1;
    	int len = data.length; 
    	if (data[0] == 0) {
    		data1 = new byte[data.length - 1];
    		System.arraycopy(data, 1, data1, 0, len - 1);
    	} else 
    		data1 = data;
    	return data1; 
    }
    /**
     * 验证平台签名
     * @param plain
     * @param sign
     * @return
     * @throws Exception 
     * @throws UnsupportedEncodingException 
     */
    public static boolean verifyPlatform(String plain, String sign) throws Exception{
    	return RSAUtil.verify(plain.getBytes(DataDict.CHARACTER_SET_ENCODING_UTF8), DataDict.PLATFORM_RSA_PUBLIC_KEY, StringUtils.trimToEmpty(sign).getBytes(DataDict.CHARACTER_SET_ENCODING_UTF8));
    }
    /**
     * 平台签名
     * @param plain
     * @return 签名后的密文
     * @throws Exception 
     * @throws UnsupportedEncodingException 
     */
    public static String signPlatform(String plain) throws Exception{
    	return new String(RSAUtil.sign(plain.getBytes(DataDict.CHARACTER_SET_ENCODING_UTF8), DataDict.PLATFORM_RSA_PRIVATE_KEY));
    }
    public static void main(String[] args) throws Exception{
//    	java.security.KeyPairGenerator keyPairGen = KeyPairGenerator   
//                .getInstance(KEY_ALGORITHM);   
//        keyPairGen.initialize(1024);   
//  
//        KeyPair keyPair = keyPairGen.generateKeyPair();   
//  
//        // 公钥   
//        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();   
//        // 对数据加密   
//        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);   
//        cipher.init(Cipher.ENCRYPT_MODE, publicKey);   
//        byte[] signByteArr = cipher.doFinal("renshui".getBytes());
//        cipher = Cipher.getInstance(KEY_ALGORITHM);   
//        PrivateKey priKey = deSerializationPrivateKey(serializationPrivateKey((RSAPrivateKey)keyPair.getPrivate()));
//        cipher.init(Cipher.DECRYPT_MODE, priKey);
//        System.out.println(new String(cipher.doFinal(signByteArr)));
//        System.out.println(serializationPrivateKey((RSAPrivateKey)keyPair.getPrivate()));
    }
} 

