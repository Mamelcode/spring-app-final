package org.edupoll.controller;

import java.util.Base64;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class FollowController {
	
	@GetMapping("/{user}/following")
	public ResponseEntity<?> getFollowingList(@PathVariable String user, 
			@RequestHeader(name = "secret", required = false) String secret) {
		
		log.info("user = "+ user);
		log.info("auth = "+ secret);
		if(secret == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		String decode = new String(Base64.getDecoder().decode(secret));
		log.info("secret user => "+ decode);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
