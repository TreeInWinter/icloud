package com.travelzen.framework.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TimeZone;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailUtil {
	private final static Logger logger = LoggerFactory.getLogger(MailUtil.class);

	private final static String defaultContentType = "text/plain;charset=utf-8";

	private String to; // 收件人
	private String from; // 发件人
	private String host; // smtp主机
	private String username = "";
	private String password = "";
	private String subject; // 邮件主题
	private String content; // 邮件正文
	private String contentType;

	// 一个有规则的map，用作嵌入正文的图片
	Map<String, String> images;

	// 存放附件
	List<String> attachments;

	public MailUtil() {
	}

	public MailUtil(String to, String from, String smtpServer) {
		this.to = to;
		this.from = from;
		this.host = smtpServer;
	}

	public MailUtil(String to, String from, String smtpServer, String username) {
		this.to = to;
		this.from = from;
		this.host = smtpServer;
		this.username = username;
	}

	public MailUtil(String to, String from, String smtpServer, String username, String subject) {
		this.to = to;
		this.from = from;
		this.host = smtpServer;
		this.username = username;
		this.subject = subject;
	}

	public MailUtil(String to, String from, String smtpServer, String username, String subject, String content) {
		this(to, from, smtpServer, username, subject, content, defaultContentType, new HashMap<String, String>(), new ArrayList<String>());
	}

	public MailUtil(String to, String from, String smtpServer, String username, String subject, String content, String contentType) {
		this(to, from, smtpServer, username, subject, content, contentType, new HashMap<String, String>(), new ArrayList<String>());
	}

	public MailUtil(String to, String from, String smtpServer, String username, String subject, String content, String contentType, Map<String, String> images,
			List<String> attachments) {
		this.to = to;
		this.from = from;
		this.host = smtpServer;
		this.username = username;
		this.subject = subject;
		this.content = content;
		this.contentType = contentType;
		this.images = images;
		this.attachments = attachments;
	}

	public void attachfile(String file) {
		attachments.add(file);
	}

	public void setTo(String to) {
		this.to = to;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Map<String, String> getImages() {
		return images;
	}

	public void setImages(Map<String, String> images) {
		this.images = images;
	}

	public List<String> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<String> attachments) {
		this.attachments = attachments;
	}

	public boolean sendMail() {
		Properties properties = new Properties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.auth", "true");
		Session session = Session.getDefaultInstance(properties, new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		Transport transport = null;
		try {
			// MimeMessage mp = new MimeMessage(session);
			MimeMessage mp = createMessage(session);
			transport = session.getTransport("smtp");
			transport.connect(host, username, password);
			transport.sendMessage(mp, mp.getAllRecipients());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		} finally {
			if (transport != null)
				try {
					transport.close();
				} catch (MessagingException e) {
					logger.error(e.getMessage(), e);
				}
		}
		return true;
	}

	private MimeMessage createMessage(Session session) throws AddressException, MessagingException {
		MimeMessage msg = new MimeMessage(session);
		msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(to));
		msg.setFrom(new InternetAddress(from));
		msg.setSubject(subject);
		Multipart allMultipart = new MimeMultipart();
		// 添加正文
		MimeBodyPart contentpart = createContent(this.content);
		allMultipart.addBodyPart(contentpart);
		// 添加附件
		if (attachments != null && attachments.size() > 0) {
			for (String fileName : attachments) {
				allMultipart.addBodyPart(createAttachment(fileName));
			}
		}
		msg.setContent(allMultipart);

		// 设置发送时间
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
		fm.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		String moditime=fm.format(new Date());
		msg.setSentDate(new Date(Timestamp.valueOf(moditime).getTime()));
//		msg.setSentDate(new Date());
		msg.saveChanges();
		return msg;
	}

	/**
	 * 创建正文，如果是HTML格式邮件，拼装相应的图片资源
	 *
	 * @param content
	 * @return
	 * @throws MessagingException
	 */
	private MimeBodyPart createContent(String content) throws MessagingException {
		MimeBodyPart contentPart = new MimeBodyPart();
		Multipart contentMultipart = new MimeMultipart("related");

		// 创建用于保存HTML正文的MimeBodyPart对象，并将它保存到MimeMultipart中
		MimeBodyPart htmlbodypart = new MimeBodyPart();
		if (StringUtils.isEmpty(contentType)) {
			contentType = defaultContentType;
		}
		htmlbodypart.setContent(content, contentType);
		contentMultipart.addBodyPart(htmlbodypart);

		// 拼装嵌入图片资源
		if (images != null && images.size() > 0) {
			for (Entry<String, String> entry : images.entrySet()) {
				// 创建用于保存图片的MimeBodyPart对象，并将它保存到MimeMultipart中
				MimeBodyPart imageBodyPart = new MimeBodyPart();
				// 图片所在的目录的绝对路径
				FileDataSource fds = new FileDataSource(entry.getValue());
				imageBodyPart.setDataHandler(new DataHandler(fds));
				// cid的值
				imageBodyPart.setContentID(entry.getKey());
				contentMultipart.addBodyPart(imageBodyPart);
			}
		}
		contentPart.setContent(contentMultipart);
		return contentPart;
	}

	/**
	 * 创建附件
	 *
	 * @param fileName
	 * @return
	 * @throws MessagingException
	 */
	private BodyPart createAttachment(String fileName) throws MessagingException {
		MimeBodyPart attachmentPart = new MimeBodyPart();
		attachmentPart.setFileName(fileName);
		FileDataSource fds = new FileDataSource(fileName);
		attachmentPart.setDataHandler(new DataHandler(fds));
		return attachmentPart;
	}

}
