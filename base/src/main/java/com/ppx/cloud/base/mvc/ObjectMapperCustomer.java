package com.ppx.cloud.base.mvc;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.apache.logging.log4j.util.Strings;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.ppx.cloud.base.exception.ExceptionCode;
import com.ppx.cloud.base.exception.custom.ErrorException;
import com.ppx.cloud.base.util.DateUtils;
import com.ppx.cloud.base.util.DecimalUtils;

@SuppressWarnings("serial")
public class ObjectMapperCustomer extends ObjectMapper {
	
	public static String toJson(Object obj) {
		try {
			return new ObjectMapperCustomer().writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new ErrorException(ExceptionCode.ERROR_JAVA, "toJson error:" + e.getMessage());
		}
	}
	
	public static JsonNode toJsonNode(String str) {
	    if (Strings.isEmpty(str)) return JsonNodeFactory.instance.objectNode();
        try {
            return new ObjectMapperCustomer().readTree(str.getBytes());
        } catch (IOException e) {
            throw new ErrorException(ExceptionCode.ERROR_JAVA, "toJsonNode error:" + e.getMessage());
        } 
    }

	public ObjectMapperCustomer() {
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.TIME_PATTERN);
		super.setDateFormat(sdf);
	

		super.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		SimpleModule module = new SimpleModule();
		
		module.addSerializer(Float.class, new JsonSerializer<Float>() {
			@Override
			public void serialize(Float value, JsonGenerator jsonGenerator, SerializerProvider provider)
					throws IOException {
				DecimalFormat df = new DecimalFormat(DecimalUtils.MONEY_PATTERN);
				jsonGenerator.writeString(df.format(value));
			}
		});
		
		registerModule(module);

	}
}
