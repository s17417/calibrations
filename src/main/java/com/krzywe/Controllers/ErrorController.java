package com.krzywe.Controllers;

import java.util.Map;

import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/error")
public class ErrorController extends AbstractErrorController {

	public ErrorController(ErrorAttributes errorAttributes) {
		super(errorAttributes);
	}
	
	@RequestMapping
	public ResponseEntity<Map<String,Object>> error (HttpServletRequest httpServletRequest){
		var map = this.getErrorAttributes(httpServletRequest, ErrorAttributeOptions.defaults());
		var status = this.getStatus(httpServletRequest);
		return new ResponseEntity<>(map, status);
	}
}
