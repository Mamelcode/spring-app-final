package org.edupoll.service;

import java.util.Optional;

import org.edupoll.dto.validcode.ValidCodeRequest;
import org.edupoll.entity.ValidCode;
import org.edupoll.exception.ValidCodeErrorException;
import org.edupoll.repository.ValidCodeRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidCodeService {
	
	private final ValidCodeRepository validCodeRepository;
	
	@Transactional
	public int EmailCodeValidation(ValidCodeRequest data) throws ValidCodeErrorException {
		Optional<ValidCode> option = validCodeRepository.findByEmail(data.getEmail());
		
		// 비어있으면 없는거
		option.orElseThrow(() -> new ValidCodeErrorException("유효하지않는 인증입니다."));
		
		ValidCode validCode = option.get();
		
		long elapsed = System.currentTimeMillis() - validCode.getCreated().getTime();
		if(elapsed > 1000*60*10) {
			throw new ValidCodeErrorException("인증시간 만료 : 유효하지않는 인증입니다");
		}
		
		if(data.getCode().equals(validCode.getCode())) {
			validCode.setState("Y");
			return 1;
		}else {
			throw new ValidCodeErrorException("옳바르지 않는 인증입니다.");
		}
	}
}
