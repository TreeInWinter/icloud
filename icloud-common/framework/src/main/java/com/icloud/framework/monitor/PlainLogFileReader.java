/**
 * 
 */
package com.icloud.framework.monitor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * @author shuiren
 *
 */
public class PlainLogFileReader extends AbstractLogFileReader implements ILogFileReader{
	public PlainLogFileReader(String filePath) throws Exception{
		reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
	}
}
