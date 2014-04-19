/**
 * 
 */
package com.icloud.framework.monitor;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.icloud.framework.core.util.RPIDLogger;

import freemarker.template.Template;

/**
 * @author shuiren
 *
 */
public class SendShortLogExceptionMail {
	private JavaMailSender mailSender;
	//发件人邮箱地址
	private String from;
	//收件人邮箱地址
	private String to;
	//邮件主题
	private String subject;
	//邮件内容字符集编码
	private String encoding;

	private FreeMarkerConfigurer freemarkerConfig;
	
	private String mailTemplatePath;
	
	
	public void sendMail(List<String> exceptionList) throws Exception{
		RPIDLogger.info("发送邮件开始");
	    String localIp = getLocalIp();
//	    RPIDLogger.debug(localIp);
	    MimeMessage message = mailSender.createMimeMessage();
	    // use the true flag to indicate you need a multipart message
	    MimeMessageHelper helper = new MimeMessageHelper(message, true, encoding);
	    helper.setFrom(from);
	    helper.setTo(to.split(","));	   
	    helper.setSubject(subject);
	    Map<String, Object> emailData = new HashMap<String, Object>();
	    emailData.put("localIp", localIp);
	    emailData.put("appName", "wostore-ads-api");
	    emailData.put("exceptionList", exceptionList);
        helper.setText(buildMailText(emailData),true);
        mailSender.send(message);
        RPIDLogger.info("发送邮件成功");
	}
	@SuppressWarnings("rawtypes")
	private String buildMailText(Map map) throws Exception{
		Template tpl = freemarkerConfig.getConfiguration().getTemplate(mailTemplatePath);
		return FreeMarkerTemplateUtils.processTemplateIntoString(tpl,map);
	}
	/**
	 * 获取本地ip，获取不到返回null
	 * @return
	 */
	private String getLocalIp(){
       String ip = null;
       try {
           Enumeration<?> e1 = (Enumeration<?>) NetworkInterface.getNetworkInterfaces();
           while (e1.hasMoreElements()) {
               NetworkInterface ni = (NetworkInterface) e1.nextElement();
               if (!ni.getName().startsWith("eth") && !ni.getName().startsWith("bond")) {
                   continue;
               } else {
                   Enumeration<?> e2 = ni.getInetAddresses();
                   while (e2.hasMoreElements()) {
                       InetAddress ia = (InetAddress) e2.nextElement();
                       if (ia instanceof Inet6Address)
                           continue;
                       ip = ia.getHostAddress();
                   }
                   break;
               }
           }
       } catch (Throwable thr) {
           return null;
       }
       return ip;
	}
	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}
	/**
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return the mailSender
	 */
	public JavaMailSender getMailSender() {
		return mailSender;
	}
	/**
	 * @param mailSender the mailSender to set
	 */
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}
	/**
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	/**
	 * @return the freemarkerConfig
	 */
	public FreeMarkerConfigurer getFreemarkerConfig() {
		return freemarkerConfig;
	}
	/**
	 * @param freemarkerConfig the freemarkerConfig to set
	 */
	public void setFreemarkerConfig(FreeMarkerConfigurer freemarkerConfig) {
		this.freemarkerConfig = freemarkerConfig;
	}
	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}
	/**
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}
	public String getMailTemplatePath() {
		return mailTemplatePath;
	}
	public void setMailTemplatePath(String mailTemplatePath) {
		this.mailTemplatePath = mailTemplatePath;
	}
}
