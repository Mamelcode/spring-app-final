package org.edupoll.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserPasswordData {
		
	String prevPassword;
	String nextPassword;
	
}
