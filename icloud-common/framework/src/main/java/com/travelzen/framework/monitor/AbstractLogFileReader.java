/**
 * 
 */
package com.travelzen.framework.monitor;

import java.io.BufferedReader;

/**
 * @author shuiren
 *
 */
public class AbstractLogFileReader{
	protected BufferedReader reader;
	protected long curPosition;
	protected boolean eof = false;
	//从底层的IO流中预读的字符, 最多预读5M个字符
	protected char[] charBuffer = new char[5 * 1024 * 1024];
	//charBuffer实际包含的字符数
	protected int charBufferSize = 0;
	//charBuffer中最后一个被访问字符的索引
	protected int charBufferIndex = -1;
	
	
//	public void skipLines(long lineNum) throws Exception {
//		// TODO Auto-generated method stub
//		while(lineNum > 0){
//			if(reader.readLine() == null)
//				throw new Exception("reach the end of file before skip " + lineNum + "lines");
//			lineNum--;
//		}
//	}

	private String readLine() throws Exception {
		// TODO Auto-generated method stub
//		return reader.readLine();
		StringBuffer line = new StringBuffer("");
		bufferLoop:
		for(;;){
			while(++charBufferIndex < charBufferSize){
				//读取下一个字符
				char curChar = charBuffer[charBufferIndex];
				//当前位置向前移1位
				curPosition++;
				//判断当前字符是不是终结符'\r','\n'
				if(curChar == '\r' || curChar == '\n')
					return line.toString();
				else line.append(curChar);
			}
			
			if(!eof){
			  //若未到文件尾，继续预读字符
				fill();
				continue bufferLoop;
			}else{
				//已到文件尾
				if(line.length() == 0)
					return null;
				return line.toString();
			}
		}
	}
    public String readLine(boolean skipEmptyLine) throws Exception{
    	if(skipEmptyLine){
    		String line = null;
    		do{
    			line = readLine();
    		}while(line != null && "".equals(line));
    		return line;
    	}else{
    		return readLine();
    	}
    }

	public void skipChars(long charNum) throws Exception {
		// TODO Auto-generated method stub
		reader.skip(charNum);
		curPosition += charNum;
	}

	public long getCurPosition() throws Exception {
		// TODO Auto-generated method stub
		return curPosition;
	}

	public void close() throws Exception {
		try{
			if(reader != null)
				reader.close();
		}catch(Throwable thr){
			
		}	
	}
	//填充charBuffer
	private void fill() throws Exception{
		charBufferSize = reader.read(charBuffer);
		charBufferIndex = -1;
		if(charBufferSize == 0 || charBufferSize == -1)
			eof = true;
	}
}
