package org.edupoll.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@AllArgsConstructor
public class KakaoAccount {
	
	private String email;
	private String nickname;
	private String profileImage;
}
