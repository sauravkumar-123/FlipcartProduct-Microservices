package com.flipcart.ExceptionHandle;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FlipcartProductTypeExceptionHandler {

	@ExceptionHandler(value= {FlipcartProductTypeException.class})
	public ResponseEntity<Object> handleProductTypeException(FlipcartProductTypeException ex){
		//@Create payload contain exception details.
		//@Return resposce entity.
		FlipcartProductTypeResponce message=new FlipcartProductTypeResponce(ZonedDateTime.now(),false,ex.getMessage(),null);
		  if(null==ex.httpStatus) {
			  return new ResponseEntity<>(message,HttpStatus.INTERNAL_SERVER_ERROR);
		  }
	   return new ResponseEntity<>(message,ex.httpStatus);
	}
	
	@ExceptionHandler(value= {NullPointerException.class})
	public ResponseEntity<Object> handleNullPointerException(NullPointerException ex){
		FlipcartProductTypeResponce message=new FlipcartProductTypeResponce(ZonedDateTime.now(),false,ex.getMessage(),null);
		return new ResponseEntity<>(message,HttpStatus.NOT_FOUND);
	}
}
