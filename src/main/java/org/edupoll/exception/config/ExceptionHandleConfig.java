package org.edupoll.exception.config;

import org.edupoll.dto.error.ErrorResponse;
import org.edupoll.exception.UserLoginErrorExcetion;
import org.edupoll.exception.ValidCodeErrorException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandleConfig {

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> UserJoinErrorExceptionHandle(DataIntegrityViolationException e) {
		ErrorResponse error = new ErrorResponse(e.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(UserLoginErrorExcetion.class)
	public ResponseEntity<ErrorResponse> UserJoinErrorExceptionHandle(UserLoginErrorExcetion e) {
		ErrorResponse error = new ErrorResponse(e.getMessage(), System.currentTimeMillis());
		
		if(e.getMessage().equals("존재하지 않는 아이디 입니다.")) {
			return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		}
		else {
			return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
		}
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> validException(MethodArgumentNotValidException e) {
		ErrorResponse error = new ErrorResponse("잘못된 형식 입니다!", System.currentTimeMillis());
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ValidCodeErrorException.class)
	public ResponseEntity<ErrorResponse> validCodeException(ValidCodeErrorException e) {
		ErrorResponse error = new ErrorResponse(e.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
	}
}
