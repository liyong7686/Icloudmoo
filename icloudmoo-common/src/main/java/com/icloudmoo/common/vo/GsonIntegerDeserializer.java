
package com.icloudmoo.common.vo;

import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;


public class GsonIntegerDeserializer implements JsonDeserializer<Integer> {

	private final String numberPattern = "\\d+";

	@Override
	public Integer deserialize(JsonElement jele, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		String value = jele.getAsString();
		value = value.trim();
		Pattern p = Pattern.compile(numberPattern);
		Matcher m = p.matcher(value);
		if (m.matches()) {
			return Integer.valueOf(value);
		}

		return null;
	}
}
