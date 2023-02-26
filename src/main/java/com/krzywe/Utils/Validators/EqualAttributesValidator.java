package com.krzywe.Utils.Validators;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EqualAttributesValidator implements ConstraintValidator<EqualAttributesValue, Object> {

	private String[] attributes;
	@Override
	public void initialize(EqualAttributesValue arg0) { 
		attributes=arg0.attributes();
	}
	
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		var pathList = Arrays.stream(attributes)
				.map(obj -> obj.split("\\."))
				.collect(Collectors.toList());
		Set<Object> objList = new HashSet<>();
		int nullObj = 0;
		
		for (var arr : pathList) {
			Object obj;
			try {
				obj = getValue(value, arr);
				if (obj!=null)objList.add(obj);
				else nullObj+=1;
			} catch (SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException | IntrospectionException e) {
				e.printStackTrace();
			}
		}
		return objList.size()==1&&nullObj==0?true:objList.size()==0?true:false;
	}
	
	private Object getObject(Object value, String attribute) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (value == null) return null;
		var getter = new PropertyDescriptor(attribute, value.getClass()).getReadMethod();
		if (getter ==null) return null;
		getter.trySetAccessible();
		return getter.invoke(value);
	}
	
	private Object getValue(Object value, String[] path) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		Object source = value;
		for (var attribute : path) {
			source = getObject(source, attribute);
		}
		return source;
	}
	
	

}
