package org.ngrinder.benchmark.zmq;

import com.ztesoft.mq.client.api.MQClientFactory;
import com.ztesoft.mq.client.api.common.PropertyKey;
import com.ztesoft.mq.client.api.common.exception.MQClientApiException;
import com.ztesoft.mq.client.api.model.MQMessage;
import com.ztesoft.mq.client.api.producer.Producer;
import com.ztesoft.mq.client.impl.MQClientFactoryImpl;
import org.ngrinder.common.util.ScriptUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author ssssssnake
 **/
public class MQProducerBenchmarkTest {

	private static List<Producer> producerList;
	private static int activeCount;
	private Producer currentProducer;
	private MQMessage message;

	static {
		try {
			ScriptUtils.execSh("/lvdata/perftest/shell/startup.sh");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public MQProducerBenchmarkTest(String nameSrv) {

		this.message = buildMessage("ssssssnakeTopic", 1024);

		synchronized (MQProducerBenchmarkTest.class) {
			activeCount++;

			if (null == producerList) {
				producerList = new ArrayList<Producer>();

				for (int index = 0; index < 5; index++) {
					MQClientFactory factory = new MQClientFactoryImpl();
					Properties properties = new Properties();
					properties.put(PropertyKey.Producer_Id, "PID_001_" + index);
					properties.put(PropertyKey.Instance_Name, System.nanoTime());
					properties.put(PropertyKey.Namesrv_Addr, nameSrv);

					try {
						Producer producer = factory.createProducer(properties);
						producer.start();
						producer.send(this.message);
						producerList.add(producer);
					} catch (Exception e) {
						e.printStackTrace();
						throw new RuntimeException("failed to initialize the producer instance", e);
					}
				}
			}

			this.currentProducer = producerList.get(activeCount % producerList.size());
		}
	}

	private static MQMessage buildMessage(final String topic, final int messageSize) {
		final String Content = "Hello...";
		MQMessage msg = new MQMessage();
		msg.setTopic(topic);

		byte[] contentBytes = Content.getBytes(Charset.forName("UTF-8"));

		int count = messageSize / contentBytes.length;

		if (count <= 0) {
			count = 1;
		}

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < count; i++) {
			sb.append(Content);

		}

		msg.setBody(sb.toString().getBytes(Charset.forName("UTF-8")));

		return msg;
	}

	public void runTest() throws MQClientApiException {
		this.currentProducer.send(this.message);
	}

	public void teardownTest() {
		synchronized (MQProducerBenchmarkTest.class) {
			activeCount--;

			if (activeCount == 0) {
				for (Producer producer : producerList) {
					try {
						producer.shutdown();
					} catch (MQClientApiException e) {
						e.printStackTrace();
					}
				}

				producerList = null;
			}
		}
	}

	public static void main(String[] args) {
		MQProducerBenchmarkTest mqProducerBenchmarkTest = new MQProducerBenchmarkTest("172.16.17.20:9876");
	}
}
