package com.krzywe.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ApplicationSecurity {
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
     return http
    	    .cors().and().csrf().disable()
    	    .httpBasic().disable()
    	    .build();	
    }
    
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
         return web -> web.ignoring().antMatchers("/**");
    }
}
