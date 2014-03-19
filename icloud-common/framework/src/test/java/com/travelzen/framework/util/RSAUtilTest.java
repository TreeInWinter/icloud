/**
 * 
 */
package com.travelzen.framework.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.travelzen.framework.core.dict.DataDict;
import com.travelzen.framework.security.RSAKeyPair;
import com.travelzen.framework.security.RSAUtil;

/**
 * @author shuiren
 *
 */
public class RSAUtilTest {

	@Test
	public void test() throws Exception{
		RSAKeyPair keyPair = RSAUtil.generateKeyPair();
		System.out.println("public key for java:" + keyPair.getPublicKeyJava());
		System.out.println("private key for java:" + keyPair.getPrivateKeyJava());
		System.out.println("public key for net:" + keyPair.getPublicKeyNet());
		System.out.println("private key for net:" + keyPair.getPrivateKeyNet());
		//公钥加密，私钥解密
		String plain = "renshui";
		String sign = new String(RSAUtil.encryptByPublicKey(plain.getBytes(), keyPair.getPublicKeyJava()));
//		System.out.println(sign);
		assertEquals(plain, new String(RSAUtil.decryptByPrivateKey(sign.getBytes(), keyPair.getPrivateKeyJava())));
//		//私钥加密，公钥解密
		sign = new String(RSAUtil.encryptByPrivateKey(plain.getBytes(), keyPair.getPrivateKeyJava()));
	    assertEquals(plain, new String(RSAUtil.decryptByPublicKey(sign.getBytes(), keyPair.getPublicKeyJava())));
	    //先签名，后验签
	    byte[] signArr = RSAUtil.sign(plain.getBytes(), keyPair.getPrivateKeyJava());
	    assertTrue(RSAUtil.verify(plain.getBytes(), keyPair.getPublicKeyJava(), signArr));
	}
	@Test
	public void test_platformKey() throws Exception{
		String plain = "任水";
	    //先签名，后验签
	    byte[] signArr = RSAUtil.sign(plain.getBytes(), DataDict.PLATFORM_RSA_PRIVATE_KEY);
	    assertTrue(RSAUtil.verify(plain.getBytes(), DataDict.PLATFORM_RSA_PUBLIC_KEY, signArr));
	}
}
