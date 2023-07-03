package org.edupoll.controller;

import org.edupoll.dto.user.UserJoinData;
import org.edupoll.dto.user.UserValidData;
import org.edupoll.dto.user.ValidateUserResponse;
import org.edupoll.dto.validcode.ValidCodeRequest;
import org.edupoll.exception.UserJoinErrorException;
import org.edupoll.exception.UserLoginErrorExcetion;
import org.edupoll.exception.ValidCodeErrorException;
import org.edupoll.service.JWTService;
import org.edupoll.service.MailService;
import org.edupoll.service.UserService;
import org.edupoll.service.ValidCodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
@RequestMapping("/api/v1/user")
public class UserController {

	private final UserService userService;
	
	private final MailService mailService;
	
	private final ValidCodeService validCodeService;
	
	private final JWTService jwtService;
	
	// 회원가입
	@PostMapping("/join")
	public ResponseEntity<UserJoinData> userJoinHandle(@Valid UserJoinData data) throws ValidCodeErrorException {
		
		UserJoinData savedData = userService.createUser(data);
		
		return new ResponseEntity<UserJoinData>(savedData, HttpStatus.CREATED);
	}
	
	// 이메일 중복체크
	@GetMapping("/available")
	public ResponseEntity<?> availableHandle(ValidCodeRequest data) throws UserJoinErrorException {
		userService.emailAvailableCheck(data);
		
		return ResponseEntity.ok("사용가능한 이메일입니다.");
	}
	
	// 토큰 받기
	@PostMapping("/valid")
	public ResponseEntity<ValidateUserResponse> userValidHandle(@Valid UserValidData data) throws UserLoginErrorExcetion {
		UserValidData findUser = userService.vaildByUser(data);
		
		String token = jwtService.createToken(data.getEmail());
		
		ValidateUserResponse resp = new ValidateUserResponse(200, token, data.getEmail());
		
		return new ResponseEntity<ValidateUserResponse>(resp, HttpStatus.OK);
	}
	
	// 인증메일 발송
	@PostMapping("/mail-code")
	public ResponseEntity<?> mailCodeCreateHandle(ValidCodeRequest data) throws MessagingException, ValidCodeErrorException {
		String code = mailService.sendValidCodeMail(data);
		
		return ResponseEntity.ok("인증코드 : "+ code);
		
	}
	
	// 인증메일코드 확인
	@PostMapping("/mail-valid")
	public ResponseEntity<?> mailValidHandle(ValidCodeRequest data) throws ValidCodeErrorException {
		log.info("이메일 인증 컨트롤러 => {}, {}", data.getCode(), data.getEmail());
		int result = validCodeService.EmailCodeValidation(data);
		
		
		return ResponseEntity.ok("인증에 성공하였습니다.");
	}
}
