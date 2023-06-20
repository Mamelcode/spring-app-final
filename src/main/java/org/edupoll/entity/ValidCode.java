package org.edupoll.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter @Getter
@NoArgsConstructor
@Table(name = "verificationCodes")
public class ValidCode {
	
	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private String email;	// 발급받은 이메일
	private String code;	// 인증코드
	private Date created;	// 발급시기
	private String state;	// 인증여부
	
	public ValidCode(String email, String code, String state) {
		this.email = email;
		this.code = code;
		this.state = state;
	}
	
	@PrePersist
	public void prePersist() {
		created = new Date();
	}
}
