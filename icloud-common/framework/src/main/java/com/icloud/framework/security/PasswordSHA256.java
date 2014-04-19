package com.icloud.framework.security;

import java.security.SecureRandom;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * <p>
 * Description: 密码加密
 * 
 * @version 1.0
 */
public class PasswordSHA256 {

	private static final int SALT_ORIGINAL_LEN = 8;

	private static final int SALT_ENCODED_LEN = 12;

	/**
	 * @method digestPassword
	 * @param password
	 *            需要加密的字符串
	 * @description 加密
	 */
	public static String digestPassword(String password) {
		try {

			DigestUtils.sha256Hex(password);

			SecureRandom random = new SecureRandom();
			byte[] salt = new byte[SALT_ORIGINAL_LEN];
			random.nextBytes(salt);

			byte[] digest = DigestUtils.sha256(password.getBytes());

			return Base64.encodeBase64String(salt)
					+ Base64.encodeBase64String(digest);

		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}

	/**
	 * @method validatePassword
	 * @param password
	 *            用户输入的明文密码，如：123456
	 * @param digest
	 *            密文密码
	 * @return true:密码相同 false:密码不同
	 * @description 验证一个明文密码和一个密文密码是否相等ͬ
	 */
	public static boolean validatePassword(String password, String digest) {
		boolean label = false;
		try {
//			String salt_str = digest.substring(0, SALT_ENCODED_LEN);
			String digest_str = digest.substring(SALT_ENCODED_LEN,
					digest.length());

			byte[] digest_new = DigestUtils.sha256(password.getBytes());

//			byte[] salt = Base64.decodeBase64(salt_str);
			byte[] digest_old = Base64.decodeBase64(digest_str);

			label = Arrays.equals(digest_old, digest_new);

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return label;
	}

	public static void main(String[] args) throws Exception {

		System.out.println(PasswordSHA256.digestPassword("wangliang"));

		System.out.print(validatePassword("wangliang",
				"oeEvibQPUio=flEoMuT13ZutBXs8JlpRIVqsdNXwbat00dVIYEBy08U="));
		// System.out.print(validatePassword("111111",
		// "X5XViDmsOjQ=hfkhTnxxfyYIU3fLPD3PCA=="));
		// System.out.print(validatePassword("123456",
		// "cXGwarlK0+0=lnqGYV\/\/bnG+18CWRL9UBg=="));

	}
}
