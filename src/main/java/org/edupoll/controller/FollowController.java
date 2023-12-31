package org.edupoll.controller;

import org.edupoll.service.JWTService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
@RequiredArgsConstructor
public class FollowController {
	
	private final JWTService jwtService;
	
	@GetMapping("/{user}/following")
	public ResponseEntity<?> getFollowingList(@PathVariable String user, 
			@RequestHeader(name = "token") String token) {
		
		if(token == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		jwtService.verifyToken(token);
		
		return ResponseEntity.ok("토큰 설정완료!");
	}
}
