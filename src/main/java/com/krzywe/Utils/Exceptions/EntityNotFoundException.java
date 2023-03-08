package com.krzywe.Utils.Exceptions;

public class EntityNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EntityNotFoundException(Class<?> entityClass) {
		super("Entity of type:"+entityClass.getSimpleName()+"not found.");
	}

	public EntityNotFoundException(Class<?> entityClass, String message) {
		super("Entity of type:"+entityClass.getSimpleName()+"not found:"+message);
		// TODO Auto-generated constructor stub
	}
	
	 

}
