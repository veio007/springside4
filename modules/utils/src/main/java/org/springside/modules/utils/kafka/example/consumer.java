package org.springside.modules.utils.kafka.example;

import com.alibaba.fastjson.JSONObject;
import org.springside.modules.utils.kafka.core.IKConsumerAction;
import org.springside.modules.utils.kafka.core.KConsumer;

public class consumer {
	public static void main(String[] args) throws InterruptedException {
		JSONObject object = new JSONObject();
		object.put("bootstrap.servers", "192.168.10.211:9092,192.168.10.222:9092");
		object.put("group.id", "testgroup");
		// kafka topic(添加gt.前缀是和kafka已有配置区分，在传给KafkaConsumer类前会移除掉)
		object.put("gt.topic.id", "test1");
		// 消费速度限制 比如每秒100条记录
		object.put("gt.ratelimit.sec", "100");
		// 消息有效期 过期的消息不再处理 单位ms
		object.put("gt.retention.ms", "10000");

		KConsumer consumer = new KConsumer(object.toJSONString(), new IKConsumerAction() {
			@Override
			public void process(String message) {
				System.out.println(message);
			}
		});

		Thread.sleep(Integer.MAX_VALUE);
	}
}
