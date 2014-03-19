/**
 * 
 */
package com.travelzen.framework.monitor;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;

import com.travelzen.framework.core.time.DateTimeUtil;
import com.travelzen.framework.core.util.RPIDLogger;
import com.travelzen.framework.core.util.RandomUtil;

/**
 * @author shuiren
 *
 */
public class MonitorShortLogTask {
	private String shortLogPath;
	private String archiveShortLogPathPattern;
	private SendShortLogExceptionMail sendShortLogExceptionMail;
	private static Pattern normalPattern = Pattern.compile(".*,0000,.*");
	//上一次扫描到哪一行
	private static long lastPosition = 0;
	//上一次扫描时shortLog的修改日期
	private static Date shortLogLastModDate = null;
	
	public void doTask(){
		RPIDLogger.setRpid(RandomUtil.getRandomStr(10));
		File shortLogFile = new File(shortLogPath);
		Date shortLogCurModDate = new Date(shortLogFile.lastModified());
		if(shortLogLastModDate != null && shortLogLastModDate.getTime() == shortLogCurModDate.getTime()){
			RPIDLogger.debug("当前日志文件没有新日志，不扫描");
			return;
		}
		List<String> exceptionList = new ArrayList<String>();
		//读取简要日志，过滤出异常日志
		if(shortLogLastModDate != null && !DateTimeUtil.date8(new DateTime(shortLogLastModDate.getTime())).equals(DateTimeUtil.date8(new DateTime(shortLogCurModDate.getTime())))){
			//如果上次扫描文件的日期和当前日期不同，则先扫描归档文件
			RPIDLogger.debug("扫描归档日志开始");
			try{
				String archiveShortLogPath = parseArchiveShortLogPath(DateTimeUtil.date8(new DateTime(shortLogLastModDate.getTime())));
				ILogFileReader reader = new ZipLogFileReader(archiveShortLogPath);
				reader.skipChars(lastPosition);
				String line = null;
				while((line = reader.readLine(true)) != null){
					if(isExceptionLine(line))
						exceptionList.add(line);
				}
				reader.close();
				RPIDLogger.debug("扫描归档日志完成");
			}catch(Throwable thr){
				//抛出异常忽略
				RPIDLogger.error("扫描归档文件异常，该异常可忽略", thr);
			}
			lastPosition = 0;
		}
		//读取当前日志文件
		RPIDLogger.debug("扫描当前日志开始");
		try{
			ILogFileReader reader = new PlainLogFileReader(shortLogPath);
			reader.skipChars(lastPosition);
			String line = null;
			while((line = reader.readLine(true)) != null){
				if(isExceptionLine(line))
					exceptionList.add(line);
			}
			lastPosition = reader.getCurPosition();
			reader.close();
			RPIDLogger.debug("扫描当前日志完成");
		}catch(Throwable thr){
			//抛出异常忽略
			RPIDLogger.error("扫描日志文件异常，该异常可忽略", thr);
		}
		shortLogLastModDate = shortLogCurModDate;
		//将异常日志通过邮箱发送给运维人员
		try {
			if(exceptionList.size() > 0)
				sendShortLogExceptionMail.sendMail(exceptionList);
			else
				RPIDLogger.debug("本次扫描未发现异常日志");
		}catch(Throwable thr){
			//抛出异常忽略
			RPIDLogger.error("发送报警邮件异常", thr);
		}
	}
	/**
	 * @param archiveShortLogPathPattern2
	 * @return
	 */
	private String parseArchiveShortLogPath(String lastReadDate) throws Exception{
		//替换模式中的日期，日期用{}包围
		int openMarkerIndex = archiveShortLogPathPattern.indexOf("{");
		int closeMarkerIndex = archiveShortLogPathPattern.indexOf("}");
		if(openMarkerIndex == -1 || closeMarkerIndex == -1)
			return archiveShortLogPathPattern;
		if(openMarkerIndex >= closeMarkerIndex)
			throw new Exception("unmatched '{','}'");
		String filePrefix = archiveShortLogPathPattern.substring(0, openMarkerIndex);
		String fileSuffix = archiveShortLogPathPattern.substring(closeMarkerIndex + 1);
		String datePattern = archiveShortLogPathPattern.substring(openMarkerIndex + 1, closeMarkerIndex);
		String date = new SimpleDateFormat(datePattern).format(DateTimeUtil.parseDate8(lastReadDate));
		return filePrefix + date + fileSuffix;
	}
	private boolean isExceptionLine(String line){
		Matcher matcher = normalPattern.matcher(line);
		return !matcher.matches();
	}
	/**
	 * @return the shortLogPath
	 */
	public String getShortLogPath() {
		return shortLogPath;
	}
	/**
	 * @param shortLogPath the shortLogPath to set
	 */
	public void setShortLogPath(String shortLogPath) {
		this.shortLogPath = shortLogPath;
	}
	/**
	 * @return the archiveShortLogPathPattern
	 */
	public String getArchiveShortLogPathPattern() {
		return archiveShortLogPathPattern;
	}
	/**
	 * @param archiveShortLogPathPattern the archiveShortLogPathPattern to set
	 */
	public void setArchiveShortLogPathPattern(String archiveShortLogPathPattern) {
		this.archiveShortLogPathPattern = archiveShortLogPathPattern;
	}
	/**
	 * @return the sendShortLogExceptionMail
	 */
	public SendShortLogExceptionMail getSendShortLogExceptionMail() {
		return sendShortLogExceptionMail;
	}
	/**
	 * @param sendShortLogExceptionMail the sendShortLogExceptionMail to set
	 */
	public void setSendShortLogExceptionMail(
			SendShortLogExceptionMail sendShortLogExceptionMail) {
		this.sendShortLogExceptionMail = sendShortLogExceptionMail;
	}
	public static void main(String[] args) throws Exception{
		Matcher matcher = normalPattern.matcher("22:45:12.214 - [5898951179]:/report,0000,生成报表成功,4fb6528d0c49e7591da2ea41,huoshuqiang1@163.com,20120501,20120507");
		System.out.println(matcher.matches());
	}
}
