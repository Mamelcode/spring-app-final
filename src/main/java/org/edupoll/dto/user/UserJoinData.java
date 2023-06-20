package org.edupoll.dto.user;

import org.edupoll.entity.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserJoinData {
	
	@NotNull
	@NotBlank
	String email;
	@NotNull
	@NotBlank
	String password;
	@NotNull
	@NotBlank
	String name;
	
	public UserJoinData() {
		super();
	}

	public UserJoinData(User user) {
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.name = user.getName();
	}
}
