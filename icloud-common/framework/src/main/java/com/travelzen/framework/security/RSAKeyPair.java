/**
 * 
 */
package com.travelzen.framework.security;


/**
 * @author shuiren
 *
 */
public class RSAKeyPair{
	private String publicKeyJava;
	private String privateKeyJava;
	private String publicKeyNet;
	private String privateKeyNet;
	/**
	 * @param publicKeyJava2
	 * @param privateKeyJava2
	 * @param publicKeyNet2
	 * @param privateKeyNet2
	 */
	public RSAKeyPair(String publicKeyJava, String privateKeyJava,
			String publicKeyNet, String privateKeyNet) {
		this.publicKeyJava = publicKeyJava;
		this.privateKeyJava = privateKeyJava;
		this.publicKeyNet = publicKeyNet;
		this.privateKeyNet = privateKeyNet;
	}
	public String getPublicKeyJava() {
		return publicKeyJava;
	}
	/**
	 * @param publicKeyJava the publicKeyJava to set
	 */
	public void setPublicKeyJava(String publicKeyJava) {
		this.publicKeyJava = publicKeyJava;
	}
	/**
	 * @return the privateKeyJava
	 */
	public String getPrivateKeyJava() {
		return privateKeyJava;
	}
	/**
	 * @param privateKeyJava the privateKeyJava to set
	 */
	public void setPrivateKeyJava(String privateKeyJava) {
		this.privateKeyJava = privateKeyJava;
	}
	/**
	 * @return the publicKeyNet
	 */
	public String getPublicKeyNet() {
		return publicKeyNet;
	}
	/**
	 * @param publicKeyNet the publicKeyNet to set
	 */
	public void setPublicKeyNet(String publicKeyNet) {
		this.publicKeyNet = publicKeyNet;
	}
	/**
	 * @return the privateKeyNet
	 */
	public String getPrivateKeyNet() {
		return privateKeyNet;
	}
	/**
	 * @param privateKeyNet the privateKeyNet to set
	 */
	public void setPrivateKeyNet(String privateKeyNet) {
		this.privateKeyNet = privateKeyNet;
	}
	
}