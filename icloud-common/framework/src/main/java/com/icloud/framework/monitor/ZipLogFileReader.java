/**
 * 
 */
package com.icloud.framework.monitor;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * @author shuiren
 *
 */
public class ZipLogFileReader extends AbstractLogFileReader implements ILogFileReader{
	protected ZipFile zf;
	public ZipLogFileReader(String filePath) throws Exception{
		zf = new ZipFile(filePath);  
        InputStream in = new BufferedInputStream(new FileInputStream(filePath));  
        ZipInputStream zin = new ZipInputStream(in);  
        ZipEntry ze;  
        while ((ze = zin.getNextEntry()) != null) {  
            if (!ze.isDirectory()){  
                   reader = new BufferedReader(new InputStreamReader(zf.getInputStream(ze))); 
                   break; 
            }  
        }  
        zin.closeEntry();
	}

	/* (non-Javadoc)
	 * @see com.travelzen.framework.monitor.ILogFileReader#close()
	 */
	@Override
	public void close() throws Exception {
		super.close();
		try{
			if(zf != null)
				zf.close();
		}catch(Throwable thr){
			
		}
		
	}

}
