package org.edupoll.dto.oauth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
public class KakaoAuthroizedCallbackRequest {
	private String code;
	private String error;
}
