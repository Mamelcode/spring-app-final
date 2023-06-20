package org.edupoll.controller;

import java.util.Base64;

import org.edupoll.dto.user.UserJoinData;
import org.edupoll.dto.user.UserValidData;
import org.edupoll.dto.user.ValidateUserResponse;
import org.edupoll.dto.validcode.ValidCodeRequest;
import org.edupoll.exception.UserLoginErrorExcetion;
import org.edupoll.exception.ValidCodeErrorException;
import org.edupoll.service.MailService;
import org.edupoll.service.UserService;
import org.edupoll.service.ValidCodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/user")
public class UserController {

	private final UserService userService;
	
	private final MailService mailService;
	
	private final ValidCodeService validCodeService;
	
	@PostMapping("/join")
	public ResponseEntity<UserJoinData> userJoinHandle(@Valid UserJoinData data) throws ValidCodeErrorException {
		
		UserJoinData savedData = userService.createUser(data);
		
		return new ResponseEntity<UserJoinData>(savedData, HttpStatus.CREATED);
	}
	
	@PostMapping("/valid")
	public ResponseEntity<ValidateUserResponse> userValidHandle(@Valid UserValidData data) throws UserLoginErrorExcetion {
		UserValidData findUser = userService.vaildByUser(data);
		
		String encoded = Base64.getEncoder().encodeToString(data.getEmail().getBytes());
		
		ValidateUserResponse resp = new ValidateUserResponse(200, encoded);
		
		
		return new ResponseEntity<ValidateUserResponse>(resp, HttpStatus.OK);
	}
	
	@PostMapping("/mail-code")
	public ResponseEntity<?> mailCodeCreateHandle(ValidCodeRequest data) throws MessagingException, ValidCodeErrorException {
		String code = mailService.sendValidCodeMail(data);
		
		return ResponseEntity.ok(code);
		
	}
	
	@PostMapping("/mail-valid")
	public ResponseEntity<?> mailValidHandle(ValidCodeRequest data) throws ValidCodeErrorException {
		int result = validCodeService.EmailCodeValidation(data);
		
		return ResponseEntity.ok(result);
	}
}
