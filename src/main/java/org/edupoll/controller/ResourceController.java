package org.edupoll.controller;

import java.net.MalformedURLException;

import org.edupoll.exception.NotFoundException;
import org.edupoll.service.UserService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//@RestController
//@RequestMapping("/resource")
//@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class ResourceController {

	private final UserService userService;
	
	// 특정 경로로 왓을때, 이미지를 보내준다.
//	@GetMapping("/profile/{filename}")
	public ResponseEntity<?> getResourceHandle(HttpServletRequest req) throws MalformedURLException, NotFoundException {
		Resource resource = userService.loadResource(req.getRequestURL().toString());
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("content-type", "image/png");
		ResponseEntity<Resource> resp = new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
		
		return resp;
	}
}
