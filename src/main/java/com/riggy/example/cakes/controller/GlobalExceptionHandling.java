package com.riggy.example.cakes.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

@ControllerAdvice
public class GlobalExceptionHandling extends ResponseEntityExceptionHandler {
	

	  @ExceptionHandler(ConstraintViolationException.class)
	  @ResponseStatus(HttpStatus.BAD_REQUEST)
	  @ResponseBody
	  Map<String, String> onConstraintValidationException(
	      ConstraintViolationException e) {
		  Map<String, String> errors = new HashMap<>();
	    for (ConstraintViolation violation : e.getConstraintViolations()) {
	      errors.put(violation.getPropertyPath().toString(), violation.getMessage());
	    }
	    return errors;
	  }

	  @Override
	  protected ResponseEntity<Object> handleMethodArgumentNotValid(
	      MethodArgumentNotValidException ex, HttpHeaders headers,
	      HttpStatus status, WebRequest request) {

	    Map<String, List<String>> body = new HashMap<>();

	    List<String> errors = ex.getBindingResult()
	        .getFieldErrors()
	        .stream()
	        .map(DefaultMessageSourceResolvable::getDefaultMessage)
	        .collect(Collectors.toList());

	    body.put("errors", errors);

	    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	  }
	  
	  
	  @Override
	  protected ResponseEntity<Object> handleHttpMessageNotReadable(
				HttpMessageNotReadableException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
		  String message = "";
		  if(e.getMostSpecificCause() instanceof UnrecognizedPropertyException) {
			  UnrecognizedPropertyException upe = (UnrecognizedPropertyException) e.getMostSpecificCause();
			  message = "Unrecognized property: " + upe.getPropertyName();
		  } else {
			  message = e.getClass().getName();
		  }
		  
		  return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	  }
}
