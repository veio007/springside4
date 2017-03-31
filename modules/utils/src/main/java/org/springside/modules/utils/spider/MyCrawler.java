package org.springside.modules.utils.spider;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {
	private static int allsize = 0;
	private static synchronized void addSize(int size) {
		allsize += size;
	}
	public static int getSize() {
		return allsize;
	}

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String u = url.getURL().toLowerCase();
		return true;
	}

	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		System.out.println("download " + url);
		addSize(page.getContentData().length);
	}
}
