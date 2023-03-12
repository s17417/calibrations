package com.krzywe.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.krzywe.Utils.Exceptions.AppErrorAttributes;

@Configuration
public class ConfigAttributeMap {
	
	@Value("${app.api.version}")
	private String apiVersion;
	
	@Bean
	public DefaultErrorAttributes errorAttributes() {
		return new AppErrorAttributes(apiVersion);
	}
	

}
