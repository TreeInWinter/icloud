package com.icloud.framework.logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.status.StatusManager;
import ch.qos.logback.core.status.ViewStatusMessagesServletBase;

/**
 * 扩展logback默认的web接口 查看特定日志变化
 */
public class WebLogViewServlet extends ViewStatusMessagesServletBase {
	private static final long serialVersionUID = 1L;

	@Override
	protected StatusManager getStatusManager(HttpServletRequest req,
			HttpServletResponse resp) {
		String name = req.getParameter("name");
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		return lc.getStatusManager();
	}

	@Override
	protected String getPageTitle(HttpServletRequest req,
			HttpServletResponse resp) {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		return "<h2>Status messages for LoggerContext named [" + lc.getName()
				+ "]</h2>\r\n";
	}

}
