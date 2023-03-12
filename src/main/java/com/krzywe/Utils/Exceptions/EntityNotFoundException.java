package com.krzywe.Utils.Exceptions;

public class EntityNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Class<?> entityClass;

	public EntityNotFoundException(Class<?> entityClass) {
		super("Entity of type: "+entityClass.getSimpleName()+" not found.");
		this.entityClass = entityClass;
	}

	public EntityNotFoundException(Class<?> entityClass, String message) {
		super("Entity of type:"+entityClass.getSimpleName()+"not found:"+message);
		this.entityClass = entityClass;
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}
}
