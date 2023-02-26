package com.krzywe.Utils.Validators;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EitherOrValidator implements ConstraintValidator<EitherOr, Object> {

	private String[] attributes;
	@Override
	public void initialize(EitherOr arg0) { 
		attributes=arg0.attributes();
	}
	
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		return Arrays.asList(attributes)
		.stream()
		.map(obj -> {
			try {
				return value.getClass().getDeclaredField(obj);
			} catch (NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		})
		.map(obj -> {
			obj.trySetAccessible();
			try {
				return obj.get(value);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		})
		.filter(obj -> obj!=null)
		.collect(Collectors.toList()).size()<2;
	}

}
