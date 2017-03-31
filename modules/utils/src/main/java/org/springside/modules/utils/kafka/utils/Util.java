package org.springside.modules.utils.kafka.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Properties;
import java.util.Set;

/**
 * 工具类
 */
public class Util {
	/**
	 * 检查是否存在未定义配置
	 * @param sets
	 * @param props
	 */
	public static void checkUndefined(Set<String> sets, Properties props) {
		for (Object key : props.keySet()) {
			if (!sets.contains(key)) {
				throw new RuntimeException("kafka check undefined prop:" + key);
			}
		}
	}


	/**
	 * 合并配置
	 * @param newProps
	 * @param oldProps
	 * @return
	 */
	public static Properties mergeProps(Properties newProps, Properties oldProps) {
		for(Object prop : oldProps.keySet()) {
			newProps.put(prop, oldProps.get(prop));
		}
		return newProps;
	}

	/**
	 * json转换为Properties
	 * @param json
	 * @return
	 */
	public static Properties json2Props(String json) {
		JSONObject object = (JSONObject)JSON.parse(json);
		Properties props = new Properties();
		for(Object key : object.keySet()) {
			props.put(key, object.get(key));
		}
		return props;
	}

	public static void main(String[] args) {
		JSONObject o = (JSONObject)JSON.parse("{\"p\":1, \"s\":\"t\"}");
		System.out.println(o.get("p"));
	}
}
