package com.krzywe.Utils.Exceptions;

import java.util.List;
import java.util.Map;

public class DefaultErrorScheme {
	
	private String apiVersion;
	private ErrorBlock  errorBlock;
	
	public DefaultErrorScheme(String apiVersion, String code, String message, String domain, String reason, String errorMessage) {
		this.apiVersion = apiVersion;
		this.errorBlock = new ErrorBlock(code, message, domain, reason, errorMessage);
	}
	
	public DefaultErrorScheme(String apiVersion, String code, String message, List<Error> error) {
		this.apiVersion = apiVersion;
		this.errorBlock = new ErrorBlock(code, message, error);
	}
	
	public DefaultErrorScheme(String apiVersion, ErrorBlock errorBlock) {
		this.apiVersion = apiVersion;
		this.errorBlock = errorBlock;
	}
	
	public String getApiVersion() {
		return apiVersion;
	}

	public ErrorBlock getErrorBlock() {
		return errorBlock;
	}

	public static DefaultErrorScheme fromDefaultAttributeMap(
			String apiVersion,
			Map<String, Object> attributes
			) {
		return new DefaultErrorScheme(
				apiVersion,
				attributes.getOrDefault("status", "code not available").toString(),
				attributes.getOrDefault("message", "message not available").toString(),
				List.of(new Error(
						attributes.getOrDefault("path", "domain not available").toString(),
						attributes.getOrDefault("error", "reason not available").toString(),
						attributes.getOrDefault("message", "message not available").toString()
						))
				);
	}
	
	public Map<String, Object> toAttributeMap(){
		return Map.of(
				"apiVersion", apiVersion,
				"error", errorBlock
				);
	}

	static class ErrorBlock {
		
		private String code;
		private String message;
		private List<Error> errors;
		
		public ErrorBlock(String code, String message, List<Error> error) {
			this.code = code;
			this.message = message;
			this.errors = error;
		}

		public ErrorBlock(String code, String message, String domain, String reason, String errorMessage) {
			this.code = code;
			this.message = message;
			this.errors = List.of(new Error(domain, reason, errorMessage));
		}

		public String getCode() {
			return code;
		}

		public String getMessage() {
			return message;
		}

		public List<Error> getErrors() {
			return errors;
		}
	}
	
	static class Error {
		
		private String domain;
		private String reason;
		private String message;	
		
		public Error(String domain, String reason, String message) {
			this.domain = domain;
			this.reason = reason;
			this.message = message;
		}

		public String getDomain() {
			return domain;
		}

		public String getReason() {
			return reason;
		}

		public String getMessage() {
			return message;
		}	
	}
}
