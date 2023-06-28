package org.edupoll.service;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

import org.edupoll.dto.KakaoAccount;
import org.edupoll.dto.oauth.KakaoAccessTokenWrapper;
import org.edupoll.dto.oauth.KakaoAuthroizedCallbackRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoAPIService {
	
	public KakaoAccessTokenWrapper getAccessToken(String code) {
		String tokenURL = "https://kauth.kakao.com/oauth/token";
		
		RestTemplate template = new RestTemplate();
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", "67c8ded12ce74471c10663984f114b81");
		body.add("redirect_uri", "http://192.168.4.59:3000/flow/kakao/callback");
		body.add("code", code);
		
		HttpEntity<MultiValueMap<String, String>> entity = 
				new HttpEntity<>(body, headers);
		
		ResponseEntity<KakaoAccessTokenWrapper> result = 
				template.postForEntity
				(tokenURL, entity, KakaoAccessTokenWrapper.class);
		
		log.info("getAccessToken => "+ result.getBody().getAccessToken());
		
		return result.getBody();
		
	}

	public KakaoAccount getUserInfo(String accessToken) throws JsonMappingException, JsonProcessingException {
		String targetURL = "https://kapi.kakao.com/v2/user/me";
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Authorization", "Bearer "+ accessToken);
		headers.add("Content-type", "Content-type: application/x-www-form-urlencoded;charset=utf-8");
		
		RestTemplate template = new RestTemplate();
		
		RequestEntity<?> req = new RequestEntity<>(headers, HttpMethod.GET, URI.create(targetURL));
		
		ResponseEntity<String> resp = template.exchange(req, String.class);
		
		log.info("body => "+ resp.getBody());
		
		ObjectMapper mapper = new ObjectMapper();
//		KakaoAccount result = mapper.readValue(resp.getBody(), KakaoAccount.class);
//		System.out.println("email => "+ result.getEmail());
		
		JsonNode node = mapper.readTree(resp.getBody());
		
		String email = node.get("id").asText("") + "@kakao.user";
		String nickname = node.get("properties").get("nickname").asText();
		String image = node.get("properties").get("profile_image").asText();
//		String email = node.get("kakao_account").get("email").asText();
		
		return new KakaoAccount(email, nickname, image);
	}
	
	public void deleteUser(String accessToken) {
		String deleteURL = "https://kapi.kakao.com/v1/user/unlink";
		
		RestTemplate template = new RestTemplate();
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		headers.add("Authorization", "Bearer "+ accessToken);
		
//		RequestEntity<?> request = new RequestEntity<>(headers, HttpMethod.POST, URI.create(deleteURL));
//		ResponseEntity<String> resp =  template.exchange(request, String.class);
		
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(headers);
		ResponseEntity<String> req = template.postForEntity(deleteURL, entity, String.class);
		log.info("kakao unlink = "+ req.getBody());
	}
	
	// spring 에서 rest api 를 호출 하기위한 라이브러리
	// RestTemplate - 동기 , Webclient - 비동기
}
