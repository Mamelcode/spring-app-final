package org.edupoll.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class JWTService {
	
	
	
	@Value("${secretKey}")
	private String sercretKey;

	public String createToken(String email) {
		
		Algorithm algorithm = Algorithm.HMAC256(sercretKey);
		
		return JWT.create().withIssuer("finalApp")
					.withIssuedAt(new Date(System.currentTimeMillis()))
					.withExpiresAt(new Date(System.currentTimeMillis() + 1000*60*30))
					.withClaim("email", email)
					.sign(algorithm);
	}
	
	public String verifyToken(String token) {
		DecodedJWT decodedJWT;
		
		Algorithm algorithm = Algorithm.HMAC256(sercretKey);
		JWTVerifier jwtVerifier = JWT.require(algorithm)
					.withIssuer("finalApp")
					.build();
		
		decodedJWT = jwtVerifier.verify(token);
		log.info("JWT ==> "+ decodedJWT.getClaims());
		
		return decodedJWT.getClaim("email").asString();
	}
}
