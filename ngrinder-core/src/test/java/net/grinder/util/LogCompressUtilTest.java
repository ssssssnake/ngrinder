/* 
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package net.grinder.util;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class LogCompressUtilTest {
	@Test
	public void testLogCompressUnCompress() throws IOException {
		File file = new File(LogCompressUtilTest.class.getResource("/grinder1.properties").getFile());
		byte[] zipedContent = LogCompressUtil.compressFile(file);
		File createTempFile2 = File.createTempFile("a22", "zip");
		createTempFile2.deleteOnExit();
		FileUtils.writeByteArrayToFile(createTempFile2, zipedContent);
		File createTempFile = File.createTempFile("a22", "tmp");
		LogCompressUtil.unCompress(zipedContent, createTempFile);
		assertThat(createTempFile.exists(), is(true));
		byte[] unzipedContent = FileUtils.readFileToByteArray(createTempFile);
		assertThat(FileUtils.readFileToByteArray(file), is(unzipedContent));

	}
}