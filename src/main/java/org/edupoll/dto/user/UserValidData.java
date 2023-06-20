package org.edupoll.dto.user;

import org.edupoll.entity.User;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserValidData {
	
	@NotBlank
	private String email;
	@NotBlank
	private String password;
	
	private String name;
	private String image;
	
	public UserValidData(User user) {
		super();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.name = user.getName();
		this.image = user.getProfile_image();
	}
	
	
	
}
