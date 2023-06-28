package org.edupoll.dto.user;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileRequest {
	
	private String name;
	private MultipartFile profile;
	
	// 여러 파일을 넘길경우
	// private MultipartFile[] profile;
	// private List<MultipartFile> profile;
	
}
// 부트에서는 멀티파트 요청 처리를 할 수 있도록 기본설정이 되어있다.
// File은 타입을 MultipartFile
// Text는 타입을 자료형