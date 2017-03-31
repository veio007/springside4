package org.springside.modules.utils.kafka.core;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.atomic.AtomicLong;

public class AbstractBase {
	protected static final AtomicLong instanceSeq = new AtomicLong();

	protected String getClientId() {
		String clientId = System.getProperty("myName");
		if (StringUtils.isBlank(clientId)) {
			clientId = this.getClass().getSimpleName();
		}
		return clientId + "-" + instanceSeq;
	}
}
