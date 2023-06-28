package org.edupoll.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidateUserResponse {
	
	// 인증성공시 보내주는 응답객체
	private int code;
	private String token;
	private String userEmail;
	
	public ValidateUserResponse(int code, String token) {
		super();
		this.code = code;
		this.token = token;
	}
}
