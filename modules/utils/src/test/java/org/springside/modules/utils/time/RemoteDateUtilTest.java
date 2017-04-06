package org.springside.modules.utils.time;


import org.junit.Assert;
import org.junit.Test;

public class RemoteDateUtilTest {
	@Test
	public void testGetTime() {
		Assert.assertTrue(Math.abs(RemoteDateUtil.getTime() - System.currentTimeMillis()) < 10000);
	}
}
