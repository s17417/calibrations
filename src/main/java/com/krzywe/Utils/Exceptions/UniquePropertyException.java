package com.krzywe.Utils.Exceptions;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class UniquePropertyException extends Exception {
	
private static final long serialVersionUID = 1L;
	
	private Class<?> entityClass;
	
	private List<Field> properties;
	
	private List<?> values;

	public UniquePropertyException(Class<?> entityClass, List<Field> properties, List<?> values) {
		super("Entity of type: "
				+entityClass.getSimpleName()
				+" violates unique field constraint on property: "
				+properties
				.stream()
				.map(Field::getName)
				.collect(Collectors.joining("; "))
		);
		this.entityClass = entityClass;
		this.properties=properties;
		this.values=values;
	}

	public UniquePropertyException(Class<?> entityClass, List<Field> properties, List<?> values, String message) {
		super("Entity of type: "
				+entityClass.getSimpleName()
				+" violates unique field constraint on property: "
				+properties
				.stream()
				.map(Field::getName)
				.collect(Collectors.joining("; "))
				+"\n"+message);
		this.entityClass = entityClass;
		this.properties=properties;
		this.values=values;
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public List<Field> getProperty() {
		return properties;
	}

	public List<?> getValues() {
		return values;
	}
	
}
