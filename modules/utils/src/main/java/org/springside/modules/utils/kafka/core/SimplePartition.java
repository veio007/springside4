package org.springside.modules.utils.kafka.core;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;


public class SimplePartition implements Partitioner {
	private AtomicLong count = new AtomicLong();

	// 按顺序分配到不同的partition上面
	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
		int numPartitions = cluster.partitionCountForTopic(topic);
		return Math.abs((int) (count.incrementAndGet() % numPartitions));
	}

	@Override
	public void close() {
	}

	@Override
	public void configure(Map<String, ?> configs) {
	}
}