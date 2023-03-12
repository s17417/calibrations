package com.krzywe.Utils.Exceptions;

import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.web.context.request.WebRequest;

public class AppErrorAttributes extends DefaultErrorAttributes {
	
	private String currentApiVersion;

	public AppErrorAttributes(String currentApiVersion) {
		this.currentApiVersion = currentApiVersion;
	}

	@Override
	public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
		var defaultAttributeMap = super.getErrorAttributes(webRequest, options);
		return DefaultErrorScheme.fromDefaultAttributeMap(
				currentApiVersion,
				defaultAttributeMap)
				.toAttributeMap();
	}
	
	
	

}
