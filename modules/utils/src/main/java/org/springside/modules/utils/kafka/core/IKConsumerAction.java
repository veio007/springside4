package org.springside.modules.utils.kafka.core;

public interface IKConsumerAction {
	public void process(String message);
}