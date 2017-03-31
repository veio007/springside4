package org.springside.modules.utils.spider;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

// tag 爬虫 离线
public class Main {
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		String crawlStorageFolder = "e:/data/crawl/root";
		int numberOfCrawlers = 100;

		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setMaxDepthOfCrawling(1);
		config.setIncludeBinaryContentInCrawling(true);

        /*
         * Instantiate the controller for this crawl.
         */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
		controller.addSeed("http://getui.com/cn/index.html");

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
		controller.start(MyCrawler.class, numberOfCrawlers);
		System.out.println("finished .., " + (System.currentTimeMillis() - start) + ", " + MyCrawler.getSize());
	}
}
