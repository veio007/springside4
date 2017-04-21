package org.springside.modules.utils.config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

public class CommonConfig {
	public static void main(String[] args) throws ConfigurationException, InterruptedException {
		PropertiesConfiguration config = new PropertiesConfiguration("config/rp.properties");
		config.setReloadingStrategy(new FileChangedReloadingStrategy());
		while(true) {
			System.out.println(config.getProperty("list"));
			Thread.sleep(1000*5);
		}
	}
}
