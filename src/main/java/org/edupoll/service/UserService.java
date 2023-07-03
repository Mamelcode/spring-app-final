package org.edupoll.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Base64;
import java.util.Optional;

import org.edupoll.dto.KakaoAccount;
import org.edupoll.dto.user.UpdateProfileRequest;
import org.edupoll.dto.user.UserDeleteData;
import org.edupoll.dto.user.UserJoinData;
import org.edupoll.dto.user.UserPasswordData;
import org.edupoll.dto.user.UserResponseData;
import org.edupoll.dto.user.UserValidData;
import org.edupoll.dto.validcode.ValidCodeRequest;
import org.edupoll.entity.User;
import org.edupoll.entity.ValidCode;
import org.edupoll.exception.NotFoundException;
import org.edupoll.exception.UserJoinErrorException;
import org.edupoll.exception.UserLoginErrorExcetion;
import org.edupoll.exception.ValidCodeErrorException;
import org.edupoll.repository.ProfileImageRepository;
import org.edupoll.repository.UserRepository;
import org.edupoll.repository.ValidCodeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.NotSupportedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
	
	private final UserRepository userRepository;
	
	private final ValidCodeRepository validCodeRepository;
	
	private final KakaoAPIService kakaoAPIService;
	
	private final ProfileImageRepository profileImageRepository;
	
	@Value("${upload.baseDir}")
	String baseDir;
	@Value("${upload.server}")
	String uploadServer;
	
	
	// 유저 생성
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
	
	
	// 유저 인증
	@Transactional(readOnly = true)
	public UserValidData vaildByUser(UserValidData data) throws UserLoginErrorExcetion {		
		User findUser = userRepository.findByEmail(data.getEmail())
				.orElseThrow(() -> new UserLoginErrorExcetion("존재하지 않는 아이디 입니다."));
		
		if(data.getPassword().equals(findUser.getPassword())) {
			return new UserValidData(findUser);
		}else {
			throw new UserLoginErrorExcetion("비밀번호가 틀렸습니다.");
		}
	}
	
	// 비밀번호 변경
	@Transactional
	public void modifyPassword(UserPasswordData data, String email) throws UserLoginErrorExcetion {
		Optional<User> option = userRepository.findByEmail(email);
		User user = option.get();
		
		if(option.isEmpty()) {
			throw new UserLoginErrorExcetion("유저가 존재하지 않습니다.");
		}
		
		if(!user.getPassword().equals(data.getPrevPassword())) {
			throw new UserLoginErrorExcetion("현재 비밀번호가 일치하지 않습니다.");
		}
		
		user.setPassword(data.getNextPassword());
	}
	
	// 유저 삭제
	@Transactional
	public boolean deleteUser(UserDeleteData data, String email) throws UserLoginErrorExcetion {
		Optional<User> option = userRepository.findByEmail(email);
		
		if(option.isEmpty()) {
			throw new UserLoginErrorExcetion("유저가 존재하지 않습니다.");
		}
		
		User user = option.get();
		
		if(user.getSocial() != null) {
			kakaoAPIService.deleteUser(user.getSocial());
			userRepository.delete(user);
			return true;
		}else {			
			if(!user.getPassword().equals(data.getPassword())) {
				throw new UserLoginErrorExcetion("비밀번호가 일치하지 않습니다.");
			}
//			validCodeRepository.delete(validCodeRepository.findByEmail(email).get());
			validCodeRepository.deleteByEmail(email);
			userRepository.delete(user);
			return true;
		}
		
	}

	@Transactional(readOnly = true)
	public void emailAvailableCheck(ValidCodeRequest data) throws UserJoinErrorException {
		
		if(userRepository.findByEmail(data.getEmail()).isPresent()) {
			throw new UserJoinErrorException("이미 가입된 이메일 입니다.");
		}
	}
	
	@Transactional(readOnly = true)
	public UserResponseData getUserDetail(String email) throws UserLoginErrorExcetion {
		User user = userRepository.findByEmail(email)
					.orElseThrow(() -> new UserLoginErrorExcetion("존재하지않는 이메일입니다."));
		
		return new UserResponseData(user);
	}


	@Transactional
	public void updateKakoUser(KakaoAccount account, String accessToken) {
		// 인증코드를 통해 확보한 카카오유저가 없다면 save / 있다면 update
		Optional<User> option = userRepository.findByEmail(account.getEmail());
		if(option.isPresent()) {
			User saved = option.get();
			saved.setSocial(accessToken);
		}else {
			User user = new User();
			user.setEmail(account.getEmail());
			user.setName(account.getNickname());
			user.setProfile_image(account.getProfileImage());
			user.setSocial(accessToken);
			userRepository.save(user);
		}
	}

	// 특정유저 정보 업데이트
	@Transactional
	public UserResponseData modifySpecificUser(String userEmail, UpdateProfileRequest req) throws IOException, NotSupportedException {
		var foundUser = userRepository.findByEmail(userEmail).get();
		
		if(req.getProfile() == null) {
			foundUser.setName(req.getName());
			return new UserResponseData(foundUser);
		}
		
		// DTD에서 파일정보만 뽑는것
		MultipartFile multi = req.getProfile();
		if(!multi.getContentType().startsWith("image/")) {
			throw new NotSupportedException("이미지 파일만 업로드 가능합니다.");
		}
		
		// 파일을 옮기는 작업
		// 기본 세이브 경로는 propertis 에서
		String emailEncoded = new String(Base64.getEncoder().encode(userEmail.getBytes()));
		
		File saveDir = new File(baseDir+"/profile/"+emailEncoded);
		saveDir.mkdirs();
		// 파일네임 구성하기
		String fileName = System.currentTimeMillis()+"";
		fileName += multi.getOriginalFilename().substring(multi.getOriginalFilename().lastIndexOf("."));
		
		File dest = new File(saveDir, fileName);
		
		// 업로드
		multi.transferTo(dest);
		
		foundUser.setName(req.getName());
		foundUser.setProfile_image(uploadServer+"/resource/profile/"+emailEncoded+"/"+fileName);
		
		return new UserResponseData(foundUser);
	}


	public Resource loadResource(String url) throws NotFoundException, MalformedURLException {
		log.info("file url = {} ", url);
		
		var found = profileImageRepository.findTop1ByUrl(url).orElseThrow(() -> new NotFoundException("이미지없음"));
		
		log.info("file address = {} ", found.getFileAddress());
		
		return new FileUrlResource(found.getFileAddress());
	}
}
