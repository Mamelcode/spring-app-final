package org.edupoll.service;

import java.util.Optional;
import java.util.UUID;

import org.edupoll.dto.validcode.ValidCodeRequest;
import org.edupoll.entity.ValidCode;
import org.edupoll.exception.ValidCodeErrorException;
import org.edupoll.repository.ValidCodeRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {
	
	private final JavaMailSender javaMailSender;
	
	private final ValidCodeRepository validCodeRepository;
	
	public static final String code = UUID.randomUUID().toString().split("-")[0];
		
	@Transactional
	public String sendValidCodeMail(ValidCodeRequest data) throws MessagingException, ValidCodeErrorException {	
		Optional<ValidCode> option = validCodeRepository.findByEmail(data.getEmail());
		if(option.isPresent() && option.get().getState().equals("Y")) {
			throw new ValidCodeErrorException("이미 인증이 처리된 계정입니다.");
		}

		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
				
		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
		messageHelper.setTo(data.getEmail());
		messageHelper.setSubject("[Edupoll]인증코드 입니다.");
		messageHelper.setText(
				"""
					<div>
						인증코드 : <b><i>%uuid%</i></b>
					</div>
				""".replace("%uuid%", code) , true);
		javaMailSender.send(mimeMessage);
				

		if(option.isPresent()) {
			ValidCode validcode = option.get();
			validcode.setCode(code);
			validCodeRepository.save(validcode);
			return code;
		}
		
		ValidCode entity = new ValidCode(data.getEmail(), code, "N"); 
		validCodeRepository.save(entity);
		return code;
	}
}
