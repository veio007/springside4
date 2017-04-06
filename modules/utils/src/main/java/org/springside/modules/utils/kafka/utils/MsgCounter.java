package org.springside.modules.utils.kafka.utils;


import org.springside.modules.utils.kafka.bean.MsgCountBean;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MsgCounter {

	public static void increment(String topic, Type type) {
		MsgCountBean bean = msgCountMap.get(topic);
		if (bean == null) {
			synchronized (MsgCounter.class) {
				if (bean == null) {
					bean = new MsgCountBean();
					msgCountMap.put(topic, bean);
				}
			}
		}

		switch (type) {
			case SEND:
				bean.incrSend();
				break;
			case SENDSUCC:
				bean.incrSendSucc();
				break;
			case SENDFAIL:
				bean.incrSendFail();
				break;
			case RECV:
				bean.incrRecv();
				break;
		}
	}

	public static Map<String, String> getCountMap() {
		Map<String, String> map = new HashMap<>();
		for (String topic : msgCountMap.keySet()) {
			MsgCountBean bean = msgCountMap.get(topic);
			if (bean != null) {
				map.put(topic, bean.toString());
			}
		}
		return map;
	}

	// Map<topic, count>
	private final static Map<String, MsgCountBean> msgCountMap = new ConcurrentHashMap<>();

	public enum Type {
		SEND, SENDSUCC, SENDFAIL, RECV
	}
}
