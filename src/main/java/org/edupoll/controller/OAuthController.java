package org.edupoll.controller;

import org.edupoll.dto.KakaoAccount;
import org.edupoll.dto.oauth.KakaoAccessTokenWrapper;
import org.edupoll.dto.oauth.KakaoAuthroizedCallbackRequest;
import org.edupoll.dto.oauth.OAuthSignResponse;
import org.edupoll.dto.oauth.ValidateKakaoReqeust;
import org.edupoll.dto.user.ValidateUserResponse;
import org.edupoll.service.JWTService;
import org.edupoll.service.KakaoAPIService;
import org.edupoll.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
public class OAuthController {
	
	private final KakaoAPIService kakaoApiService;
	
	private final JWTService jwtService;
	
	private final UserService userService;
	
	@Value("${kakao.restapi.key}")
	String kakaoRestApiKey;
	
	@Value("${kakao.redirect.url}")
	String kakaoRedirectURL;
	
	// 카카오 인증주소를 보내주는곳
	@GetMapping("/kakao")
	public ResponseEntity<OAuthSignResponse> oauthKakaoHandle() {
		var response = new OAuthSignResponse(200, "https://kauth.kakao.com/oauth/authorize?"
				+ "response_type=code&client_id=67c8ded12ce74471c10663984f114b81&redirect_uri=http://192.168.4.59:3000/flow/kakao/callback");
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
//	// 이코드값을 프론트에서 백으로 전달해주는 방식으로 바뀌게됨.
//	@GetMapping("/kakao/callback")
//	public ResponseEntity<?> oauthKakaoCallbackHandle
//			(KakaoAuthroizedCallbackRequest req) {
//		
//		log.info("kakao callback Handle => "+ req.getCode());
//				
//		return new ResponseEntity<>(HttpStatus.OK);
//	}
	
	@PostMapping("/kakao")
	public ResponseEntity<ValidateUserResponse> oauthKakaoPostHandle(ValidateKakaoReqeust req) throws JsonMappingException, JsonProcessingException {
		KakaoAccessTokenWrapper wrapper = kakaoApiService.getAccessToken(req.getCode());
		
		KakaoAccount account = kakaoApiService.getUserInfo(wrapper.getAccessToken());
		String token = jwtService.createToken(account.getEmail());
		log.info("인증유저 => "+ account);
		
		userService.updateKakoUser(account, wrapper.getAccessToken());
		
		ValidateUserResponse resp = new ValidateUserResponse(200, token, account.getEmail());
		
		return new ResponseEntity<>(resp, HttpStatus.OK);
	}
}
