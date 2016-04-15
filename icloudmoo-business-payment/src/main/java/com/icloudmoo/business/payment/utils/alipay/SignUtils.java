package com.icloudmoo.business.payment.utils.alipay;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

public class SignUtils {

	private static final String ALGORITHM = "RSA";

	private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	private static final String DEFAULT_CHARSET = "UTF-8";

	public static String sign(String content, String privateKey) {
		try {
		    /*
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(AlipayBase64.decode(privateKey));
			KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);

			signature.initSign(priKey);
			signature.update(content.getBytes(DEFAULT_CHARSET));

			byte[] signed = signature.sign();

			return AlipayBase64.encode(signed);*/
		    return content;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * @description 加载密钥字符串
	 * 
	 * @return 密钥字符串
	 * @throws Exception
	 */
	public static String loadRsaKey(String keyFile) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(
					SignUtils.class.getResourceAsStream(keyFile)));
			String readLine = null;
			StringBuilder sb = new StringBuilder();
			while ((readLine = br.readLine()) != null) {
				// 去掉注释
				if (readLine.startsWith("-----")) {
					continue;
				} else {
					sb.append(readLine);
					sb.append('\r');
				}
			}

			return sb.toString();
		} catch (Exception e) {
			return StringUtils.EMPTY;
		} finally {
			IOUtils.closeQuietly(br);
		}
	}
}
