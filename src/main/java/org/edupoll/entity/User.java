package org.edupoll.entity;

import org.edupoll.dto.user.UserJoinData;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor // 기본생성자
public class User {
	
	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	private String email;
	private String password;
	private String name;
	private String profile_image;
	private String social;
	
	public User(UserJoinData data) {
		super();
		this.email = data.getEmail();
		this.password = data.getPassword();
		this.name = data.getName();
		this.profile_image = "man1.png";
	}
}
