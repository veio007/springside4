package org.springside.modules.utils.kafka.bean;

import org.softee.management.annotation.Description;
import org.softee.management.annotation.MBean;
import org.softee.management.annotation.ManagedAttribute;
import org.springside.modules.utils.kafka.utils.MsgCounter;

import java.util.Map;

@MBean
@Description("kafka monitor")
public class StatusBean {
	@ManagedAttribute
	@Description("kafka消息计数")
	public Map<String, String> getKafkaMsgCountMap() {
		return MsgCounter.getCountMap();
	}
}
