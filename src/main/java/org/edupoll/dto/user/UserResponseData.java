package org.edupoll.dto.user;

import org.edupoll.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseData {
	private Long id;
	private String email;
	private String name;
	private String profileImage;
	
	
	public UserResponseData(User user) {
		super();
		this.id = user.getId();
		this.email = user.getEmail();
		this.name = user.getName();
		this.profileImage = user.getProfile_image();
	}
}
