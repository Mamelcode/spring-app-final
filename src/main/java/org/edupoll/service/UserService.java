package org.edupoll.service;

import java.util.Optional;

import org.edupoll.dto.user.UserJoinData;
import org.edupoll.dto.user.UserValidData;
import org.edupoll.entity.User;
import org.edupoll.entity.ValidCode;
import org.edupoll.exception.UserLoginErrorExcetion;
import org.edupoll.exception.ValidCodeErrorException;
import org.edupoll.repository.UserRepository;
import org.edupoll.repository.ValidCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	
	private final ValidCodeRepository validCodeRepository;
	
	@Transactional
	public UserJoinData createUser(UserJoinData data) throws ValidCodeErrorException {
		userRepository.findByEmail(data.getEmail())
		.ifPresent(t -> {
			throw new DataIntegrityViolationException("중복된 이메일 입니다!");
		});
		
		Optional<ValidCode> valid = validCodeRepository.findByEmail(data.getEmail());
		if(valid.isEmpty() || valid.get().getState().equals("N")) {
			throw new ValidCodeErrorException("인증이 유효하지 않습니다.");
		}

		// dto => entity
		User user = new User(data);
				
		return new UserJoinData(userRepository.save(user));
	}
	
	@Transactional
	public UserValidData vaildByUser(UserValidData data) throws UserLoginErrorExcetion {
		User findUser = userRepository.findByEmail(data.getEmail())
				.orElseThrow(() -> new UserLoginErrorExcetion("존재하지 않는 아이디 입니다."));
		
		if(data.getPassword().equals(findUser.getPassword())) {
			return new UserValidData(findUser);
		}else {
			throw new UserLoginErrorExcetion("비밀번호가 틀렸습니다.");
		}
	}
}
