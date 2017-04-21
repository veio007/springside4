package org.springside.modules.utils.time;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class RemoteDateUtil {

	/**
	 * @return 远程时间服务器当前时间戳
	 */
	public static long getTime() {
		long start = System.currentTimeMillis();
		try {
			NTPUDPClient timeClient = new NTPUDPClient();
			String timeServerUrl = "cn.ntp.org.cn";//asia.pool.ntp.org
			InetAddress timeServerAddress = InetAddress.getByName(timeServerUrl);
			TimeInfo timeInfo = timeClient.getTime(timeServerAddress);
			TimeStamp timeStamp = timeInfo.getMessage().getTransmitTimeStamp();

			return timeStamp.getTime();
		} catch (Throwable e) {
			throw new RuntimeException("get time from timeserver error", e);
		} finally {
			System.out.println(System.currentTimeMillis() - start);
		}
	}
}
