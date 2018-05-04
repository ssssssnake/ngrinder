package org.ngrinder.common.util;

import java.io.IOException;

/**
 * @author ssssssnake
 **/
public final class ScriptUtils {

	/**
	 * @param shellPath, e.g., /lvdata/perftest/shell/startup.sh
	 */
	public static void execSh(String shellPath) throws IOException, InterruptedException {
		Process process = Runtime.getRuntime().exec(shellPath);
		byte[] b = new byte[1024];
		// flush the pipeline to prevent the child process from blocking
		while (process.getInputStream().read() != -1) {

		}
		process.waitFor();
	}
}
