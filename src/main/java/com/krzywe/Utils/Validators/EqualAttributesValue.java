package com.krzywe.Utils.Validators;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RUNTIME)
@Target(TYPE)
@Constraint(validatedBy = EqualAttributesValidator.class)
public @interface EqualAttributesValue {
	
	String message() default "Object aren't equal";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    public String[] attributes();
}
