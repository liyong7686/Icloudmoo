package com.icloudmoo.business.file.upload.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description 图片自增长编码
 * @author gengchong
 * @date 2013-2-20
 */
public class ImageSerialNumber {

	private static AtomicInteger count = new AtomicInteger(0);
	private ImageSerialNumber() {

	}

	public static int getCount() {
		return count.get();
	}

	public static void incCount() {
		count.incrementAndGet();
	}

	public static void decCount() {
		count.decrementAndGet();
	}
}
