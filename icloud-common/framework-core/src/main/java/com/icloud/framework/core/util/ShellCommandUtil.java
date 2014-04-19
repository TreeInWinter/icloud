package com.icloud.framework.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShellCommandUtil {

	public static Logger logger = LoggerFactory
			.getLogger(ShellCommandUtil.class);

	static public String runCommand(String script) {

		StringBuffer result = new StringBuffer();

		String cmd = script;
		Runtime run = Runtime.getRuntime();
		try {
			Process pr = run.exec(cmd);
			pr.waitFor();
			BufferedReader buf = new BufferedReader(new InputStreamReader(
					pr.getInputStream()));

			String line;
			while ((line = buf.readLine()) != null) {
				result.append(line);
			}
		} catch (InterruptedException e) {
			logger.error(e.getLocalizedMessage());
		} catch (IOException e) {
			logger.error(e.getLocalizedMessage());
		}

		return result.toString();

	}

	static public String runCommandAdv(String cmd) throws IOException {

		StringBuffer result = new StringBuffer();

		// String cmd = "cd ../.. ; ls -l"; // this is the command to execute in
		// // the Unix shell
		// cmd = "cd ~/kaven/Tools/DART ; sh start_dart.sh";

		// create a process for the shell
		ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
		pb.redirectErrorStream(true); // use this to capture messages sent to
										// stderr

		Process shell = pb.start();
		InputStream shellIn = shell.getInputStream(); // this captures the
														// output from the
														// command
		try {
			int shellExitStatus = shell.waitFor();
			logger.info("shell exit status: " + shellExitStatus);
		} catch (InterruptedException e) {
			logger.error(e.getLocalizedMessage());
		} // wait for the shell to finish
			// and get the return code

		// at this point you can process the output issued by the command
		// for instance, this reads the output and writes it to System.out:
		int c;
		while ((c = shellIn.read()) != -1) {
			result.append(c);
		}
		// close the stream
		try {
			shellIn.close();
		} catch (IOException ignoreMe) {
		}
		// System.out.print(" *** End *** " + shellExitStatus);
		//
		// System.out.println(pb.command());
		// System.out.println(pb.directory());
		// System.out.println(pb.environment());
		// System.out.println(System.getenv());
		// System.out.println(System.getProperties());

		return result.toString();
	}
}
