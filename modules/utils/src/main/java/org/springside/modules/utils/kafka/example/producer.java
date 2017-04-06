package org.springside.modules.utils.kafka.example;

import com.alibaba.fastjson.JSONObject;
import org.springside.modules.utils.kafka.core.KProducer;

public class producer {
	public static void main(String[] args) throws InterruptedException {
		JSONObject object = new JSONObject();
		object.put("bootstrap.servers", "192.168.10.211:9092,192.168.10.222:9092");
		// kafka topic(添加gt.前缀是和kafka已有配置区分，在传给KafkaProducer类前会移除掉)
		object.put("gt.topic.id", "test1");
		// kafka生产者对象个数 增加个数一定程度可以加快消息发送速度
		object.put("gt.producer.num", "1");

		KProducer producer = new KProducer(object.toJSONString());
		int count = 0;
		while (true) {
			++count;
			String msg = "msg-" + count;
			producer.send(msg);
			System.out.println("sendmsg, " + msg);
		}
	}
}
