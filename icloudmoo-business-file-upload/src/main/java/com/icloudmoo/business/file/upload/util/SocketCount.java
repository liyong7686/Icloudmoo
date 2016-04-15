package com.icloudmoo.business.file.upload.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description socket计数类
 * @author 
 * @date 2013-2-20
 */
public class SocketCount {

	private static AtomicInteger count = new AtomicInteger();

	private SocketCount() {

	}

	public static int getCount() {
		return count.get();
	}

	public static void incSocket() {
		count.incrementAndGet();
	}

	public static void decSocket() {
		count.decrementAndGet();
	}

}
