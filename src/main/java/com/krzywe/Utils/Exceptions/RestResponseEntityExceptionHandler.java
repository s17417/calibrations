package com.krzywe.Utils.Exceptions;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler{
	
	@Value("${app.api.version}")
	private String currentApiVersion;
	
	@ExceptionHandler({ConstraintViolationException.class})
	public ResponseEntity<DefaultErrorScheme> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req){
		
		return ResponseEntity
				.unprocessableEntity()
				.body(new DefaultErrorScheme(
						currentApiVersion,
						Integer.toString(HttpStatus.UNPROCESSABLE_ENTITY.value()),
						ex.getClass().getSimpleName(),
						ex.getConstraintViolations()
						.stream()
						.map(obj -> new DefaultErrorScheme.Error(
								req.getRequestURI(),
								obj.getMessage(),
								"invalid field: "+ obj.getPropertyPath()
								))
						.collect(Collectors.toList())
						));		
	}
	
	@ExceptionHandler({MethodArgumentNotValidException.class})
	public ResponseEntity<DefaultErrorScheme> handleMethodArgumentViolation(MethodArgumentNotValidException ex, HttpServletRequest req){
		
		return ResponseEntity
				.unprocessableEntity()
				.body(new DefaultErrorScheme(
						currentApiVersion,
						Integer.toString(HttpStatus.UNPROCESSABLE_ENTITY.value()),
						ex.getClass().getSimpleName(),
						ex.getAllErrors()
						.stream()
						.map(obj -> new DefaultErrorScheme.Error(
								req.getRequestURI(),
								obj.getDefaultMessage(),
								"invalid field: "+((FieldError) obj).getField()
								))
						.collect(Collectors.toList())
						));		
	}
	
	@ExceptionHandler({EntityNotFoundException.class})
	public ResponseEntity<DefaultErrorScheme> handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest req){
		
		return new ResponseEntity<>(new DefaultErrorScheme(
				currentApiVersion,
				Integer.toString(HttpStatus.NOT_FOUND.value()),
				ex.getClass().getSimpleName(),
				List.of(new DefaultErrorScheme.Error(
						req.getRequestURI(),
						ex.getMessage(),
						" - object: "+ ex.getEntityClass())
						)
				),
				HttpStatus.NOT_FOUND);
	}
	
	

}
