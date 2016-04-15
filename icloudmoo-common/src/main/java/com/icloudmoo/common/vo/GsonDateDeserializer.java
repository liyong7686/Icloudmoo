package com.icloudmoo.common.vo;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 * @author Administrator
 */
public class GsonDateDeserializer implements JsonDeserializer<Date> {

	private final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private final Logger logger = Logger.getLogger(getClass());

	private final String datePattern = "\\d{4}-\\d{2}-\\d{2}";

	@Override
	public Date deserialize(JsonElement json, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		try {
			String value = json.getAsString();
			value = value.trim();
			if (StringUtils.isEmpty(value)) {
				return null;
			}

			Pattern p = Pattern.compile(datePattern);
			Matcher m = p.matcher(value);
			if (m.matches()) {
				value = value + " 00:00:00";
			}

			return sdf.parse(value);
		} catch (Exception e) {
			logger.warn("Can not parse Date value, please check pattern[yyyy-MM-dd HH:mm:ss]!!");
		}

		return null;
	}
}
