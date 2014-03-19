/**
 * 
 */
package com.travelzen.framework.monitor;


/**
 * @author shuiren
 *
 */
public interface ILogFileReader{
	/**
	 * 读取下一行，若碰到文件结尾，返回null
	 * @param 是否跳过空行
	 * @return
	 * @throws Exception
	 */
	public String readLine(boolean skipEmptyLine) throws Exception;
	/**
	 * 关闭文件
	 * @throws Exception
	 */
	public void close() throws Exception;
	/**
	 * 跳过charNum个字符
	 * @param charNum
	 * @throws Exception
	 */
	public void skipChars(long charNum) throws Exception;
	/**
	 * 获取当前字符所读取字符的位置
	 * @return
	 * @throws Exception
	 */
	public long getCurPosition() throws Exception;
}
