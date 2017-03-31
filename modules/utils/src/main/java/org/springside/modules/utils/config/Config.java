package org.springside.modules.utils.config;


import com.google.common.io.Files;
import com.netflix.config.*;
import com.netflix.config.jmx.ConfigJMXManager;
import com.netflix.config.util.ConfigurationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class Config {
	public static void main(String[] args) throws IOException, InterruptedException {
		// netflix config使用
		Properties prop = ConfigurationUtils.loadPropertiesFromInputStream(
				new FileInputStream(new File("config/rp.properties")));
		System.out.println(prop);

		DynamicConfiguration configuration = new DynamicConfiguration(
				new ConfigurationSourceForProperty(new File("config/rp.properties").toURI().toURL()),
				new DelayPollingScheduler());

		ConfigurationManager.install(configuration); // source-config
		ConfigJMXManager.registerConfigMbean(configuration); //jmx
		ConfigurationManager.loadProperties(prop); // source

		while (true) {
			System.out.println(DynamicPropertyFactory.getInstance().getStringProperty("key", "empty"));

			Thread.sleep(1000 * 10);
		}
	}


	public static class ConfigurationSourceForProperty implements PolledConfigurationSource {

		private final URL[] configUrls;

		private static final Logger logger = LoggerFactory.getLogger(ConfigurationSourceForProperty.class);

		public ConfigurationSourceForProperty(String... urls) {
			configUrls = createUrls(urls);
		}


		private static URL[] createUrls(String... urlStrings) {
			if (urlStrings == null || urlStrings.length == 0) {
				throw new IllegalArgumentException("urlStrings is null or empty");
			}
			URL[] urls = new URL[urlStrings.length];
			try {
				for (int i = 0; i < urls.length; i++) {
					urls[i] = new URL(urlStrings[i]);
				}
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
			return urls;
		}

		public ConfigurationSourceForProperty(URL... urls) {
			configUrls = urls;
		}

		@Override
		public PollResult poll(boolean initial, Object checkPoint)
				throws IOException {
			if (configUrls == null || configUrls.length == 0) {
				return PollResult.createFull(null);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			for (URL url : configUrls) {
				InputStream fin = url.openStream();
				Properties props = ConfigurationUtils.loadPropertiesFromInputStream(fin);

				for (Map.Entry<Object, Object> entry : props.entrySet()) {
					map.put((String) entry.getKey(), entry.getValue());
				}
			}
			return PollResult.createFull(map);
		}

		@Override
		public String toString() {
			return "ConfigurationSourceForProperty [fileUrls=" + Arrays.toString(configUrls)
					+ "]";
		}
	}


	public static class DelayPollingScheduler extends AbstractPollingScheduler {

		private ScheduledExecutorService executor;

		private int initialDelayMillis = 1000;

		private int delayMillis = 3000;

		public DelayPollingScheduler() {
		}

		/**
		 * {@link java.util.concurrent.ScheduledExecutorService#scheduleWithFixedDelay(Runnable, long, long, TimeUnit)}
		 */
		@Override
		protected synchronized void schedule(Runnable runnable) {
			executor = Executors.newScheduledThreadPool(1, new ThreadFactory() {
				@Override
				public Thread newThread(Runnable r) {
					Thread t = new Thread(r, "polling-property-source");
					t.setDaemon(true);
					return t;
				}
			});
			executor.scheduleWithFixedDelay(runnable, initialDelayMillis, delayMillis, TimeUnit.MILLISECONDS);
		}

		@Override
		public void stop() {
			if (executor != null) {
				executor.shutdown();
				executor = null;
			}
		}
	}
}
