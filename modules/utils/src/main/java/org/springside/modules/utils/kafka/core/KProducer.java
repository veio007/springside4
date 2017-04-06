package org.springside.modules.utils.kafka.core;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.kafka.clients.producer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springside.modules.utils.kafka.utils.AutoRun;
import org.springside.modules.utils.kafka.utils.MsgCounter;
import org.springside.modules.utils.kafka.utils.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

/**
 * kafka消息发送封装类
 */
public class KProducer extends AbstractBase {
	/**
	 * 构造kafka生产类实例
	 *
	 * @param json kafka生产者属性
	 */
	public KProducer(String json) {
		Properties props = Util.json2Props(json);
		this.topic = (String) props.remove("gt.topic.id");
		String num = StringUtils.defaultIfEmpty((String) props.remove("gt.producer.num"), "1");
		this.produceNum = Integer.valueOf(num);

		// 校验props是否合法
		Util.checkUndefined(ProducerConfig.configNames(), props);

		// 合并配置
		Properties newProps = new Properties();
		newProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		newProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		newProps.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "org.springside.modules.utils.kafka.core.SimplePartition");
		newProps.put(ProducerConfig.RETRIES_CONFIG, 3);
		newProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");
		newProps.put(ProducerConfig.CLIENT_ID_CONFIG, getClientId());
		Util.mergeProps(newProps, props);

		String myName = System.getProperty("myName");
		keyPrefix = String.format("%s-%s-%s-%d-", myName == null ? "default" : myName, topic,
				DateFormatUtils.format(new Date(), "MMddHHmmss"), instanceSeq.getAndIncrement()).toLowerCase();

		// 构造生产者，可以构造多个生产者加快发送速度
		for (int i = 0; i < this.produceNum; ++i) {
			KafkaProducer<String, String> producer = new KafkaProducer<>(newProps);
			producers.add(producer);
		}
	}

	/**
	 * 发送消息给kafka
	 *
	 * @param message 发送的消息
	 */
	public void send(final String message) {
		final String key = getMsgKey();
		MsgCounter.increment(topic, MsgCounter.Type.SEND);
		logger.debug("send,key:{},value:{}", key, message);
		getProducer().send(new ProducerRecord<String, String>(topic, key, message), new Callback() {
			@Override
			public void onCompletion(RecordMetadata recordMetadata, Exception e) {
				if (recordMetadata == null) {
					// 发送失败做重发??
					logger.error("send failed,key:{},value:{}", key, message, e);
					MsgCounter.increment(topic, MsgCounter.Type.SENDFAIL);
				} else {
					// 发送成功
					MsgCounter.increment(topic, MsgCounter.Type.SENDSUCC);
					logger.debug("send succ,partition:{},offset:{},key:{},value:{}", recordMetadata.partition(),
							recordMetadata.offset(), key, message);
				}
			}
		});
	}


	/**
	 * 获取消息的唯一标识码
	 *
	 * @return
	 */
	private String getMsgKey() {
		return keyPrefix + seq.getAndIncrement();
	}


	/**
	 * 顺序选择多个生产者中的一个
	 *
	 * @return
	 */
	private KafkaProducer<String, String> getProducer() {
		int index = (int) selectSeq.get() % this.produceNum;
		return producers.get(index);
	}

	static {
		AutoRun.run();
	}

	private final List<KafkaProducer<String, String>> producers = new ArrayList<>();
	private final String topic;
	private final int produceNum;
	private final Logger logger = LoggerFactory.getLogger(KProducer.class);
	private final String keyPrefix;
	private final AtomicLong seq = new AtomicLong();
	private final static AtomicLong selectSeq = new AtomicLong();
}
