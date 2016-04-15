package com.icloudmoo.business.payment.utils.wxpay;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.icloudmoo.common.exception.BusinessServiceException;
import com.icloudmoo.common.util.ConfigureUtil;

import net.sf.json.JSONObject;

public class WXUtils {

	public static final String WX_TOKEN_REDIS = "WX_TOKEN";
	public static final String PARTNER = ConfigureUtil .getProperty("wxpay.partner");
	public static final String PARTNER_KEY = ConfigureUtil.getProperty("wxpay.partner_key");
	public static final String APPID = ConfigureUtil.getProperty("wxpay.appId");
	public static final String APP_SECRET = ConfigureUtil.getProperty("wxpay.app_secret");
	public static final String APP_KEY = ConfigureUtil.getProperty("wxpay.app_key");
	public static final String NOTIFY_URL = ConfigureUtil.getProperty("wxpay.notify_url");

	private static final Logger logger = Logger.getLogger(WXUtils.class);

	public static String httpPost(String url, String entity) {
		if (url == null || url.length() == 0) {
			logger.error("httpPost, url is null");
			return null;
		}

		HttpClient httpClient = getNewHttpClient();

		HttpPost httpPost = new HttpPost(url);

		try {
			StringEntity stringEntity = new StringEntity(entity);
			// stringEntity.setContentEncoding("ISO8859-1");
			httpPost.setEntity(stringEntity);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			HttpResponse resp = httpClient.execute(httpPost);
			if (resp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				logger.error("httpGet fail, status code = "
						+ resp.getStatusLine().getStatusCode());
				return null;
			}

			return EntityUtils.toString(resp.getEntity());
		} catch (Exception e) {
			logger.error("httpPost exception, e = " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	private static class SSLSocketFactoryEx extends SSLSocketFactory {

		SSLContext sslContext = SSLContext.getInstance("TLS");

		public SSLSocketFactoryEx(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(X509Certificate[] chain,
						String authType)
						throws java.security.cert.CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain,
						String authType)
						throws java.security.cert.CertificateException {
				}
			};

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host,
					port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}

	private static HttpClient getNewHttpClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					params, registry);

			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

	public static Map<String, String> decodeXml(String content) {
		Map<String, String> result = new HashMap<String, String>();
		try {
			Document doc = DocumentHelper.parseText(content);
			Element root = doc.getRootElement();
			String returnCode = root.element("return_code").getTextTrim();
			Element ele = root.element("prepay_id");
			if (StringUtils.equals("SUCCESS", returnCode) && null != ele) {
				result.put("prepay_id", ele.getTextTrim());
			} else {
				ele = root.element("err_code_des");
				if (null != ele) {
					result.put("return_msg", ele.getTextTrim());
				} else {
					ele = root.element("return_msg");
					if (null != ele) {
						result.put("return_msg", ele.getTextTrim());
					}
				}

				result.put("return_code", "FAIL");
			}
		} catch (DocumentException e) {
			logger.error(e);
			result.put("return_code", "FAIL");
			result.put("return_msg", e.getMessage());
		}

		return result;
	}

	public static Map<String, String> transferXml(String xml) {
		Map<String, String> result = new HashMap<String, String>();
		try {
			Document doc = DocumentHelper.parseText(xml);
			Element root = doc.getRootElement();
			Element temp = null;
			for (Iterator<Element> iter = root.elementIterator(); iter
					.hasNext();) {
				temp = iter.next();
				result.put(temp.getName(), temp.getTextTrim());
			}
		} catch (DocumentException e) {
			logger.error(e);
			result.put("return_code", "FAIL");
			result.put("return_msg", e.getMessage());
		}

		return result;
	}

	private static String genNonceStr() throws UnsupportedEncodingException {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}

	private static long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

	private static String genProductArgs(String orderNo, String payFee,
			String body) {
		StringBuffer xml = new StringBuffer();

		try {
			String nonceStr = genNonceStr();

			xml.append("</xml>");
			List<BasicNameValuePair> packageParams = new LinkedList<BasicNameValuePair>();
			packageParams.add(new BasicNameValuePair("appid", APPID));
			packageParams.add(new BasicNameValuePair("body", body));
			packageParams.add(new BasicNameValuePair("input_charset", "UTF-8"));
			packageParams.add(new BasicNameValuePair("mch_id", PARTNER));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			packageParams.add(new BasicNameValuePair("notify_url", NOTIFY_URL));
			packageParams.add(new BasicNameValuePair("out_trade_no", orderNo));
			packageParams.add(new BasicNameValuePair("spbill_create_ip","127.0.0.1"));
			packageParams.add(new BasicNameValuePair("total_fee", payFee));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));

			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));

			String xmlstring = toXml(packageParams);

			return new String(xmlstring.getBytes(),"ISO8859-1");

		} catch (Exception e) {
			logger.error(e);
			return null;
		}

	}

	private static String toXml(List<BasicNameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<" + params.get(i).getName() + ">");
			sb.append(params.get(i).getValue());
			sb.append("</" + params.get(i).getName() + ">");
		}
		sb.append("</xml>");
		return sb.toString();
	}

	/**
	 * 生成签名
	 * 
	 * @throws UnsupportedEncodingException
	 */

	private static String genPackageSign(List<BasicNameValuePair> params)
			throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();

		Collections.sort(params, new Comparator<BasicNameValuePair>() {

			@Override
			public int compare(BasicNameValuePair o1, BasicNameValuePair o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(APP_SECRET);

		logger.info("包签名参数：" + sb);

		String packageSign = MD5.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
		return packageSign;
	}

	public static String genPayReq(String orderNo, String payFee, String body) {
		String prepayId = "FAIL";
		JSONObject result = new JSONObject();
		JSONObject code = new JSONObject();
		JSONObject data = new JSONObject();
		try {
			prepayId = getPrepayId(orderNo, payFee, body);
			String packageValue = "Sign=WXPay";
			String nonceStr = genNonceStr();
			String timeStamp = String.valueOf(genTimeStamp());

			StringBuilder sb = new StringBuilder();
			List<BasicNameValuePair> signParams = new LinkedList<BasicNameValuePair>();
			signParams.add(new BasicNameValuePair("appid", APPID));
			signParams.add(new BasicNameValuePair("noncestr", nonceStr));
			signParams.add(new BasicNameValuePair("package", packageValue));
			signParams.add(new BasicNameValuePair("partnerid", PARTNER));
			signParams.add(new BasicNameValuePair("prepayid", prepayId));
			signParams.add(new BasicNameValuePair("timestamp", timeStamp));

			String sign = genAppSign(signParams, sb);
			data.accumulate("appId", APPID);
			data.accumulate("partnerId", PARTNER);
			data.accumulate("prepayId", prepayId);
			data.accumulate("packageValue", packageValue);
			data.accumulate("nonceStr", nonceStr);
			data.accumulate("timeStamp", timeStamp);
			data.accumulate("sign", sign);
			code.accumulate("returnCode", "SUCCESS");
			code.accumulate("returnMsg", "调用成功！");
		} catch (Exception e) {
			code.accumulate("returnCode", "FAIL");
			code.accumulate("returnMsg", e.getMessage());
		}

		result.accumulate("returnCode", code);
		result.accumulate("data", data);
		return result.toString();
	}

	private static String genAppSign(List<BasicNameValuePair> params,StringBuilder paramSb) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(APP_SECRET);

		paramSb.append("sign str\n" + sb.toString() + "\n\n");
		String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		return appSign;
	}

	private static String getPrepayId(String orderNo, String payFee, String body)
			throws BusinessServiceException {
		String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
		String entity = genProductArgs(orderNo, payFee, body);
		logger.info("获取PrepayId参数：" + entity);
		String content = httpPost(url, entity);
		logger.info("获取PrepayId返回值：" + content);
		Map<String, String> xml = decodeXml(content);
		if (StringUtils.equalsIgnoreCase("FAIL", xml.get("return_code"))) {
			throw new BusinessServiceException("预提交订单失败："+ xml.get("return_msg"));
		}

		return xml.get("prepay_id");
	}

	public static void main(String[] args) throws Exception {
		getPrepayId("T220150623100028785937", "1", "探宝用户充值0.01元人民币。");
	}
}
