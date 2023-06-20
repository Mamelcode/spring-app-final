package org.edupoll.dto.validcode;

import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ValidCodeRequest {
	
	@Email
	private String email;	// 발급받은 이메일
	private String code;	// 인증코드
}
