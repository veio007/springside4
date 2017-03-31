package org.springside.modules.utils.kafka.bean;

import java.util.concurrent.atomic.AtomicLong;

public class MsgCountBean {
	private AtomicLong send = new AtomicLong();
	private AtomicLong sendSucc = new AtomicLong();
	private AtomicLong sendFail = new AtomicLong();
	private AtomicLong recv = new AtomicLong();


	public void incrSend() {
		this.send.incrementAndGet();
	}

	public void incrSendSucc() {
		this.sendSucc.incrementAndGet();
	}

	public void incrSendFail() {
		this.sendFail.incrementAndGet();
	}

	public void incrRecv() {
		this.recv.incrementAndGet();
	}

	@Override
	public String toString() {
		return "{" +
				"send=" + send.get() +
				", sendSucc=" + sendSucc.get() +
				", sendFail=" + sendFail.get() +
				", recv=" + recv.get() +
				'}';
	}
}
