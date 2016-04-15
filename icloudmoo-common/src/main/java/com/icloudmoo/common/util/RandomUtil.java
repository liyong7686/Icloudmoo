package com.icloudmoo.common.util;

import java.util.Random;

/**
  * @ClassName: RandomUtil
  * @Description: TODO（用一句话描述这个类的用途）
  * @date 2015年8月21日 下午5:50:33
  *
  */
public class RandomUtil {
	public static final String allChar = "0123456789abcdefghijklmnpqrstuvwxyzABCDEFGHIJKLMNPQRSTUVWXYZ";
	public static final String letterChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String numberChar = "0123456789";
	public static final String completChar = "0123456789abcdefghijklmnpqrstuvwxyzABCDEFGHIJKLMNPQRSTUVWXYZ*@!#$%^&<>?";

	public static String generateCode(int length) // 参数为返回随机数的长度
	{
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(allChar.charAt(random.nextInt(allChar.length())));
		}
		return sb.toString();
	}

	public static String keyCode(int length) // 参数为返回随机数的长度
	{
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		int cl = completChar.length();
		for (int i = 0; i < length; i++) {
			sb.append(completChar.charAt(random.nextInt(cl)));
		}
		return sb.toString();
	}

	public static String generateString(int length) // 参数为返回随机数的长度
	{
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(numberChar.charAt(random.nextInt(numberChar.length())));
		}
		return sb.toString();
	}

	public static void main(String args[]) {
        Thread t = new Thread() {
            public void run() {
                pong();
            }
        };
        t.run();
        System.out.print("ping");

    }
    static void pong() {
        System.out.print("pong");
}

}
