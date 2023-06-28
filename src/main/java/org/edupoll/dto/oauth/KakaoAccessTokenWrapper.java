package org.edupoll.dto.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class KakaoAccessTokenWrapper {
	
	private String tokenType;
	
//	@JsonProperty("access_Token")
	private String accessToken;
	
	private int expiresIn;
}
