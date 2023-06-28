package org.edupoll.controller;

import java.io.IOException;

import org.edupoll.dto.user.UpdateProfileRequest;
import org.edupoll.dto.user.UserDeleteData;
import org.edupoll.dto.user.UserPasswordData;
import org.edupoll.dto.user.UserResponseData;
import org.edupoll.exception.UserLoginErrorExcetion;
import org.edupoll.service.JWTService;
import org.edupoll.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.NotSupportedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/user")
public class PrivateController {
	
	private final UserService userService;
	
	private final JWTService jwtService;
	
	// 비밀번호 변경을 위한 컨트롤러
	@PatchMapping("/private/password")
	public ResponseEntity<?> userModifyPasswordHandle(UserPasswordData data, 
			@AuthenticationPrincipal String principal) throws UserLoginErrorExcetion {
		
//		if(token == null) {
//			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//		}
		
//		String userEmail = jwtService.verifyToken(token);
		
		userService.modifyPassword(data, principal);
		
		return ResponseEntity.ok("비밀번호 변경완료!");
	}
	
	// 회원탈퇴를 위한 컨트롤러
	@DeleteMapping("/private/delete")
	public ResponseEntity<?> userDeleteHandle(UserDeleteData data,
			@AuthenticationPrincipal String principal) throws UserLoginErrorExcetion {
//		if(token == null || token == "") {
//			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//		}
		
//		String userEmail = jwtService.verifyToken(token);
		
		userService.deleteUser(data, principal);
		
		return ResponseEntity.ok("유저 삭제 완료");
	}
	
	// 파일업로드를 위한 컨트롤러
	// multipart/form-data (file과 text 유형이 섞여있음)
	@PostMapping("/private/info")
	public ResponseEntity<UserResponseData> updateProfileHandle(@AuthenticationPrincipal String principal, 
			UpdateProfileRequest req) throws IOException, NotSupportedException {
		
//		String emailValue = jwtService.verifyToken(token);
		
		UserResponseData user = userService.modifySpecificUser(principal, req);
		
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	// 특정유저 정보를 가져오기위한 컨트롤러
	@GetMapping("/private")
	public ResponseEntity<UserResponseData> getUserDetail(Authentication authentication) throws UserLoginErrorExcetion {		
		log.info("인증 : {}, {} ",authentication, authentication.getPrincipal());
		String email = (String)authentication.getPrincipal();
		
		// String email = jwtService.verifyToken(Authorization);
		
		UserResponseData user = userService.getUserDetail(email);
		
		log.info("getUserDetail = {} ", email);
		log.info("getUserDetail = {} ", user.toString());
		
		return new ResponseEntity<UserResponseData>(user, HttpStatus.OK);
	}
}



